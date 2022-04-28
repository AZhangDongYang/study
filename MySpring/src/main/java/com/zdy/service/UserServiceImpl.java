package com.zdy.service;

import com.spring.*;

@Component("userService")
@Scope("prototype")
public class UserServiceImpl implements UserService, BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    private String beanName;

    private String userName;

    public void test() {
        System.out.println("UserService::test()");
        System.out.println(orderService);
        System.out.println(beanName);
    }

    public void setBeanName(String name) {
        beanName = name;
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("UserServiceImpl::afterPropertiesSet()");
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
