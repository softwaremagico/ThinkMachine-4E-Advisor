/*
 *  Copyright (C) 2024 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.character.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.persistence.SettingsHandler;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialCharacterException;

public class CharacterSettingsFragmentCharacter extends CharacterCustomFragment {
    private View root;
    private SwitchCompat nonOfficialEnabled;
    private SwitchCompat restrictionsIgnored;
    private SwitchCompat playerGuideModule;
    private SwitchCompat factionBookModule;
    private SwitchCompat revisedEditionModule;
    private SwitchCompat lostWorldsBookModule;
    private SwitchCompat imperialDossierBrotherBattleModule;
    private SwitchCompat imperialDossierCharioteersGuildModule;
    private SwitchCompat imperialDossierHouseHawkwoodModule;
    private SwitchCompat imperialDossierReevesGuildModule;
    private SwitchCompat vuldrokSpaceModule;
    private boolean updatingSettings = false;

    public static CharacterSettingsFragmentCharacter newInstance(int index) {
        final CharacterSettingsFragmentCharacter fragment = new CharacterSettingsFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        if (getContext() == null) {
            return;
        }
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null) {
            return;
        }
        populateElements(root, selectedCharacter);

        nonOfficialEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (updatingSettings) {
                return;
            }
            if (getContext() == null) {
                return;
            }
            try {
                if (!isChecked) {
                    final CharacterPlayer currentCharacter = CharacterManager.getSelectedCharacter();
                    if (currentCharacter == null) {
                        return;
                    }
                    currentCharacter.checkIsOfficial();
                }
                SettingsHandler.getSettingsEntity().setOnlyOfficialAllowed(!isChecked);
                SettingsHandler.save(getContext());
                SettingsHandler.updateCharacterSettings();
            } catch (UnofficialCharacterException e) {
                SnackbarGenerator.getErrorMessage(root, R.string.message_setting_unofficial_not_changed).show();
                setSwitchValue(nonOfficialEnabled, true);
            }
        });

        restrictionsIgnored.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (updatingSettings) {
                return;
            }
            if (getContext() == null) {
                return;
            }
            try {
                if (!isChecked) {
                    final CharacterPlayer currentCharacter = CharacterManager.getSelectedCharacter();
                    if (currentCharacter == null) {
                        return;
                    }
                    currentCharacter.checkIsNotRestricted();
                }
                SettingsHandler.getSettingsEntity().setRestrictionsChecked(!isChecked);
                SettingsHandler.save(getContext());
                SettingsHandler.updateCharacterSettings();
            } catch (RestrictedElementException e) {
                SnackbarGenerator.getErrorMessage(root, R.string.message_setting_restriction_not_changed).show();
                setSwitchValue(restrictionsIgnored, true);
            }
        });

        playerGuideModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setPlayerGuideEnabled(isChecked)));
        factionBookModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setFactionsBookEnabled(isChecked)));
        revisedEditionModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setRevisedEditionEnabled(isChecked)));
        lostWorldsBookModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setLostWorldsBookEnabled(isChecked)));
        imperialDossierBrotherBattleModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setImperialDossierBrotherBattleEnabled(isChecked)));
        imperialDossierCharioteersGuildModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setImperialDossierCharioteersGuildEnabled(isChecked)));
        imperialDossierHouseHawkwoodModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setImperialDossierHouseHawkwoodEnabled(isChecked)));
        imperialDossierReevesGuildModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setImperialDossierReevesGuildEnabled(isChecked)));
        vuldrokSpaceModule.setOnCheckedChangeListener((buttonView, isChecked) -> updateModuleSetting(() ->
                SettingsHandler.getSettingsEntity().setVuldrokSpaceEnabled(isChecked)));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_settings_fragment, container, false);

        nonOfficialEnabled = root.findViewById(R.id.official_selector);
        restrictionsIgnored = root.findViewById(R.id.restricted_selector);
        playerGuideModule = root.findViewById(R.id.module_player_guide);
        factionBookModule = root.findViewById(R.id.module_factions_book);
        revisedEditionModule = root.findViewById(R.id.module_revised_edition);
        lostWorldsBookModule = root.findViewById(R.id.module_lost_worlds_book);
        imperialDossierBrotherBattleModule = root.findViewById(R.id.module_imperial_dossier_brother_battle);
        imperialDossierCharioteersGuildModule = root.findViewById(R.id.module_imperial_dossier_charioteers_guild);
        imperialDossierHouseHawkwoodModule = root.findViewById(R.id.module_imperial_dossier_house_hawkwood);
        imperialDossierReevesGuildModule = root.findViewById(R.id.module_imperial_dossier_reeves_guild);
        vuldrokSpaceModule = root.findViewById(R.id.module_vuldrok_space);

        CharacterManager.addCharacterSettingsUpdateListeners(this::updateSettings);
        SettingsHandler.addSettingsUpdateListeners(() -> updateSettings(CharacterManager.getSelectedCharacter()));

        return root;
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() == null) {
            return;
        }
        populateElements(root, characterPlayer);
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        if (getContext() == null) {
            return;
        }
        updatingSettings = true;
        nonOfficialEnabled.setChecked(!character.getSettings().isOnlyOfficialAllowed());
        restrictionsIgnored.setChecked(!character.getSettings().isRestrictionsChecked());
        playerGuideModule.setChecked(SettingsHandler.getSettingsEntity().isPlayerGuideEnabled());
        playerGuideModule.setEnabled(true);
        factionBookModule.setChecked(SettingsHandler.getSettingsEntity().isFactionsBookEnabled());
        revisedEditionModule.setChecked(SettingsHandler.getSettingsEntity().isRevisedEditionEnabled());
        lostWorldsBookModule.setChecked(SettingsHandler.getSettingsEntity().isLostWorldsBookEnabled());
        imperialDossierBrotherBattleModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierBrotherBattleEnabled());
        imperialDossierCharioteersGuildModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierCharioteersGuildEnabled());
        imperialDossierHouseHawkwoodModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierHouseHawkwoodEnabled());
        imperialDossierReevesGuildModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierReevesGuildEnabled());
        vuldrokSpaceModule.setChecked(SettingsHandler.getSettingsEntity().isVuldrokSpaceEnabled());
        updatingSettings = false;
    }

    private void updateModuleSetting(ModuleSettingUpdater updater) {
        if (updatingSettings) {
            return;
        }
        if (getContext() == null) {
            return;
        }
        updater.update();
        SettingsHandler.save(getContext());
        SettingsHandler.setModulesBySettings();
    }

    private void setSwitchValue(SwitchCompat switchCompat, boolean checked) {
        updatingSettings = true;
        switchCompat.setChecked(checked);
        updatingSettings = false;
    }

    private interface ModuleSettingUpdater {
        void update();
    }
}
