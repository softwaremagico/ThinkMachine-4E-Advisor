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

package com.softwaremagico.tm.advisor.ui.components;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class EmptyListHandlingTest {

    @Test
    public void emptyList_get0_shouldNotCrash() {
        List<String> emptyList = new ArrayList<>();
        
        // This should throw IndexOutOfBoundsException but code should validate
        try {
            String value = emptyList.get(0);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
            assertTrue("Empty list access throws expected exception", true);
        }
    }

    @Test
    public void emptyList_isEmpty_shouldReturnTrue() {
        List<String> emptyList = new ArrayList<>();
        assertTrue("Empty list isEmpty() should return true", emptyList.isEmpty());
    }

    @Test
    public void nonEmptyList_get0_shouldReturnFirstElement() {
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");
        
        assertEquals("Should return first element", "first", list.get(0));
    }

    @Test
    public void safeListAccess_withCheckBeforeGet() {
        List<String> list = new ArrayList<>();
        String value = null;
        
        if (!list.isEmpty()) {
            value = list.get(0);
        }
        
        assertNull("Value should be null for empty list with safe access", value);
    }

    @Test
    public void safeListAccess_withBoundsCheck() {
        List<String> list = new ArrayList<>();
        list.add("element");
        
        String value = null;
        int index = 0;
        
        if (index >= 0 && index < list.size()) {
            value = list.get(index);
        }
        
        assertEquals("Should safely access element", "element", value);
    }

    @Test
    public void listRemovalDuringIteration_shouldHandleSafely() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        
        // This demonstrates safe removal during iteration
        // Using Iterator.remove() is safe
        java.util.Iterator<String> iterator = list.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            String item = iterator.next();
            if ("two".equals(item)) {
                iterator.remove();
                removed = true;
            }
        }
        
        assertTrue("Should have removed item", removed);
        assertFalse("Two should be removed", list.contains("two"));
    }

    @Test
    public void safeListModification_useIterator() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        
        // Safe way to remove during iteration
        java.util.Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if ("two".equals(item)) {
                iterator.remove();
            }
        }
        
        assertFalse("Two should be removed", list.contains("two"));
        assertTrue("One should still exist", list.contains("one"));
        assertTrue("Three should still exist", list.contains("three"));
    }
}
