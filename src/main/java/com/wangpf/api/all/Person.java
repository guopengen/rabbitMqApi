package com.wangpf.api.all;

import java.util.Date;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/15 14:33
 */
public class Person {
    private String name;
    private int age;
    private String birthDate;

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
