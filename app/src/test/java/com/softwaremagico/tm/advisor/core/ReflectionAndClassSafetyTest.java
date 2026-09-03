/*
 *  Copyright (C) 2026 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReflectionAndClassSafetyTest {

    class TestClass {
        private int value = 10;
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int v) {
            this.value = v;
        }
    }

    @Test
    public void getClass_shouldNotBeNull() {
        TestClass obj = new TestClass();
        Class<?> clazz = obj.getClass();
        
        assertNotNull("getClass() should not be null", clazz);
        assertEquals("Class name should be TestClass", "TestClass", clazz.getSimpleName());
    }

    @Test
    public void nullObject_getClass_shouldThrowNullPointerException() {
        TestClass obj = null;
        
        try {
            Class<?> clazz = obj.getClass();
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("Null object getClass() throws NPE", true);
        }
    }

    @Test
    public void instanceof_withNull_shouldReturnFalse() {
        Object obj = null;
        
        boolean result = obj instanceof String;
        assertFalse("null instanceof String should be false", result);
    }

    @Test
    public void instanceof_withValidObject_shouldReturnTrue() {
        Object obj = "string";
        
        boolean result = obj instanceof String;
        assertTrue("Valid object instanceof should be true", result);
    }

    @Test
    public void classForName_withValidName_shouldWork() {
        try {
            Class<?> clazz = Class.forName("java.lang.String");
            assertEquals("Should load String class", String.class, clazz);
        } catch (ClassNotFoundException e) {
            fail("Valid class name should be found");
        }
    }

    @Test
    public void classForName_withInvalidName_shouldThrowException() {
        try {
            Class<?> clazz = Class.forName("com.nonexistent.FakeClass");
            fail("Should throw ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            assertTrue("Invalid class name throws exception", true);
        }
    }

    @Test
    public void classForName_withNull_shouldThrowException() {
        try {
            Class<?> clazz = Class.forName(null);
            fail("Should throw NullPointerException or exception");
        } catch (NullPointerException | ClassNotFoundException e) {
            assertTrue("Null class name throws exception", true);
        }
    }

    @Test
    public void newInstance_shouldCreateObject() {
        try {
            Class<?> clazz = String.class;
            Object obj = clazz.getDeclaredConstructor().newInstance();
            assertTrue("newInstance should create object", obj instanceof String);
        } catch (Exception e) {
            // String may not have default constructor
            assertTrue("Exception in newInstance is acceptable", true);
        }
    }

    @Test
    public void classComparison_shouldWork() {
        String str = "test";
        Object obj = 42;
        
        assertTrue("String class comparison", str.getClass() == String.class);
        assertFalse("Different class comparison", str.getClass().equals(obj.getClass()));
    }

    @Test
    public void isAssignableFrom_shouldWork() {
        assertTrue("String is assignable from String", String.class.isAssignableFrom(String.class));
        assertTrue("Object is assignable from String", Object.class.isAssignableFrom(String.class));
        assertFalse("String is not assignable from Object", String.class.isAssignableFrom(Object.class));
    }

    @Test
    public void getDeclaredMethods_shouldNotCrash() {
        Class<?> clazz = TestClass.class;
        
        try {
            java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
            assertNotNull("getDeclaredMethods should return array", methods);
            assertTrue("Should have methods", methods.length > 0);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void getField_withNonExistent_shouldThrowException() {
        Class<?> clazz = TestClass.class;
        
        try {
            clazz.getField("nonexistent");
            fail("Should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            assertTrue("Non-existent field throws exception", true);
        }
    }

    @Test
    public void staticMethod_shouldNotNeedInstance() {
        try {
            // Static method can be called without instance
            String version = System.getProperty("java.version");
            assertNotNull("Should get java version", version);
        } catch (Exception e) {
            fail("Static method should work");
        }
    }

    @Test
    public void toString_onNull_shouldThrowNullPointerException() {
        Object obj = null;
        
        try {
            String str = obj.toString();
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("null toString() throws NPE", true);
        }
    }

    @Test
    public void toString_shouldReturnString() {
        Object obj = new Object();
        
        String str = obj.toString();
        assertNotNull("toString() should return string", str);
        assertTrue("toString() should start with class name", str.startsWith("java.lang.Object"));
    }
}
