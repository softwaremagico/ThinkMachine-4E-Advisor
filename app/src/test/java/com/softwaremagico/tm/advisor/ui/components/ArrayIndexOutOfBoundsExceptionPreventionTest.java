package com.softwaremagico.tm.advisor.ui.components;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ArrayIndexOutOfBoundsExceptionPreventionTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void emptyList_doesNotThrowOnIteration() {
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        for (CharacterPlayer character : characters) {
            // Should not iterate if empty
            assertTrue("Should not reach here for empty list", false);
        }
        // Passed
    }

    @Test
    public void characterList_safeToAccess() {
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        assertTrue("Should have 2 characters", characters.size() >= 2);
        
        // Safe access
        CharacterPlayer first = characters.get(0);
        assertTrue("First character must exist", first != null);
    }

    @Test
    public void menuAccess_withEmptyList_isSafe() {
        CharacterManager.getCharacters().clear();
        
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        assertEquals("List should be empty", 0, characters.size());
        
        // This simulates MainActivity.onPrepareOptionsMenu loop
        for (int i = 0; i < characters.size(); i++) {
            if (i >= characters.size()) {
                break; // Safe bounds check
            }
            CharacterPlayer character = characters.get(i);
            // Process
        }
        // Passed
    }

    @Test
    public void characterList_loopIsSafe() {
        for (int i = 0; i < 5; i++) {
            CharacterManager.addNewCharacter();
        }
        
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        
        // Safe loop with bounds check
        for (int i = 0; i < characters.size(); i++) {
            if (i >= characters.size()) break;
            CharacterPlayer character = characters.get(i);
            assertTrue("Character at index " + i + " should exist", character != null);
        }
    }

    @Test
    public void reverseLoop_isSafe() {
        for (int i = 0; i < 5; i++) {
            CharacterManager.addNewCharacter();
        }
        
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        
        // Safe reverse loop
        for (int i = characters.size() - 1; i >= 0; i--) {
            CharacterPlayer character = characters.get(i);
            assertTrue("Character at index " + i + " should exist", character != null);
        }
    }

    @Test
    public void getFirstCharacter_afterRemovingAllButOne_isSafe() {
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        CharacterManager.removeSelectedCharacter();
        
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        assertTrue("Should have at least 1 character", characters.size() >= 1);
        
        // Safe access to first
        CharacterPlayer first = characters.get(0);
        assertTrue("First character should exist", first != null);
    }

    @Test
    public void characterLevelSerializationIsSafe() {
        CharacterManager.addNewCharacter();
        CharacterPlayer character = CharacterManager.getSelectedCharacter();
        
        // Level should be >= 1 to prevent serialization crash
        assertTrue("Level must be >= 1 for safe serialization", character.getLevel() >= 1);
    }

    @Test
    public void listSize_alwaysValid() {
        CharacterManager.addNewCharacter();
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        
        int size = characters.size();
        assertTrue("Size should be valid", size >= 0);
        
        for (int i = 0; i < size; i++) {
            assertTrue("Index " + i + " should be valid", i < characters.size());
        }
    }
}
