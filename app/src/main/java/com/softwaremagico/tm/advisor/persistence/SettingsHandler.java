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

package com.softwaremagico.tm.advisor.persistence;

import android.content.Context;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.file.modules.ModuleManager;

import java.util.HashSet;
import java.util.Set;

public final class SettingsHandler {
    private static volatile SettingsHandler instance;
    private static SettingsEntity settingsEntity;


    private static final Set<SettingsUpdatedListener> settingsUpdatedListeners = new HashSet<>();

    public interface SettingsUpdatedListener {
        void updated();
    }

    public static void launchSettingsUpdateListeners() {
        for (final SettingsUpdatedListener listener : settingsUpdatedListeners) {
            listener.updated();
        }
    }

    public static void addSettingsUpdateListeners(SettingsUpdatedListener listener) {
        settingsUpdatedListeners.add(listener);
    }

    public static SettingsHandler getInstance() {
        if (instance == null) {
            synchronized (SettingsHandler.class) {
                if (instance == null) {
                    instance = new SettingsHandler();
                }
            }
        }
        return instance;
    }

    public static void save(Context context) {
        save(context, SettingsHandler.settingsEntity);
    }

    public static void save(Context context, SettingsEntity settingsEntity) {
        launchSettingsUpdateListeners();
        if (SettingsHandler.settingsEntity == null) {
            AppDatabase.getInstance(context).getSettingsEntityDao().persist(settingsEntity);
            SettingsHandler.settingsEntity = settingsEntity;
        } else {
            AppDatabase.getInstance(context).getSettingsEntityDao().delete(settingsEntity);
            AppDatabase.getInstance(context).getSettingsEntityDao().persist(settingsEntity);
        }
    }

    public static SettingsEntity loadSettingsEntity(Context context) {
        SettingsEntity settingsEntity = AppDatabase.getInstance(context).getSettingsEntityDao().get();
        if (settingsEntity == null) {
            settingsEntity = new SettingsEntity();
        }
        return settingsEntity;
    }

    public static SettingsEntity getSettingsEntity() {
        return settingsEntity;
    }

    public static void setSettingsEntity(Context context) {
        SettingsHandler.settingsEntity = loadSettingsEntity(context);
    }

    public static void setSettingsEntity(SettingsEntity settingsEntity) {
        SettingsHandler.settingsEntity = settingsEntity;
    }

    public static void setModulesBySettings() {
        if (getSettingsEntity().isPlayerGuideEnabled()) {
            ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        }
        if (getSettingsEntity().isFactionsBookEnabled()) {
            ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        }
        if (getSettingsEntity().isRevisedEditionEnabled()) {
            ModuleManager.enableModule(ModuleManager.FADING_SUNS_REVISED_EDITION_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.FADING_SUNS_REVISED_EDITION_MODULE);
        }
        if (getSettingsEntity().isLostWorldsBookEnabled()) {
            ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        }
        if (getSettingsEntity().isImperialDossierBrotherBattleEnabled()) {
            ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE);
        }
        if (getSettingsEntity().isImperialDossierCharioteersGuildEnabled()) {
            ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_MODULE);
        }
        if (getSettingsEntity().isImperialDossierHouseHawkwoodEnabled()) {
            ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_MODULE);
        }
        if (getSettingsEntity().isImperialDossierReevesGuildEnabled()) {
            ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_REEVES_GUILD_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_REEVES_GUILD_MODULE);
        }
        if (getSettingsEntity().isVuldrokSpaceEnabled()) {
            ModuleManager.enableModule(ModuleManager.VULDROK_SPACE_MODULE);
        } else {
            ModuleManager.disableModule(ModuleManager.VULDROK_SPACE_MODULE);
        }
        ModuleManager.resetModules();
        updateCharacterSettings();
    }

    public static void updateCharacterSettings(){
        CharacterManager.getSelectedCharacter().getSettings().copy(SettingsHandler.getSettingsEntity().get());
        CharacterManager.updateSettings();
    }
}
