package com.softwaremagico.tm.advisor.ui.components;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterManagerEdgeCasesTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void addCharacter_levelIsAtLeastOne() {
        CharacterManager.addNewCharacter();
        assertTrue("Character level must be >= 1", CharacterManager.getSelectedCharacter().getLevel() >= 1);
    }

    @Test
    public void addCharacter_thenRemoveLevel_staysAtOne() {
        CharacterManager.addNewCharacter();
        CharacterManager.removeCharacterLevel();
        assertTrue("Character level must stay >= 1", CharacterManager.getSelectedCharacter().getLevel() >= 1);
    }

    @Test
    public void addMultipleCharacters_allHaveValidLevel() {
        for (int i = 0; i < 5; i++) {
            CharacterManager.addNewCharacter();
        }
        
        for (CharacterPlayer character : CharacterManager.getCharacters()) {
            assertNotNull("Character should not be null", character);
            assertTrue("Character level must be >= 1", character.getLevel() >= 1);
        }
    }

    @Test
    public void selectedCharacter_isFromList() {
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        
        CharacterPlayer selected = CharacterManager.getSelectedCharacter();
        assertTrue("Selected character should be in list", CharacterManager.getCharacters().contains(selected));
    }

    @Test
    public void getCharactersNotNull() {
        assertNotNull("Characters list should never be null", CharacterManager.getCharacters());
    }

    @Test
    public void getSelectedCharacterNotNull_afterAddingCharacter() {
        CharacterManager.addNewCharacter();
        assertNotNull("Selected character should not be null", CharacterManager.getSelectedCharacter());
    }
}
