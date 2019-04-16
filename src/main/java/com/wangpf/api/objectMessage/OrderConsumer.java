package com.wangpf.api.objectMessage;

import java.util.Date;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/10 17:14
 */
public class OrderConsumer {
    private String name;
    private int age;
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
