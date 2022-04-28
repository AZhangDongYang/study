package com.zdy.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.Proxy;

@Component("zdyBeanPostProcessor")
public class ZdyBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前");
        if(beanName.equals("userService")){
            ((UserServiceImpl)bean).setBeanName("zdy");
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        if("userService".equals(beanName)){
            Object proxyInstance = Proxy.newProxyInstance(ZdyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) ->{
                System.out.println("代理逻辑");
                return method.invoke(bean, args);
            });
            return proxyInstance;
        }
        return bean;
    }
}
