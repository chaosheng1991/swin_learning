package com.zcs.springsecurity.entity;

import lombok.Data;

@Data
public class Test {
    String name;

    public static void main(String[] args) {
        Test test = new Test();
        test.setName("hi");
        System.out.println(test.getName());
    }
}
