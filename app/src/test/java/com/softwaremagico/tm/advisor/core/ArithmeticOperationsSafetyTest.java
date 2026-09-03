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

public class ArithmeticOperationsSafetyTest {

    @Test
    public void divisionByZero_shouldThrowException() {
        try {
            int result = 10 / 0;
            fail("Should throw ArithmeticException");
        } catch (ArithmeticException e) {
            assertTrue("Division by zero throws exception", true);
        }
    }

    @Test
    public void doubleDivisionByZero_shouldReturnInfinity() {
        double result = 10.0 / 0.0;
        assertTrue("Double division by zero returns Infinity", Double.isInfinite(result));
    }

    @Test
    public void integerOverflow_shouldWrap() {
        int max = Integer.MAX_VALUE;
        int result = max + 1;
        
        // Java integers overflow silently to negative
        assertTrue("Integer overflow wraps to negative", result < 0);
    }

    @Test
    public void longArithmetic_shouldHandleLargeValues() {
        long large1 = Long.MAX_VALUE - 1;
        long large2 = Long.MAX_VALUE - 1;
        
        // This will overflow silently
        long result = large1 + large2;
        
        assertTrue("Large long addition overflows", result < 0);
    }

    @Test
    public void modulo_byZero_shouldThrowException() {
        try {
            int result = 10 % 0;
            fail("Should throw ArithmeticException");
        } catch (ArithmeticException e) {
            assertTrue("Modulo by zero throws exception", true);
        }
    }

    @Test
    public void negativeModulo_shouldBeNegative() {
        int result = -10 % 3;
        assertTrue("Negative modulo result is negative", result < 0);
    }

    @Test
    public void floatingPointPrecision_shouldBeLimited() {
        double a = 0.1;
        double b = 0.2;
        double c = 0.3;
        
        double sum = a + b;
        
        assertFalse("Floating point sum may not equal third value", sum == c);
        assertTrue("But should be very close", Math.abs(sum - c) < 0.0001);
    }

    @Test
    public void negativeSquareRoot_shouldReturnNaN() {
        double result = Math.sqrt(-1);
        assertTrue("Square root of negative returns NaN", Double.isNaN(result));
    }

    @Test
    public void logarithmOfZero_shouldReturnNegativeInfinity() {
        double result = Math.log(0);
        assertTrue("Log of zero returns negative infinity", Double.isInfinite(result) && result < 0);
    }

    @Test
    public void integerCastingFromDouble_shouldTruncate() {
        double d = 3.9;
        int i = (int) d;
        
        assertEquals("Casting double to int truncates", 3, i);
    }

    @Test
    public void divisionWithIntegersRoundingDown() {
        int result = 10 / 3;
        assertEquals("Integer division rounds down", 3, result);
    }

    @Test
    public void floatingPointComparison_shouldUseEpsilon() {
        double a = 0.1 + 0.2;
        double b = 0.3;
        
        boolean equals = Math.abs(a - b) < 0.0001;
        assertTrue("Should use epsilon for float comparison", equals);
    }

    @Test
    public void nanComparison_shouldFail() {
        double nan = Double.NaN;
        
        assertFalse("NaN is not equal to itself", nan == nan);
        assertFalse("NaN is not equal to 0", nan == 0);
        assertTrue("Should use isNaN for NaN check", Double.isNaN(nan));
    }

    @Test
    public void infinityComparison_shouldWork() {
        double inf = Double.POSITIVE_INFINITY;
        
        assertTrue("Infinity is equal to itself", inf == inf);
        assertFalse("Infinity is not equal to MAX_VALUE", inf == Double.MAX_VALUE);
    }

    @Test
    public void bitOperations_shouldNotCrash() {
        int a = 5;    // 0101
        int b = 3;    // 0011
        
        int and = a & b;  // 0001 = 1
        int or = a | b;   // 0111 = 7
        int xor = a ^ b;  // 0110 = 6
        
        assertEquals("Bitwise AND", 1, and);
        assertEquals("Bitwise OR", 7, or);
        assertEquals("Bitwise XOR", 6, xor);
    }

    @Test
    public void leftShift_shouldMultiply() {
        int value = 5;
        int shifted = value << 2;  // Multiply by 2^2 = 4
        
        assertEquals("Left shift multiplies", 20, shifted);
    }

    @Test
    public void rightShift_shouldDivide() {
        int value = 20;
        int shifted = value >> 2;  // Divide by 2^2 = 4
        
        assertEquals("Right shift divides", 5, shifted);
    }
}
