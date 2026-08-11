package com.softwaremagico.tm.advisor.ui.character.upbringing;

import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class UpbringingFragmentCharacterTest {

    @Test
    public void setRaisedInSpace_withoutUpbringing_returnsFalse() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        final boolean updated = UpbringingFragmentCharacter.setRaisedInSpace(characterPlayer, true);

        assertFalse(updated);
    }
}
