package com.softwaremagico.tm.advisor.ui.visualization.pdf;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PdfVisualizationFragmentTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
        CharacterManager.addNewCharacter();
    }

    @After
    public void tearDown() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void selectedCharacter_isNotNull() {
        assertNotNull(CharacterManager.getSelectedCharacter());
    }

    @Test
    public void selectedCharacter_hasValidLevel() {
        assertTrue(CharacterManager.getSelectedCharacter().getLevel() >= 1);
    }
}
