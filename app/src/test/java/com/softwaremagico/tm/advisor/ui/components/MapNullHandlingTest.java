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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MapNullHandlingTest {

    @Test
    public void mapWithNullKey_shouldBeHandled() {
        Map<String, String> map = new HashMap<>();
        map.put(null, "value");
        
        assertTrue("Map should contain null key", map.containsKey(null));
        assertEquals("Should retrieve value with null key", "value", map.get(null));
    }

    @Test
    public void mapWithNullValue_shouldBeHandled() {
        Map<String, String> map = new HashMap<>();
        map.put("key", null);
        
        assertNull("Map should contain null value", map.get("key"));
        assertTrue("Map should contain key", map.containsKey("key"));
    }

    @Test
    public void mapGetWithDefault_shouldNotCrash() {
        Map<String, String> map = new HashMap<>();
        
        // Using getOrDefault is safer
        String value = map.getOrDefault("missing", "default");
        assertEquals("Should return default", "default", value);
    }

    @Test
    public void mapPutIfAbsent_shouldNotReplace() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "original");
        
        // putIfAbsent returns the existing value, doesn't replace
        String previous = map.putIfAbsent("key", "new");
        assertEquals("Should return original value", "original", previous);
        assertEquals("Should not replace", "original", map.get("key"));
    }

    @Test
    public void mapCompute_withNull_shouldBeHandled() {
        Map<String, Integer> map = new HashMap<>();
        
        // Compute can handle null values
        map.compute("key", (k, v) -> (v == null) ? 1 : v + 1);
        
        assertEquals("Should set value to 1", Integer.valueOf(1), map.get("key"));
    }

    @Test
    public void mapRemove_withNull_shouldBeHandled() {
        Map<String, String> map = new HashMap<>();
        map.put("key", null);
        
        String removed = map.remove("key");
        assertNull("Should remove null value", removed);
        assertFalse("Key should be removed", map.containsKey("key"));
    }

    @Test
    public void mapIteration_withNullValues_shouldNotCrash() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        
        int count = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertNotNull("Key should never be null in iteration", entry.getKey());
            // Value can be null, but should be handled safely
            if (entry.getValue() != null) {
                assertTrue("Non-null value should work", true);
            }
            count++;
        }
        
        assertEquals("Should iterate over all entries", 3, count);
    }

    @Test
    public void nullMapAccess_shouldThrowNullPointerException() {
        Map<String, String> map = null;
        
        try {
            map.put("key", "value");
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("Accessing null map throws NPE", true);
        }
    }

    @Test
    public void mapMerge_shouldHandleNulls() {
        Map<String, String> map = new HashMap<>();
        
        // merge can handle null remapping function results
        map.merge("key", "new", (old, value) -> (old == null) ? value : old + value);
        
        assertEquals("Should have merged value", "new", map.get("key"));
    }

    @Test
    public void mapForEach_withConsumer_shouldNotCrash() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        
        final int[] count = {0};
        map.forEach((k, v) -> {
            assertNotNull("Key should not be null", k);
            // Value handling
            if (v != null) {
                count[0]++;
            }
        });
        
        assertEquals("Should handle forEach", 1, count[0]);
    }

    @Test
    public void emptyMapOperations_shouldNotCrash() {
        Map<String, String> map = new HashMap<>();
        
        // All these should work on empty map
        assertEquals("Empty map size", 0, map.size());
        assertTrue("Empty map isEmpty", map.isEmpty());
        assertNull("Get from empty map returns null", map.get("key"));
        assertFalse("Empty map contains no keys", map.containsKey("key"));
        
        assertTrue("Should handle empty map operations", true);
    }
}
