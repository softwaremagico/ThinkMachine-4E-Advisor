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

public class EnumSafetyTest {

    enum Status {
        ACTIVE("active"),
        INACTIVE("inactive"),
        PENDING("pending");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Status fromValue(String value) {
            if (value == null) {
                return null;
            }
            for (Status status : values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @Test
    public void enumNullComparison_shouldBeSafe() {
        Status status = null;
        
        if (status == null) {
            assertTrue("Enum null check should work", true);
        }
    }

    @Test
    public void enumNullValueOf_shouldThrowException() {
        try {
            Status status = Status.valueOf(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("valueOf(null) throws NPE", true);
        }
    }

    @Test
    public void enumInvalidValue_shouldThrowException() {
        try {
            Status status = Status.valueOf("INVALID");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue("Invalid enum value throws exception", true);
        }
    }

    @Test
    public void enumFromValue_withNull_shouldReturnNull() {
        Status status = Status.fromValue(null);
        assertNull("Should return null for null input", status);
    }

    @Test
    public void enumFromValue_withValidValue_shouldWork() {
        Status status = Status.fromValue("active");
        assertEquals("Should find enum", Status.ACTIVE, status);
    }

    @Test
    public void enumFromValue_withInvalidValue_shouldReturnNull() {
        Status status = Status.fromValue("unknown");
        assertNull("Should return null for invalid value", status);
    }

    @Test
    public void enumIteration_shouldNotCrash() {
        int count = 0;
        for (Status status : Status.values()) {
            assertNotNull("Enum value should not be null", status);
            count++;
        }
        
        assertEquals("Should iterate all values", 3, count);
    }

    @Test
    public void enumSwitchStatement_shouldNotCrash() {
        Status status = Status.ACTIVE;
        
        String message = null;
        switch (status) {
            case ACTIVE:
                message = "Active";
                break;
            case INACTIVE:
                message = "Inactive";
                break;
            case PENDING:
                message = "Pending";
                break;
        }
        
        assertEquals("Switch should work", "Active", message);
    }

    @Test
    public void enumNullInSwitch_shouldHandleSafely() {
        Status status = null;
        
        String message = "Unknown";
        if (status != null) {
            switch (status) {
                case ACTIVE:
                    message = "Active";
                    break;
                default:
                    message = "Other";
            }
        }
        
        assertEquals("Should handle null safely", "Unknown", message);
    }

    @Test
    public void enumComparison_shouldWork() {
        Status s1 = Status.ACTIVE;
        Status s2 = Status.ACTIVE;
        Status s3 = Status.INACTIVE;
        
        assertTrue("Same enum values should equal", s1 == s2);
        assertFalse("Different enum values should not equal", s1 == s3);
    }

    @Test
    public void enumOrdinal_shouldBeConsistent() {
        assertEquals("ACTIVE ordinal", 0, Status.ACTIVE.ordinal());
        assertEquals("INACTIVE ordinal", 1, Status.INACTIVE.ordinal());
        assertEquals("PENDING ordinal", 2, Status.PENDING.ordinal());
    }

    @Test
    public void enumInMap_shouldWork() {
        java.util.Map<Status, String> map = new java.util.HashMap<>();
        map.put(Status.ACTIVE, "System is active");
        map.put(Status.INACTIVE, "System is inactive");
        
        String message = map.get(Status.ACTIVE);
        assertEquals("Should retrieve enum key value", "System is active", message);
    }

    @Test
    public void enumNameMethod_shouldReturnName() {
        Status status = Status.ACTIVE;
        assertEquals("name() should return enum name", "ACTIVE", status.name());
    }
}
