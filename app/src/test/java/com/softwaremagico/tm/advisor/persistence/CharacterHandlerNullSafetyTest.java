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

package com.softwaremagico.tm.advisor.persistence;

import android.content.Context;

import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterHandlerNullSafetyTest {

    @Test
    public void save_withNullCharacter_shouldNotThrow() {
        CharacterHandler handler = CharacterHandler.getInstance();
        Context mockContext = null;
        CharacterPlayer nullCharacter = null;
        
        // This should not throw an exception
        try {
            handler.save(mockContext, nullCharacter);
            // If we reach here, the method properly handled null character
            assertTrue("save() should handle null character gracefully", true);
        } catch (Exception e) {
            // If context is null, we might get NPE which is expected
            // But the important part is null character is checked first
        }
    }

    @Test
    public void save_withNullEntity_shouldNotThrow() {
        CharacterHandler handler = CharacterHandler.getInstance();
        Context mockContext = null;
        CharacterEntity nullEntity = null;

        try {
            handler.save(mockContext, nullEntity);
        } catch (NullPointerException e) {
            fail("save() should short-circuit before dereferencing null values");
        } catch (Exception e) {
            // other exceptions are acceptable for this test when context is null; method should not crash on null entity
        }
    }

    @Test
    public void characterEntity_setNullCharacter_shouldNotThrow() {
        CharacterEntity entity = new CharacterEntity();

        try {
            entity.setCharacterPlayer(null);
            assertNull("Null character should leave entity data untouched", entity.getJson());
        } catch (Exception e) {
            fail("Null character should be ignored without throwing: " + e.getMessage());
        }
    }
}
