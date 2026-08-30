package com.softwaremagico.tm.advisor.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.persistence.SettingsHandler;

public class SettingsWindow extends DialogFragment {
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
    private SwitchCompat debugModeEnabled;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_window, container, false);

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
        debugModeEnabled = root.findViewById(R.id.debug_mode_selector);

        nonOfficialEnabled.setChecked(!SettingsHandler.getSettingsEntity().onlyOfficialAllowed);
        restrictionsIgnored.setChecked(!SettingsHandler.getSettingsEntity().restrictionsChecked);
        playerGuideModule.setChecked(SettingsHandler.getSettingsEntity().isPlayerGuideEnabled());
        playerGuideModule.setEnabled(false);
        factionBookModule.setChecked(SettingsHandler.getSettingsEntity().isFactionsBookEnabled());
        revisedEditionModule.setChecked(SettingsHandler.getSettingsEntity().isRevisedEditionEnabled());
        lostWorldsBookModule.setChecked(SettingsHandler.getSettingsEntity().isLostWorldsBookEnabled());
        imperialDossierBrotherBattleModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierBrotherBattleEnabled());
        imperialDossierCharioteersGuildModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierCharioteersGuildEnabled());
        imperialDossierHouseHawkwoodModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierHouseHawkwoodEnabled());
        imperialDossierReevesGuildModule.setChecked(SettingsHandler.getSettingsEntity().isImperialDossierReevesGuildEnabled());
        vuldrokSpaceModule.setChecked(SettingsHandler.getSettingsEntity().isVuldrokSpaceEnabled());
        debugModeEnabled.setChecked(SettingsHandler.getSettingsEntity().isDebugModeEnabled());

        nonOfficialEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setOnlyOfficialAllowed(!isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.updateCharacterSettings();
        });

        restrictionsIgnored.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setRestrictionsChecked(!isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.updateCharacterSettings();
        });

        playerGuideModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setPlayerGuideEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        factionBookModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setFactionsBookEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        revisedEditionModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setRevisedEditionEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        lostWorldsBookModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setLostWorldsBookEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        imperialDossierBrotherBattleModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setImperialDossierBrotherBattleEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        imperialDossierCharioteersGuildModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setImperialDossierCharioteersGuildEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        imperialDossierHouseHawkwoodModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setImperialDossierHouseHawkwoodEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        imperialDossierReevesGuildModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setImperialDossierReevesGuildEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        vuldrokSpaceModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setVuldrokSpaceEnabled(isChecked);
            SettingsHandler.save(getContext());
            SettingsHandler.setModulesBySettings();
        });

        debugModeEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsHandler.getSettingsEntity().setDebugModeEnabled(isChecked);
            SettingsHandler.save(getContext());
        });


        return root;
    }
}