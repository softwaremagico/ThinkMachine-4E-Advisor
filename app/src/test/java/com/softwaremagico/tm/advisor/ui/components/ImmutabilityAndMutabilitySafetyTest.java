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

public class ImmutabilityAndMutabilitySafetyTest {

    @Test
    public void immutableList_shouldNotBeModifiable() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        
        List<String> immutable = Collections.unmodifiableList(list);
        
        try {
            immutable.add("three");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue("Immutable list should reject add", true);
        }
    }

    @Test
    public void modifyingOriginalModifiesImmutable() {
        List<String> list = new ArrayList<>();
        list.add("one");
        
        List<String> immutable = Collections.unmodifiableList(list);
        
        // Modifying original list affects immutable view
        list.add("two");
        
        assertEquals("Original list change affects immutable view", 2, immutable.size());
    }

    @Test
    public void immutableEmptyList_shouldNotAdd() {
        List<String> empty = Collections.emptyList();
        
        try {
            empty.add("item");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue("Empty immutable list should reject add", true);
        }
    }

    @Test
    public void synchronizedList_shouldBeThreadSafe() {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        
        list.add("item1");
        list.add("item2");
        
        assertTrue("Synchronized list should contain items", list.contains("item1"));
        assertEquals("Synchronized list should have 2 items", 2, list.size());
    }

    @Test
    public void singletonList_shouldHaveOneElement() {
        List<String> singleton = Collections.singletonList("only");
        
        assertEquals("Singleton should have 1 element", 1, singleton.size());
        assertEquals("Singleton should contain item", "only", singleton.get(0));
    }

    @Test
    public void singletonList_shouldNotAllowModification() {
        List<String> singleton = Collections.singletonList("only");
        
        try {
            singleton.set(0, "new");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue("Singleton list should not allow modification", true);
        }
    }

    @Test
    public void shallowCopyOfList_shouldBeIndependent() {
        List<String> original = new ArrayList<>();
        original.add("one");
        original.add("two");
        
        List<String> copy = new ArrayList<>(original);
        
        copy.add("three");
        
        assertFalse("Copy should not affect original", original.contains("three"));
        assertEquals("Original should have 2 items", 2, original.size());
    }

    @Test
    public void listOfMutableObjects_shallowCopyIsNotSafe() {
        class MutableObject {
            int value;
            MutableObject(int v) { this.value = v; }
        }
        
        List<MutableObject> original = new ArrayList<>();
        MutableObject obj = new MutableObject(10);
        original.add(obj);
        
        List<MutableObject> copy = new ArrayList<>(original);
        
        // Modifying object in copy affects original
        copy.get(0).value = 20;
        
        assertEquals("Original object was modified", 20, original.get(0).value);
    }

    @Test
    public void nullSafeCollection_shouldHandle() {
        List<String> list = new ArrayList<>();
        list.add(null);
        list.add("value");
        
        assertTrue("List should contain null", list.contains(null));
        assertEquals("List size should be 2", 2, list.size());
    }

    @Test
    public void listSublist_shouldReflectChanges() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        
        List<String> sublist = list.subList(1, 3);
        
        assertEquals("Sublist should have 2 elements", 2, sublist.size());
        assertEquals("Sublist should contain 'b'", "b", sublist.get(0));
        
        // Modifying sublist affects original
        sublist.add("new");
        assertEquals("Original list should reflect sublist change", 5, list.size());
    }

    @Test
    public void reversedList_shouldBeViewOnly() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        
        List<String> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        
        assertEquals("Reversed list first element", "c", reversed.get(0));
        assertEquals("Original first element", "a", list.get(0));
    }
}
