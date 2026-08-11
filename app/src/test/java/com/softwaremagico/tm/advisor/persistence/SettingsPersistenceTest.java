package com.softwaremagico.tm.advisor.persistence;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SettingsPersistenceTest {

    @Before
    public void setUp() {
        CharacterManager.getCharacters().clear();
    }

    @Test
    public void settings_alwaysInitialized() {
        CharacterManager.addNewCharacter();
        assertNotNull("Settings should be initialized", 
            CharacterManager.getSelectedCharacter().getSettings());
    }

    @Test
    public void settings_isConsistent() {
        CharacterManager.addNewCharacter();
        CharacterManager.addNewCharacter();
        
        for (int i = 0; i < CharacterManager.getCharacters().size(); i++) {
            assertNotNull("All characters should have settings",
                CharacterManager.getCharacters().get(i).getSettings());
        }
    }

    @Test
    public void settingsEntity_neverNull() {
        CharacterManager.addNewCharacter();
        
        // Access settings multiple times
        for (int i = 0; i < 5; i++) {
            assertNotNull("Settings should remain non-null",
                CharacterManager.getSelectedCharacter().getSettings());
        }
    }
}
