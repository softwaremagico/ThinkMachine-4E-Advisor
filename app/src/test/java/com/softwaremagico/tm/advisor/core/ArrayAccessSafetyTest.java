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

public class ArrayAccessSafetyTest {

    @Test
    public void arrayIndexOutOfBounds_shouldThrowException() {
        String[] arr = new String[3];
        arr[0] = "first";
        arr[1] = "second";
        arr[2] = "third";
        
        try {
            String value = arr[5];
            fail("Should throw ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Out of bounds access throws exception", true);
        }
    }

    @Test
    public void arrayNegativeIndex_shouldThrowException() {
        String[] arr = new String[3];
        
        try {
            String value = arr[-1];
            fail("Should throw ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Negative index throws exception", true);
        }
    }

    @Test
    public void nullArrayAccess_shouldThrowNullPointerException() {
        String[] arr = null;
        
        try {
            String value = arr[0];
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("Null array access throws NPE", true);
        }
    }

    @Test
    public void safeArrayAccess_withBoundsCheck() {
        String[] arr = new String[3];
        arr[0] = "first";
        arr[1] = "second";
        arr[2] = "third";
        
        String value = null;
        int index = 1;
        
        if (arr != null && index >= 0 && index < arr.length) {
            value = arr[index];
        }
        
        assertEquals("Should safely access element", "second", value);
    }

    @Test
    public void safeArrayAccess_withOutOfBoundsIndex() {
        String[] arr = new String[3];
        arr[0] = "first";
        
        String value = null;
        int index = 5;
        
        if (arr != null && index >= 0 && index < arr.length) {
            value = arr[index];
        }
        
        assertNull("Should return null for out of bounds", value);
    }

    @Test
    public void emptyArray_shouldNotCrash() {
        String[] arr = new String[0];
        
        assertEquals("Empty array length should be 0", 0, arr.length);
        
        String value = null;
        if (arr.length > 0) {
            value = arr[0];
        }
        
        assertNull("Empty array access returns null", value);
    }

    @Test
    public void arrayWithNullElements() {
        String[] arr = new String[3];
        arr[0] = null;
        arr[1] = "second";
        arr[2] = null;
        
        String value = arr[0];
        assertNull("Array can contain null elements", value);
    }

    @Test
    public void arrayIteration_shouldNotCrash() {
        String[] arr = new String[]{"one", null, "three"};
        
        int count = 0;
        for (String item : arr) {
            if (item != null) {
                assertTrue("Non-null item should work", true);
            }
            count++;
        }
        
        assertEquals("Should iterate all elements", 3, count);
    }

    @Test
    public void arrayRangeOutOfBounds_shouldThrowException() {
        String[] arr = new String[3];
        
        try {
            System.arraycopy(arr, 0, arr, 2, 5);  // Copy would go out of bounds
            fail("Should throw ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Array copy out of bounds throws exception", true);
        }
    }

    @Test
    public void primitiveArrayOutOfBounds_shouldThrowException() {
        int[] arr = new int[3];
        
        try {
            int value = arr[10];
            fail("Should throw ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Primitive array out of bounds throws exception", true);
        }
    }

    @Test
    public void multidimensionalArrayAccess_shouldBeChecked() {
        String[][] matrix = new String[3][];
        matrix[0] = new String[2];
        matrix[1] = null;  // Can have null rows
        matrix[2] = new String[3];
        
        // Need to check both dimensions
        String value = null;
        int row = 1;
        int col = 0;
        
        if (matrix != null && row < matrix.length && matrix[row] != null && col < matrix[row].length) {
            value = matrix[row][col];
        }
        
        assertNull("Should safely access multidimensional array", value);
    }

    @Test
    public void arrayCloning_shouldCreateIndependentCopy() {
        String[] original = new String[]{"one", "two", "three"};
        String[] clone = original.clone();
        
        clone[0] = "modified";
        
        assertEquals("Original should not change", "one", original[0]);
        assertEquals("Clone should change", "modified", clone[0]);
    }
}
