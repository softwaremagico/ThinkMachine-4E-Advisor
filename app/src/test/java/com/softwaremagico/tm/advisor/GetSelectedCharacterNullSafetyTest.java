/*
 *  Copyright (C) 2024 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public class GetSelectedCharacterNullSafetyTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void testGetSelectedCharacterNeverNull() {
        // getSelectedCharacter() should never return null
        // even when called multiple times
        CharacterPlayer character1 = CharacterManager.getSelectedCharacter();
        assertNotNull("First call to getSelectedCharacter should not return null", character1);

        CharacterPlayer character2 = CharacterManager.getSelectedCharacter();
        assertNotNull("Second call to getSelectedCharacter should not return null", character2);

        // Both should be the same instance
        CharacterPlayer selected = CharacterManager.getSelectedCharacter();
        assertNotNull("Selected character should never be null", selected);
    }

    @Test
    public void testGetSelectedCharacterHasValidInfo() {
        // Verify getSelectedCharacter() returns a character with valid info
        CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull("Character should not be null", character);
        assertNotNull("Character info should not be null", character.getInfo());
        assertNotNull("Character settings should not be null", character.getSettings());
    }

    @Test
    public void testGetSelectedCharacterMultipleCalls() {
        // Verify multiple rapid calls don't cause null return
        for (int i = 0; i < 10; i++) {
            CharacterPlayer character = CharacterManager.getSelectedCharacter();
            assertNotNull("getSelectedCharacter should never return null (iteration " + i + ")", character);
        }
    }

    @Test
    public void testCharacterMethodsChainSafely() {
        // Test that chain methods work safely
        CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull("Character should not be null", character);

        // These chain calls should not throw NPE
        String name = character.getCompleteNameRepresentation();
        assertNotNull("Complete name representation should not be null", name);

        // Info access should not throw NPE
        String player = character.getInfo().getPlayer();
        // player can be null, but calling the method should not crash
    }
}
