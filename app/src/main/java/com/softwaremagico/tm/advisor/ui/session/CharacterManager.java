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

package com.softwaremagico.tm.advisor.ui.session;

import com.softwaremagico.tm.advisor.persistence.SettingsHandler;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CharacterManager {
    private static final List<CharacterPlayer> characters = new ArrayList<>();
    private static CharacterPlayer selectedCharacter;
    private static final Set<CharacterSelectedListener> CHARACTER_SELECTED_LISTENERS = new HashSet<>();
    private static final Set<CharacterUpdatedListener> CHARACTER_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CharacterSpecieUpdatedListener> CHARACTER_SPECIE_UPDATED_LISTENER = new HashSet<>();
    private static final Set<CharacterAgeUpdatedListener> CHARACTER_AGE_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CharacterPlanetUpdatedListener> CHARACTER_PLANET_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CharacterFactionUpdatedListener> CHARACTER_FACTION_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CharacterCallingUpdatedListener> CHARACTER_CALLING_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CharacterUpbringingUpdatedListener> CHARACTER_UPBRINGING_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CharacterCharacteristicsUpdatedListener> CHARACTER_CHARACTERISTICS_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CharacterSettingsUpdatedListener> CHARACTER_SETTINGS_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<PerkUpdatedListener> PERK_UPDATED_LISTENERS = new HashSet<>();
    private static final Set<CyberneticDeviceUpdatedListener> CYBERNETIC_DEVICE_UPDATED_LISTENERS = new HashSet<>();
    private static boolean updatingCharacter = false;

    public interface CharacterSelectedListener {
        void selected(CharacterPlayer characterPlayer);
    }

    public interface CharacterUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterSpecieUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterAgeUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterPlanetUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterFactionUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterCallingUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterUpbringingUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterCharacteristicUpdatedListener {
        void updated(CharacterPlayer characterPlayer, CharacteristicName characteristic);
    }

    public interface CharacterCharacteristicsUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterSettingsUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CyberneticDeviceUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface PerkUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    private CharacterManager() {

    }

    public static void updateSettings() {
        launchCharacterSettingsUpdateListeners(getSelectedCharacter());
    }

    public static void launchCharacterSettingsUpdateListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterSettingsUpdatedListener listener : CHARACTER_SETTINGS_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchSelectedCharacterListeners(CharacterPlayer characterPlayer) {
        updatingCharacter = true;
        for (final CharacterSelectedListener listener : CHARACTER_SELECTED_LISTENERS) {
            listener.selected(characterPlayer);
        }
        updatingCharacter = false;
    }

    private static void launchCharacterUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterUpdatedListener listener : CHARACTER_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    private static void launchCharacterSpecieUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterSpecieUpdatedListener listener : CHARACTER_SPECIE_UPDATED_LISTENER) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterAgeUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterAgeUpdatedListener listener : CHARACTER_AGE_UPDATED_LISTENERS) {
               // listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterPlanetUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterPlanetUpdatedListener listener : CHARACTER_PLANET_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterFactionUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterFactionUpdatedListener listener : CHARACTER_FACTION_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterCallingsUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterCallingUpdatedListener listener : CHARACTER_CALLING_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterUpbringingsUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterUpbringingUpdatedListener listener : CHARACTER_UPBRINGING_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterCharacteristicsUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterCharacteristicsUpdatedListener listener : CHARACTER_CHARACTERISTICS_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCyberneticDeviceUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CyberneticDeviceUpdatedListener listener : CYBERNETIC_DEVICE_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchPerkUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final PerkUpdatedListener listener : PERK_UPDATED_LISTENERS) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void addCharacterSettingsUpdateListeners(CharacterSettingsUpdatedListener listener) {
        CHARACTER_SETTINGS_UPDATED_LISTENERS.add(listener);
    }

    public static void addSelectedCharacterListener(CharacterSelectedListener listener) {
        CHARACTER_SELECTED_LISTENERS.add(listener);
    }

    public static void addCharacterUpdatedListener(CharacterUpdatedListener listener) {
        CHARACTER_UPDATED_LISTENERS.add(listener);
    }

    public static void addCharacterSpecieUpdatedListener(CharacterSpecieUpdatedListener listener) {
        CHARACTER_SPECIE_UPDATED_LISTENER.add(listener);
    }

    public static void addCharacterUpbringingUpdatedListener(CharacterUpbringingUpdatedListener listener) {
        CHARACTER_UPBRINGING_UPDATED_LISTENERS.add(listener);
    }

    public static void addCharacterAgeUpdatedListener(CharacterAgeUpdatedListener listener) {
        CHARACTER_AGE_UPDATED_LISTENERS.add(listener);
    }

    public static void addCharacterPlanetUpdatedListener(CharacterPlanetUpdatedListener listener) {
        CHARACTER_PLANET_UPDATED_LISTENERS.add(listener);
    }

    public static void addCharacterFactionUpdatedListener(CharacterFactionUpdatedListener listener) {
        CHARACTER_FACTION_UPDATED_LISTENERS.add(listener);
    }

    public static void addCharacterCallingUpdatedListener(CharacterCallingUpdatedListener listener) {
        CHARACTER_CALLING_UPDATED_LISTENERS.add(listener);
    }

    public static void addCharacterCharacteristicsUpdatedListener(CharacterCharacteristicsUpdatedListener listener) {
        CHARACTER_CHARACTERISTICS_UPDATED_LISTENERS.add(listener);
    }

    public static void addCyberneticDeviceUpdatedListeners(CyberneticDeviceUpdatedListener listener) {
        CYBERNETIC_DEVICE_UPDATED_LISTENERS.add(listener);
    }

    public static PerkUpdatedListener addPerkUpdatedListeners(PerkUpdatedListener listener) {
        PERK_UPDATED_LISTENERS.add(listener);
        return listener;
    }

    public static void removePerkUpdatedListeners(PerkUpdatedListener listener) {
        PERK_UPDATED_LISTENERS.remove(listener);
    }

    public static boolean isStarted() {
        return !characters.isEmpty();
    }


    public synchronized static CharacterPlayer getSelectedCharacter() {
        if (characters.isEmpty()) {
            addNewCharacter();
        }
        return selectedCharacter;
    }

    public synchronized static void setSelectedCharacter(CharacterPlayer characterPlayer) {
        if (!characters.contains(characterPlayer)) {
            characters.add(characterPlayer);
        }
        selectedCharacter(characterPlayer);
    }

    private synchronized static void selectedCharacter(CharacterPlayer characterPlayer) {
        selectedCharacter = characterPlayer;
        launchSelectedCharacterListeners(characterPlayer);
    }

    private static CharacterPlayer createNewCharacter() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.getSettings().copy(SettingsHandler.getSettingsEntity().get());
        characters.add(characterPlayer);
        return characterPlayer;
    }

    public static void addNewCharacter() {
        setSelectedCharacter(createNewCharacter());
    }

    public static void removeSelectedCharacter() {
        characters.remove(getSelectedCharacter());
        if (!characters.isEmpty()) {
            setSelectedCharacter(characters.get(0));
        } else {
            addNewCharacter();
        }
    }

    public static List<CharacterPlayer> getCharacters() {
        return characters;
    }

    public static void setSpecie(Specie specie) throws InvalidSpecieException, RestrictedElementException, UnofficialElementNotAllowedException {
        if (specie != null) {
            getSelectedCharacter().setSpecie(specie.getId());
        } else {
            getSelectedCharacter().setSpecie(null);
        }
        launchCharacterSpecieUpdatedListeners(getSelectedCharacter());
        launchCharacterUpdatedListeners(getSelectedCharacter());
    }

    public static void setPlanet(Planet planet) {
        if (planet != null) {
            getSelectedCharacter().getInfo().setPlanet(planet.getId());
        } else {
            getSelectedCharacter().getInfo().setPlanet((String) null);
        }
        launchCharacterPlanetUpdatedListeners(getSelectedCharacter());
    }

    public static void setFaction(Faction faction) throws InvalidFactionException, RestrictedElementException, UnofficialElementNotAllowedException {
        if (faction != null) {
            getSelectedCharacter().setFaction(faction.getId());
        } else {
            getSelectedCharacter().setFaction((String) null);
        }
        launchCharacterFactionUpdatedListeners(getSelectedCharacter());
    }

    public static void setCalling(Calling calling) throws InvalidFactionException, RestrictedElementException, UnofficialElementNotAllowedException {
        if (calling != null) {
            getSelectedCharacter().setCalling(calling.getId());
        } else {
            getSelectedCharacter().setCalling((String) null);
        }
        launchCharacterCallingsUpdatedListeners(getSelectedCharacter());
    }

    public static void setUpbringing(Upbringing upbringing) throws InvalidFactionException, RestrictedElementException, UnofficialElementNotAllowedException {
        if (upbringing != null) {
            getSelectedCharacter().setUpbringing(upbringing.getId());
        } else {
            getSelectedCharacter().setUpbringing((String) null);
        }
        launchCharacterUpbringingsUpdatedListeners(getSelectedCharacter());
    }

}
