package com.softwaremagico.tm.advisor.ui.session;

import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharacterManagerTest {

    @Test
    public void addNewCharacter_setsMinimumLevelToOne() {
        CharacterManager.getCharacters().clear();

        CharacterManager.addNewCharacter();

        assertEquals(1, CharacterManager.getSelectedCharacter().getLevel());
    }

    @Test
    public void setSelectedCharacter_withZeroLevelNormalizesToOne() {
        CharacterManager.getCharacters().clear();
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        CharacterManager.setSelectedCharacter(characterPlayer);

        assertEquals(1, CharacterManager.getSelectedCharacter().getLevel());
    }

    @Test
    public void setCharacterLevel_belowOne_keepsMinimumLevelOne() {
        CharacterManager.getCharacters().clear();
        CharacterManager.addNewCharacter();

        CharacterManager.setCharacterLevel(0);

        assertEquals(1, CharacterManager.getSelectedCharacter().getLevel());
    }
}
