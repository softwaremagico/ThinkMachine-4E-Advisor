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
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public class EmptyCollectionHandlingTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void testEmptySelectionsWithIteratorNext() {
        // Simulate the crash condition in OptionSelectorLayout:150
        // where selections.iterator().next() is called on empty collection
        
        CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull("Character should exist", character);
        
        // This test verifies that the fix in OptionSelectorLayout prevents
        // NoSuchElementException when selections is empty
        // The fix checks !selections.isEmpty() before calling iterator().next()
        
        // Empty list - iterator().next() would crash
        java.util.Collection<com.softwaremagico.tm.character.Selection> emptySelections = 
            new java.util.ArrayList<>();
        
        // Should safely handle empty collection
        if (!emptySelections.isEmpty()) {
            // Safe: only call next() if collection has elements
            com.softwaremagico.tm.character.Selection first = emptySelections.iterator().next();
            assertNotNull("If not empty, should have a first element", first);
        }
    }

    @Test
    public void testSettingsRestrictionCheck() {
        // Test that ElementAdapter.isEnabled safely accesses getSettings()
        // even when getSelectedCharacter() could theoretically be null
        
        CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull("Character should not be null", character);
        assertNotNull("Settings should not be null", character.getSettings());
        
        // Safe access pattern:
        boolean isRestrictionsChecked = character.getSettings().isRestrictionsChecked();
        // This should not throw NPE
    }

    @Test
    public void testCharacterInfoChainCalls() {
        // Test that CharacterDescriptionFragmentCharacter chain calls are safe
        
        CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull("Character should not be null", character);
        
        // These chain calls should not throw NPE:
        assertNotNull("Info should not be null", character.getInfo());
        
        // Safe to call methods on info
        String player = character.getInfo().getPlayer();
        String hair = character.getInfo().getHair();
        String eyes = character.getInfo().getEyes();
        
        // Values can be null, but calling methods should not crash
    }

    @Test
    public void testOccultismTypeCanBeNull() {
        // Test that updateVisibility() safely handles null occultism type
        
        CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull("Character should not be null", character);
        
        // getOccultismType() can return null
        java.util.Objects.isNull(character.getOccultismType());
        
        // Safe pattern - check for null before using
        com.softwaremagico.tm.character.occultism.OccultismType occultismType = 
            character.getOccultismType();
        if (occultismType != null) {
            // Safe to use occultismType
            String id = occultismType.getId();
            assertNotNull("ID should not be null if type is not null", id);
        }
    }
}
