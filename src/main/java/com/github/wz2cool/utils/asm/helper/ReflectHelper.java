package com.github.wz2cool.utils.asm.helper;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Frank
 */
public class ReflectHelper {
    private ReflectHelper() {
        throw new UnsupportedOperationException();
    }

    public static Field[] getProperties(Class targetClass) {
        Collection<Field> fields = new ArrayList<>();
        getProperties(targetClass, fields);
        return fields.toArray(new Field[0]);
    }

    public static Field[] getPropertiesForCurrentClass(Class targetClass) {
        Method[] methods = targetClass.getMethods();
        Field[] fields = targetClass.getDeclaredFields();

        Collection<Field> result = new ArrayList<>();
        for (Field field : fields) {
            if (isProperty(field, methods)) {
                field.setAccessible(true);
                result.add(field);
            }
        }
        return result.toArray(new Field[0]);
    }

    public static boolean hasParent(final Class current, final Class expectParent) {
        if (current == null || expectParent == null) {
            return false;
        }

        if (current == expectParent) {
            return true;
        }

        if (expectParent.isInterface()) {
            return hasParentInterface(current, expectParent);
        } else {
            return hasParentClass(current, expectParent);
        }
    }

    public static boolean hasParentClass(final Class current, final Class expectParentClass) {
        if (current == null || expectParentClass == null) {
            return false;
        }

        if (current == expectParentClass) {
            return true;
        }

        Class parentClass = current.getSuperclass();
        if (parentClass == null) {
            return false;
        }

        if (parentClass == expectParentClass) {
            return true;
        }

        return hasParent(parentClass, expectParentClass);
    }

    public static boolean hasParentInterface(final Class current, final Class expectParentInterface) {
        if (current == null || expectParentInterface == null) {
            return false;
        }

        if (current == expectParentInterface) {
            return true;
        }

        Class[] interfaces = current.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            return false;
        }

        boolean result = false;
        for (Class iClass : interfaces) {
            if (iClass == expectParentInterface
                    || hasParent(iClass, expectParentInterface)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static boolean isProperty(final Field field, final Method[] methods) {
        if (field == null || methods == null || methods.length == 0) {
            return false;
        }

        String name = field.getName();
        String getMethodName = String.format("get%s", name);
        String isMethodName = String.format("is%s", name);
        String setMethodName = String.format("set%s", name);

        boolean hasGetMethod = false;
        boolean hasSetMethod = false;
        for (Method method : methods) {
            if (hasGetMethod && hasSetMethod) {
                break;
            }

            if (getMethodName.equalsIgnoreCase(method.getName())
                    || isMethodName.equalsIgnoreCase(method.getName())) {
                hasGetMethod = true;
            }

            if (setMethodName.equalsIgnoreCase(method.getName())) {
                hasSetMethod = true;
            }
        }
        return hasGetMethod && hasSetMethod;
    }

    private static void getProperties(Class targetClass, final Collection<Field> fields) {
        Class parentClass = targetClass.getSuperclass();
        Field[] currentFields = getPropertiesForCurrentClass(targetClass);

        Collection<Field> filterDuplicatedFields = new ArrayList<>();
        for (Field field : currentFields) {
            if (!hasContainsFieldName(field.getName(), fields)) {
                filterDuplicatedFields.add(field);
            }
        }

        fields.addAll(filterDuplicatedFields);
        if (parentClass != null) {
            getProperties(parentClass, fields);
        }
    }

    static boolean hasContainsFieldName(String fieldName, final Collection<Field> fields) {
        if (StringUtils.isBlank(fieldName)) {
            return false;
        }

        for (Field field : fields) {
            if (fieldName.equalsIgnoreCase(field.getName())) {
                return true;
            }
        }

        return false;
    }
}
