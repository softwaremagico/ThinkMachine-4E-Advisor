package com.softwaremagico.tm.advisor.ui.session;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidJsonException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class CharacterManagerTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void addNewCharacter_setsMinimumLevelToOne() {
        CharacterManager.addNewCharacter();

        assertEquals(1, CharacterManager.getSelectedCharacter().getLevel());
    }

    @Test
    public void setSelectedCharacter_withZeroLevelNormalizesToOne() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        CharacterManager.setSelectedCharacter(characterPlayer);

        assertEquals(1, CharacterManager.getSelectedCharacter().getLevel());
    }

    @Test
    public void setCharacterLevel_belowOne_keepsMinimumLevelOne() {
        CharacterManager.addNewCharacter();

        CharacterManager.setCharacterLevel(0);

        assertEquals(1, CharacterManager.getSelectedCharacter().getLevel());
    }

    @Test
    public void setSelectedCharacter_withNull_keepsSelectedCharacterValid() {
        CharacterManager.addNewCharacter();

        CharacterManager.setSelectedCharacter(null);

        assertNotNull(CharacterManager.getSelectedCharacter());
    }

    @Test
    public void getSelectedCharacter_recoversWhenSelectedCharacterWasCleared() {
        CharacterManager.getCharacters().add(null);
        CharacterManager.getCharacters().add(new CharacterPlayer());

        CharacterManager.setSelectedCharacter(null);

        assertNotNull(CharacterManager.getSelectedCharacter());
    }

    // --- Clone tests ---

    @Test
    public void cloneSelectedCharacter_returnsNonNullCharacter() throws InvalidJsonException {
        CharacterManager.addNewCharacter();

        final CharacterPlayer clone = CharacterManager.cloneSelectedCharacter();

        assertNotNull(clone);
    }

    @Test
    public void cloneSelectedCharacter_returnsDifferentInstance() throws InvalidJsonException {
        CharacterManager.addNewCharacter();
        final CharacterPlayer original = CharacterManager.getSelectedCharacter();

        final CharacterPlayer clone = CharacterManager.cloneSelectedCharacter();

        assertNotSame(original, clone);
    }

    @Test
    public void cloneSelectedCharacter_setsCloneAsSelectedCharacter() throws InvalidJsonException {
        CharacterManager.addNewCharacter();

        final CharacterPlayer clone = CharacterManager.cloneSelectedCharacter();

        assertSame(clone, CharacterManager.getSelectedCharacter());
    }

    @Test
    public void cloneSelectedCharacter_addsCloneToCharacterList() throws InvalidJsonException {
        CharacterManager.addNewCharacter();
        final int sizeBeforeClone = CharacterManager.getCharacters().size();

        CharacterManager.cloneSelectedCharacter();

        assertEquals(sizeBeforeClone + 1, CharacterManager.getCharacters().size());
    }

    @Test
    public void cloneSelectedCharacter_preservesLevel() throws InvalidJsonException {
        CharacterManager.addNewCharacter();
        final int originalLevel = CharacterManager.getSelectedCharacter().getLevel();

        final CharacterPlayer clone = CharacterManager.cloneSelectedCharacter();

        assertEquals(originalLevel, clone.getLevel());
    }

    @Test
    public void cloneSelectedCharacter_preservesPlayerInfo() throws InvalidJsonException {
        CharacterManager.addNewCharacter();
        CharacterManager.getSelectedCharacter().getInfo().setPlayer("TestPlayer");

        final CharacterPlayer clone = CharacterManager.cloneSelectedCharacter();

        assertEquals("TestPlayer", clone.getInfo().getPlayer());
    }

    @Test
    public void cloneSelectedCharacter_originalRemainsInCharacterList() throws InvalidJsonException {
        CharacterManager.addNewCharacter();
        final CharacterPlayer original = CharacterManager.getSelectedCharacter();

        CharacterManager.cloneSelectedCharacter();

        assertNotNull("Original character should still be in the list",
                CharacterManager.getCharacters().stream()
                        .filter(c -> c == original)
                        .findFirst()
                        .orElse(null));
    }

    @Test
    public void cloneSelectedCharacter_cloneIsIndependentFromOriginal() throws InvalidJsonException {
        CharacterManager.addNewCharacter();
        CharacterManager.getSelectedCharacter().getInfo().setPlayer("Original");
        final CharacterPlayer original = CharacterManager.getSelectedCharacter();

        final CharacterPlayer clone = CharacterManager.cloneSelectedCharacter();
        clone.getInfo().setPlayer("Clone");

        assertEquals("Original", original.getInfo().getPlayer());
        assertEquals("Clone", clone.getInfo().getPlayer());
    }
}
