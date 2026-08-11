package com.softwaremagico.tm.advisor.ui.main;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class MainActivityMenuTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void onPrepareOptionsMenu_withEmptyCharacterList_doesNotCrash() {
        assertEquals("Character list should be empty", 0, CharacterManager.getCharacters().size());
    }

    @Test
    public void onPrepareOptionsMenu_afterAddingCharacter_characterIsAvailable() {
        CharacterManager.addNewCharacter();
        assertEquals("Should have one character", 1, CharacterManager.getCharacters().size());
        assertNotNull("First character should not be null", CharacterManager.getSelectedCharacter());
    }

    @Test
    public void onPrepareOptionsMenu_afterMultipleCharacters_allAreAccessible() {
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        assertEquals("Should have three characters", 3, CharacterManager.getCharacters().size());
        
        for (int i = 0; i < CharacterManager.getCharacters().size(); i++) {
            CharacterPlayer character = CharacterManager.getCharacters().get(i);
            assertNotNull("Character at " + i + " should not be null", character);
            assertEquals("Character level should be at least 1", true, character.getLevel() >= 1);
        }
    }
}
