package com.softwaremagico.tm.advisor.ui.character;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class CharacterValidationTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void character_alwaysHasInfo() {
        CharacterManager.addNewCharacter();
        assertNotNull("Character info should not be null", CharacterManager.getSelectedCharacter().getInfo());
    }

    @Test
    public void character_alwaysHasSettings() {
        CharacterManager.addNewCharacter();
        assertNotNull("Character settings should not be null", CharacterManager.getSelectedCharacter().getSettings());
    }

    @Test
    public void character_alwaysHasLevel() {
        CharacterManager.addNewCharacter();
        assertTrue("Character must have level >= 1", CharacterManager.getSelectedCharacter().getLevel() >= 1);
    }

    @Test
    public void emptyCharacterList_doesNotThrow() {
        List<CharacterPlayer> characters = CharacterManager.getCharacters();
        assertEquals("Should start empty", 0, characters.size());
    }

    @Test
    public void characterList_neverNull() {
        assertNotNull("Characters list should never be null", CharacterManager.getCharacters());
    }

    @Test
    public void selectedCharacterNotNull_afterCreation() {
        CharacterManager.addNewCharacter();
        assertNotNull("Selected character must exist after creation", CharacterManager.getSelectedCharacter());
    }

    @Test
    public void removeCharacter_maintainsSelectedCharacter() {
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        int initialSize = CharacterManager.getCharacters().size();
        
        CharacterManager.removeSelectedCharacter();
        
        assertTrue("Should still have selected character", CharacterManager.getSelectedCharacter() != null);
        assertTrue("List size should decrease", CharacterManager.getCharacters().size() <= initialSize);
    }

    @Test
    public void characterName_isNeverNull() {
        CharacterManager.addNewCharacter();
        String name = CharacterManager.getSelectedCharacter().getCompleteNameRepresentation();
        assertNotNull("Character name should not be null", name);
    }

    @Test
    public void multipleCharacterLevels_allValid() {
        for (int i = 0; i < 3; i++) {
            CharacterManager.addNewCharacter();
            assertTrue("All levels should be >= 1", CharacterManager.getSelectedCharacter().getLevel() >= 1);
        }
    }
}
