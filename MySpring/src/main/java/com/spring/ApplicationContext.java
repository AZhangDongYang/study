package com.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private Class configClass;
    // 单例池 用于保存@Scope("singleton")所创建的单例对象
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<String, BeanDefinition>();
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public ApplicationContext(Class configClass) {
        this.configClass = configClass;

        scan(configClass);
        for(Map.Entry<String, BeanDefinition> entry: beanDefinitions.entrySet()){
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if(beanDefinition.getScope().equals("singleton")){
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private void scan(Class configClass) {
        // 解析配置类
        ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String scanPath = componentScan.value();
        scanPath = scanPath.replace(".", "/"); // com.zdy.service => com/zdy/service

        // 扫描
        ClassLoader classLoader = ApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(scanPath);
        File file = new File(resource.getFile());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f: files){
                String fileName = f.getAbsolutePath();
                String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                className = className.replace("\\", ".");

                try {
                    Class aClass = classLoader.loadClass(className);
                    // 类上有Component注解
                    if(aClass.isAnnotationPresent(Component.class)){
                        Component component = (Component) aClass.getAnnotation(Component.class);
                        String beanName = component.value();
                        if(BeanPostProcessor.class.isAssignableFrom(aClass)){
                            BeanPostProcessor beanPostProcessor = (BeanPostProcessor)aClass.getDeclaredConstructor().newInstance();
                            beanPostProcessors.add(beanPostProcessor);
                        }
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(aClass);
                        // 包含Scope注解
                        if(aClass.isAnnotationPresent(Scope.class)){
                            Scope scope = (Scope)aClass.getAnnotation(Scope.class);
                            beanDefinition.setScope(scope.value());
                        }else{
                            beanDefinition.setScope("singleton");
                        }
                        beanDefinitions.put(beanName, beanDefinition);
                    }
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            // 依赖注入 AutoWired功能实现
            for (Field declaredField: clazz.getDeclaredFields()){
                if(declaredField.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = declaredField.getAnnotation(Autowired.class);
                    Object bean = getBean(declaredField.getName());
                    if(bean == null && !autowired.required()){
                        System.out.println("Bean Must Not Null");
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }

            // Aware回调
            if (instance instanceof BeanNameAware){
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean){
                ((InitializingBean)instance).afterPropertiesSet();
            }

            // BeanPostProcessor 给用户实现回调功能
            for(BeanPostProcessor beanPostProcessor: beanPostProcessors){
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            return instance;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getBean(String beanName) {
        if(beanDefinitions.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            // 单例bean
            if(beanDefinition.getScope().equals("singleton")){
                Object o = singletonObjects.get(beanName);
                return o;
            } else if(beanDefinition.getScope().equals("prototype")){
                // 原型bean 需要创建bean对象
                Object bean = createBean(beanName, beanDefinition);
                return bean;
            } else {
                System.out.println("Scope Value InValid");
                return null;
            }
        }
        return null;
    }
}
