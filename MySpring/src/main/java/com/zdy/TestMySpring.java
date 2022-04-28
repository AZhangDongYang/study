package com.zdy;

import com.spring.ApplicationContext;
import com.zdy.service.UserService;

public class TestMySpring {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        testComponentAndComponentScanAndScope(applicationContext);
//        testAutowiredAndBeanNameAware(applicationContext);
    }
    public static void testComponentAndComponentScanAndScope(ApplicationContext applicationContext){
        Object userService1 = applicationContext.getBean("userService");
        Object userService2 = applicationContext.getBean("userService");
        Object userService3 = applicationContext.getBean("userService");
        System.out.println(userService1);
        System.out.println(userService2);
        System.out.println(userService3);
    }

    public static void testAutowiredAndBeanNameAware(ApplicationContext applicationContext){
        UserService userServiceImpl = (UserService) applicationContext.getBean("userService");
        userServiceImpl.test();
    }
}
