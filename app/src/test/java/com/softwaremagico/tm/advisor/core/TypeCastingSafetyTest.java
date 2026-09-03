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

public class TypeCastingSafetyTest {

    interface Animal {
        void sound();
    }

    class Dog implements Animal {
        @Override
        public void sound() {
            // Woof
        }
    }

    class Cat implements Animal {
        @Override
        public void sound() {
            // Meow
        }
    }

    @Test
    public void validCast_shouldWork() {
        Animal animal = new Dog();
        
        if (animal instanceof Dog) {
            Dog dog = (Dog) animal;
            assertNotNull("Cast should succeed", dog);
        }
    }

    @Test
    public void invalidCast_shouldThrowClassCastException() {
        Animal animal = new Dog();
        
        try {
            Cat cat = (Cat) animal;
            fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            assertTrue("Invalid cast throws exception", true);
        }
    }

    @Test
    public void castAfterInstanceof_shouldBeSafe() {
        Object obj = "string";
        
        if (obj instanceof String) {
            String str = (String) obj;
            assertEquals("Safe cast should work", "string", str);
        }
    }

    @Test
    public void castWithoutInstanceof_isRisky() {
        Object obj = 42;  // Integer, not String
        
        try {
            String str = (String) obj;
            fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            assertTrue("Unsafe cast throws exception", true);
        }
    }

    @Test
    public void castNull_shouldReturnNull() {
        Object obj = null;
        
        // Casting null is safe - returns null
        String str = (String) obj;
        assertNull("Casting null should return null", str);
    }

    @Test
    public void primitiveUnboxing_withWrongType_shouldThrowException() {
        Object obj = "string";
        
        try {
            Integer i = (Integer) obj;
            fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            assertTrue("Wrong unboxing throws exception", true);
        }
    }

    @Test
    public void autoboxing_shouldWork() {
        int primitive = 42;
        Object obj = primitive;  // Autoboxing
        
        assertTrue("Autoboxing should create Integer", obj instanceof Integer);
    }

    @Test
    public void unboxing_shouldWork() {
        Integer obj = 42;
        int primitive = obj;  // Unboxing
        
        assertEquals("Unboxing should work", 42, primitive);
    }

    @Test
    public void unboxingNull_shouldThrowNullPointerException() {
        Integer obj = null;
        
        try {
            int primitive = obj;  // Unboxing null
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("Unboxing null throws NPE", true);
        }
    }

    @Test
    public void castArrayElement_shouldCheckEachElement() {
        Object[] objects = new Object[2];
        objects[0] = "string";
        objects[1] = 42;
        
        for (Object obj : objects) {
            if (obj instanceof String) {
                String str = (String) obj;
                assertEquals("String element", "string", str);
            } else if (obj instanceof Integer) {
                Integer i = (Integer) obj;
                assertEquals("Integer element", Integer.valueOf(42), i);
            }
        }
    }

    @Test
    public void genericsCast_shouldBeCheckedAtRuntime() {
        java.util.List<String> list = new java.util.ArrayList<>();
        list.add("string1");
        list.add("string2");
        
        // Generics are erased at runtime
        java.util.List raw = list;
        
        // This could cause problems if we add wrong type
        try {
            for (Object item : raw) {
                String str = (String) item;
                assertNotNull("Should safely cast", str);
            }
        } catch (ClassCastException e) {
            fail("Should not throw exception for valid elements");
        }
    }

    @Test
    public void narrowingCast_shouldWork() {
        long longVal = 42L;
        int intVal = (int) longVal;
        
        assertEquals("Narrowing cast should work", 42, intVal);
    }

    @Test
    public void wideningCast_shouldWork() {
        int intVal = 42;
        long longVal = (long) intVal;
        
        assertEquals("Widening cast should work", 42L, longVal);
    }

    @Test
    public void floatToInt_shouldTruncate() {
        float f = 3.7f;
        int i = (int) f;
        
        assertEquals("Float to int truncates", 3, i);
    }
}
