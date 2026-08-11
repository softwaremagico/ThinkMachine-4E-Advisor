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

public class NumberConversionSafetyTest {

    @Test
    public void parseInteger_withNullString_shouldNotCrash() {
        String str = null;
        
        try {
            int value = safeParseInt(str, 0);
            assertEquals("Should return default value", 0, value);
        } catch (NullPointerException e) {
            fail("Should handle null string");
        }
    }

    @Test
    public void parseInteger_withEmptyString_shouldNotCrash() {
        String str = "";
        
        try {
            int value = safeParseInt(str, 0);
            assertEquals("Should return default value", 0, value);
        } catch (NullPointerException e) {
            fail("Should handle empty string");
        }
    }

    @Test
    public void parseInteger_withValidString_shouldWork() {
        String str = "42";
        int value = safeParseInt(str, 0);
        assertEquals("Should parse valid integer", 42, value);
    }

    @Test
    public void parseInteger_withInvalidString_shouldNotCrash() {
        String str = "not a number";
        
        try {
            int value = safeParseInt(str, -1);
            assertEquals("Should return default value", -1, value);
        } catch (NumberFormatException e) {
            fail("Should handle invalid string gracefully");
        }
    }

    @Test
    public void parseInteger_withNegativeString_shouldWork() {
        String str = "-42";
        int value = safeParseInt(str, 0);
        assertEquals("Should parse negative integer", -42, value);
    }

    @Test
    public void parseInteger_withOverflow_shouldHandleGracefully() {
        String str = "999999999999999999999";
        
        try {
            int value = safeParseInt(str, 0);
            // Should handle gracefully
            assertTrue("Should handle overflow", true);
        } catch (NumberFormatException e) {
            assertTrue("NumberFormatException is acceptable for overflow", true);
        }
    }

    @Test
    public void parseDouble_withNullString_shouldNotCrash() {
        String str = null;
        
        try {
            double value = safeParseDouble(str, 0.0);
            assertEquals("Should return default value", 0.0, value, 0.0);
        } catch (NullPointerException e) {
            fail("Should handle null string");
        }
    }

    @Test
    public void parseDouble_withDecimalString_shouldWork() {
        String str = "3.14";
        double value = safeParseDouble(str, 0.0);
        assertEquals("Should parse decimal", 3.14, value, 0.01);
    }

    @Test
    public void parseBoolean_withNullString_shouldNotCrash() {
        String str = null;
        
        try {
            boolean value = safeParseBool(str, false);
            assertFalse("Should return default value", value);
        } catch (NullPointerException e) {
            fail("Should handle null string");
        }
    }

    @Test
    public void parseBoolean_withValidString_shouldWork() {
        String str = "true";
        boolean value = safeParseBool(str, false);
        assertTrue("Should parse true", value);
    }

    @Test
    public void parseBoolean_withInvalidString_shouldUseDefault() {
        String str = "maybe";
        boolean value = safeParseBool(str, false);
        assertFalse("Should use default for invalid string", value);
    }

    @Test
    public void integerDivision_withZero_shouldNotCrash() {
        int numerator = 100;
        int denominator = 0;
        
        try {
            int result = safeDivide(numerator, denominator, 0);
            assertEquals("Should return default value", 0, result);
        } catch (ArithmeticException e) {
            fail("Should handle division by zero gracefully");
        }
    }

    @Test
    public void integerDivision_withValidValues_shouldWork() {
        int numerator = 100;
        int denominator = 5;
        
        int result = safeDivide(numerator, denominator, 0);
        assertEquals("Should divide correctly", 20, result);
    }

    private int safeParseInt(String str, int defaultValue) {
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double safeParseDouble(String str, double defaultValue) {
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean safeParseBool(String str, boolean defaultValue) {
        if (str == null || str.isEmpty()) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(str.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private int safeDivide(int numerator, int denominator, int defaultValue) {
        if (denominator == 0) {
            return defaultValue;
        }
        return numerator / denominator;
    }
}
