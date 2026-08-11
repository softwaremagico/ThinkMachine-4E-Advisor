package com.softwaremagico.tm.advisor.persistence;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SettingsHandlerTest {

    @Test
    public void updateCharacterSettings_withoutLoadedSettings_initializesDefaults() {
        SettingsHandler.setSettingsEntity((SettingsEntity) null);

        SettingsHandler.updateCharacterSettings();

        assertNotNull(SettingsHandler.getSettingsEntity());
        assertNotNull(CharacterManager.getSelectedCharacter().getSettings());
    }
}
