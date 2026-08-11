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

public class StringValidationSafetyTest {

    @Test
    public void nullString_toLengthCheck_shouldNotCrash() {
        String str = null;
        
        try {
            boolean isEmpty = str == null || str.isEmpty();
            assertTrue("Null check should work", isEmpty);
        } catch (NullPointerException e) {
            fail("Should check null before calling isEmpty()");
        }
    }

    @Test
    public void emptyString_shouldBeDetected() {
        String str = "";
        assertTrue("Empty string should be detected", str.isEmpty());
    }

    @Test
    public void whitespaceOnlyString_shouldBeTrimmed() {
        String str = "   ";
        assertTrue("Whitespace should be trimmed to empty", str.trim().isEmpty());
    }

    @Test
    public void stringWithNull_shouldBeHandled() {
        String str = "null";
        String result = processString(str);
        assertNotNull("Should handle string 'null'", result);
    }

    @Test
    public void stringComparison_withNull_shouldNotCrash() {
        String str = null;
        String literal = "test";
        
        // Safe comparison
        boolean equals = literal.equals(str);
        assertFalse("Should not equal", equals);
    }

    @Test
    public void stringComparison_withLiteralFirst_shouldNotCrash() {
        String str = null;
        String literal = "test";
        
        // Safe comparison with literal first
        boolean equals = "test".equals(str);
        assertFalse("Should not equal", equals);
    }

    @Test
    public void stringTrim_onNullString_shouldCrash() {
        String str = null;
        
        try {
            String trimmed = str.trim();
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("Null string trim should throw NPE", true);
        }
    }

    @Test
    public void safeTrimming_checkNullFirst() {
        String str = null;
        String trimmed = (str != null) ? str.trim() : "";
        assertEquals("Should return empty string for null", "", trimmed);
    }

    @Test
    public void stringConcatenation_withNull_shouldNotCrash() {
        String str = null;
        String result = "Value: " + str;
        assertEquals("Should convert null to string", "Value: null", result);
    }

    @Test
    public void stringFormat_withNull_shouldNotCrash() {
        String str = null;
        String result = String.format("Value: %s", str);
        assertEquals("Should format null", "Value: null", result);
    }

    @Test
    public void stringArray_withNullElements() {
        String[] arr = new String[3];
        arr[0] = "first";
        arr[1] = null;
        arr[2] = "third";
        
        for (String item : arr) {
            if (item != null && item.isEmpty()) {
                fail("Should not reach here");
            }
        }
        assertTrue("Should iterate safely", true);
    }

    private String processString(String input) {
        if (input == null || input.isEmpty()) {
            return "default";
        }
        if ("null".equals(input)) {
            return "literal null";
        }
        return input;
    }
}
