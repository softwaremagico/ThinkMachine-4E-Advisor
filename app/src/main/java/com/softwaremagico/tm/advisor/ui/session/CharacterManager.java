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
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.specie.Specie;
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
    private static final Set<CharacterSelectedListener> characterSelectedListener = new HashSet<>();
    private static final Set<CharacterUpdatedListener> characterUpdatedListener = new HashSet<>();
    private static final Set<CharacterRaceUpdatedListener> characterRaceUpdatedListener = new HashSet<>();
    private static final Set<CharacterAgeUpdatedListener> characterAgeUpdatedListener = new HashSet<>();
    private static final Set<CharacterPlanetUpdatedListener> characterPlanetUpdatedListener = new HashSet<>();
    private static final Set<CharacterFactionUpdatedListener> characterFactionUpdatedListener = new HashSet<>();
    private static final Set<CharacterCharacteristicUpdatedListener> characterCharacteristicUpdatedListener = new HashSet<>();
    private static final Set<CharacterSettingsUpdatedListener> characterSettingsUpdatedListeners = new HashSet<>();
    private static final Set<CyberneticDeviceUpdatedListener> cyberneticDeviceUpdatedListeners = new HashSet<>();
    private static boolean updatingCharacter = false;

    public interface CharacterSelectedListener {
        void selected(CharacterPlayer characterPlayer);
    }

    public interface CharacterUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CharacterRaceUpdatedListener {
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

    public interface CharacterCharacteristicUpdatedListener {
        void updated(CharacterPlayer characterPlayer, CharacteristicName characteristic);
    }

    public interface CharacterSettingsUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    public interface CyberneticDeviceUpdatedListener {
        void updated(CharacterPlayer characterPlayer);
    }

    private CharacterManager() {

    }

    public static void updateSettings() {
        launchCharacterSettingsUpdateListeners(getSelectedCharacter());
    }

    public static void launchCharacterSettingsUpdateListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterSettingsUpdatedListener listener : characterSettingsUpdatedListeners) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchSelectedCharacterListeners(CharacterPlayer characterPlayer) {
        updatingCharacter = true;
        for (final CharacterSelectedListener listener : characterSelectedListener) {
            listener.selected(characterPlayer);
        }
        updatingCharacter = false;
    }

    private static void launchCharacterUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterUpdatedListener listener : characterUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    private static void launchCharacterRaceUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterRaceUpdatedListener listener : characterRaceUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterAgeUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterAgeUpdatedListener listener : characterAgeUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterPlanetUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterPlanetUpdatedListener listener : characterPlanetUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterFactionUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CharacterFactionUpdatedListener listener : characterFactionUpdatedListener) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void launchCharacterCharacteristicsUpdatedListeners(CharacterPlayer characterPlayer, CharacteristicName characteristicName) {
        if (!updatingCharacter) {
            for (final CharacterCharacteristicUpdatedListener listener : characterCharacteristicUpdatedListener) {
                listener.updated(characterPlayer, characteristicName);
            }
        }
    }

    public static void launchCyberneticDeviceUpdatedListeners(CharacterPlayer characterPlayer) {
        if (!updatingCharacter) {
            for (final CyberneticDeviceUpdatedListener listener : cyberneticDeviceUpdatedListeners) {
                listener.updated(characterPlayer);
            }
        }
    }

    public static void addCharacterSettingsUpdateListeners(CharacterSettingsUpdatedListener listener) {
        characterSettingsUpdatedListeners.add(listener);
    }

    public static void addSelectedCharacterListener(CharacterSelectedListener listener) {
        characterSelectedListener.add(listener);
    }

    public static void addCharacterUpdatedListener(CharacterUpdatedListener listener) {
        characterUpdatedListener.add(listener);
    }

    public static void addCharacterRaceUpdatedListener(CharacterRaceUpdatedListener listener) {
        characterRaceUpdatedListener.add(listener);
    }

    public static void addCharacterAgeUpdatedListener(CharacterAgeUpdatedListener listener) {
        characterAgeUpdatedListener.add(listener);
    }

    public static void addCharacterPlanetUpdatedListener(CharacterPlanetUpdatedListener listener) {
        characterPlanetUpdatedListener.add(listener);
    }

    public static void addCharacterFactionUpdatedListener(CharacterFactionUpdatedListener listener) {
        characterFactionUpdatedListener.add(listener);
    }

    public static void addCharacterCharacteristicUpdatedListener(CharacterCharacteristicUpdatedListener listener) {
        characterCharacteristicUpdatedListener.add(listener);
    }

    public static void addCyberneticDeviceUpdatedListeners(CyberneticDeviceUpdatedListener listener) {
        cyberneticDeviceUpdatedListeners.add(listener);
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
        launchCharacterRaceUpdatedListeners(getSelectedCharacter());
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

}
