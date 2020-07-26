package com.github.wz2cool.utils.asm.helper;

/**
 * @author Frank
 * @date 2020/07/26
 **/
public class ClassInfoHelper {

    public static String getAsmClassName(Class clazz) {
        return clazz.getName().replace(".", "/");
    }
}
