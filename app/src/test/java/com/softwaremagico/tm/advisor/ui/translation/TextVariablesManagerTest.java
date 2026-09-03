package com.softwaremagico.tm.advisor.ui.translation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Before;
import org.junit.Test;

public class TextVariablesManagerTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void replace_withNullInput_returnsEmptyString() {
        assertEquals("", TextVariablesManager.replace(null));
    }

    @Test
    public void replace_withNullCharacterName_doesNotThrowAndReplacesWithEmptyString() {
        final CharacterPlayer characterWithNullName = new CharacterPlayer() {
            @Override
            public String getCompleteNameRepresentation() {
                return null;
            }
        };
        CharacterManager.setSelectedCharacter(characterWithNullName);

        final String result = TextVariablesManager.replace("Name: ${CHARACTER_NAME}");

        assertNotNull(result);
        assertEquals("Name: ", result);
    }
}
