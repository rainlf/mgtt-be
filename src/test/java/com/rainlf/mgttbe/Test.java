package com.rainlf.mgttbe;

import java.lang.reflect.Field;

public class Test {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        String name = "123456";

        Class clazz = name.getClass();
        Field field = clazz.getField("value");
        field.setAccessible(true);
        System.out.printf("name: %s\n", field.get(name));
    }
}
