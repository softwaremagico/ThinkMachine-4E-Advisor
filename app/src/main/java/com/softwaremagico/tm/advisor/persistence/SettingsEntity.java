package com.softwaremagico.tm.advisor.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.softwaremagico.tm.character.Settings;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = SettingsEntity.SETTINGS_TABLE)
public class SettingsEntity extends BaseEntity {
    public static final String SETTINGS_TABLE = "settings";

    @ColumnInfo(name = "only_official_allowed")
    public boolean onlyOfficialAllowed = false;

    @ColumnInfo(name = "restrictions_checked")
    public boolean restrictionsChecked = true;

    @ColumnInfo(name = "player_guide_module", defaultValue = "1")
    public boolean playerGuideEnabled = true;

    @ColumnInfo(name = "faction_book_module", defaultValue = "1")
    public boolean factionsBookEnabled = true;

    @ColumnInfo(name = "revised_edition_module", defaultValue = "1")
    public boolean revisedEditionEnabled = true;

    @ColumnInfo(name = "lost_worlds_book_module", defaultValue = "1")
    public boolean lostWorldsBookEnabled = true;

    @ColumnInfo(name = "imperial_dossier_brother_battle_module", defaultValue = "1")
    public boolean imperialDossierBrotherBattleEnabled = true;

    @ColumnInfo(name = "imperial_dossier_charioteers_guild_module", defaultValue = "1")
    public boolean imperialDossierCharioteersGuildEnabled = true;

    @ColumnInfo(name = "imperial_dossier_house_hawkwood_module", defaultValue = "1")
    public boolean imperialDossierHouseHawkwoodEnabled = true;

    @ColumnInfo(name = "imperial_dossier_reeves_guild_module", defaultValue = "1")
    public boolean imperialDossierReevesGuildEnabled = true;

    @ColumnInfo(name = "vuldrok_space_module", defaultValue = "1")
    public boolean vuldrokSpaceEnabled = true;

    public SettingsEntity() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public SettingsEntity(Settings settings) {
        this();
        set(settings);
    }

    public void set(Settings settings) {
        this.setOnlyOfficialAllowed(settings.isOnlyOfficialAllowed());
        this.setRestrictionsChecked(settings.isRestrictionsChecked());
        this.setPlayerGuideEnabled(settings.isPlayerGuideEnabled());
        this.setFactionsBookEnabled(settings.isFactionsBookEnabled());
    }

    public Settings get() {
        final Settings settings = new Settings();
        settings.setOnlyOfficialAllowed(this.isOnlyOfficialAllowed());
        settings.setRestrictionsChecked(this.isRestrictionsChecked());
        settings.setPlayerGuideEnabled(this.isPlayerGuideEnabled());
        settings.setFactionsBookEnabled(this.isFactionsBookEnabled());
        return settings;
    }


    public boolean isOnlyOfficialAllowed() {
        return onlyOfficialAllowed;
    }

    public void setOnlyOfficialAllowed(boolean onlyOfficialAllowed) {
        this.onlyOfficialAllowed = onlyOfficialAllowed;
    }

    public boolean isRestrictionsChecked() {
        return restrictionsChecked;
    }

    public void setRestrictionsChecked(boolean restrictionsChecked) {
        this.restrictionsChecked = restrictionsChecked;
    }

    public boolean isPlayerGuideEnabled() {
        return playerGuideEnabled;
    }

    public void setPlayerGuideEnabled(boolean playerGuideEnabled) {
        this.playerGuideEnabled = playerGuideEnabled;
    }

    public boolean isFactionsBookEnabled() {
        return factionsBookEnabled;
    }

    public void setFactionsBookEnabled(boolean factionsBookEnabled) {
        this.factionsBookEnabled = factionsBookEnabled;
    }

    public boolean isRevisedEditionEnabled() {
        return revisedEditionEnabled;
    }

    public void setRevisedEditionEnabled(boolean revisedEditionEnabled) {
        this.revisedEditionEnabled = revisedEditionEnabled;
    }

    public boolean isLostWorldsBookEnabled() {
        return lostWorldsBookEnabled;
    }

    public void setLostWorldsBookEnabled(boolean lostWorldsBookEnabled) {
        this.lostWorldsBookEnabled = lostWorldsBookEnabled;
    }

    public boolean isImperialDossierBrotherBattleEnabled() {
        return imperialDossierBrotherBattleEnabled;
    }

    public void setImperialDossierBrotherBattleEnabled(boolean imperialDossierBrotherBattleEnabled) {
        this.imperialDossierBrotherBattleEnabled = imperialDossierBrotherBattleEnabled;
    }

    public boolean isImperialDossierCharioteersGuildEnabled() {
        return imperialDossierCharioteersGuildEnabled;
    }

    public void setImperialDossierCharioteersGuildEnabled(boolean imperialDossierCharioteersGuildEnabled) {
        this.imperialDossierCharioteersGuildEnabled = imperialDossierCharioteersGuildEnabled;
    }

    public boolean isImperialDossierHouseHawkwoodEnabled() {
        return imperialDossierHouseHawkwoodEnabled;
    }

    public void setImperialDossierHouseHawkwoodEnabled(boolean imperialDossierHouseHawkwoodEnabled) {
        this.imperialDossierHouseHawkwoodEnabled = imperialDossierHouseHawkwoodEnabled;
    }

    public boolean isImperialDossierReevesGuildEnabled() {
        return imperialDossierReevesGuildEnabled;
    }

    public void setImperialDossierReevesGuildEnabled(boolean imperialDossierReevesGuildEnabled) {
        this.imperialDossierReevesGuildEnabled = imperialDossierReevesGuildEnabled;
    }

    public boolean isVuldrokSpaceEnabled() {
        return vuldrokSpaceEnabled;
    }

    public void setVuldrokSpaceEnabled(boolean vuldrokSpaceEnabled) {
        this.vuldrokSpaceEnabled = vuldrokSpaceEnabled;
    }
}
