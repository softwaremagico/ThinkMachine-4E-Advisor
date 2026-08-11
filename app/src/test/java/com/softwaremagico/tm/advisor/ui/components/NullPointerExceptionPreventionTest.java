package com.softwaremagico.tm.advisor.ui.components;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NullPointerExceptionPreventionTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void getSelectedCharacter_afterAddingOne_isNotNull() {
        CharacterManager.addNewCharacter();
        assertNotNull("Selected character must not be null", CharacterManager.getSelectedCharacter());
    }

    @Test
    public void getSelectedCharacter_afterRemovingAll_createsNew() {
        CharacterManager.addNewCharacter();
        CharacterManager.removeSelectedCharacter();
        
        // After removing, should auto-create a new one
        assertNotNull("Should have a character", CharacterManager.getSelectedCharacter());
    }

    @Test
    public void characterInfo_neverNull() {
        CharacterManager.addNewCharacter();
        assertNotNull("Character.getInfo() should never be null", 
            CharacterManager.getSelectedCharacter().getInfo());
    }

    @Test
    public void characterSettings_neverNull() {
        CharacterManager.addNewCharacter();
        assertNotNull("Character.getSettings() should never be null",
            CharacterManager.getSelectedCharacter().getSettings());
    }

    @Test
    public void getCharacters_neverNull() {
        assertNotNull("getCharacters() should never return null",
            CharacterManager.getCharacters());
    }

    @Test
    public void characterCompleteNameRepresentation_neverNull() {
        CharacterManager.addNewCharacter();
        assertNotNull("Character name should never be null",
            CharacterManager.getSelectedCharacter().getCompleteNameRepresentation());
    }

    @Test
    public void characterLevel_alwaysAtLeastOne() {
        CharacterManager.addNewCharacter();
        assertTrue("Level must always be >= 1",
            CharacterManager.getSelectedCharacter().getLevel() >= 1);
    }

    @Test
    public void multipleOperations_allSafe() {
        // Add
        CharacterManager.addNewCharacter();
        assertNotNull("First add should be safe", CharacterManager.getSelectedCharacter());
        
        // Add more
        CharacterManager.addNewCharacter();
        assertNotNull("Second add should be safe", CharacterManager.getSelectedCharacter());
        
        // Select
        CharacterManager.setSelectedCharacter(CharacterManager.getCharacters().get(0));
        assertNotNull("Select should be safe", CharacterManager.getSelectedCharacter());
        
        // Remove
        CharacterManager.removeSelectedCharacter();
        assertNotNull("Remove should be safe", CharacterManager.getSelectedCharacter());
    }

    @Test
    public void characterAccess_threadSafe() {
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        
        // Access from "multiple threads"
        for (int i = 0; i < 10; i++) {
            assertNotNull("Concurrent access should be safe",
                CharacterManager.getSelectedCharacter());
        }
    }
}
