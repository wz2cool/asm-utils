package com.github.wz2cool.utils.asm.helper;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Frank
 * @date 2020/07/26
 **/
public class ClassInfoHelperTest {

    @Test
    public void testAsmClassName() {
        Assert.assertEquals("java/lang/Object", ClassInfoHelper.getAsmClassName(Object.class));
        Assert.assertEquals("java/lang/Integer", ClassInfoHelper.getAsmClassName(Integer.class));
    }
}
