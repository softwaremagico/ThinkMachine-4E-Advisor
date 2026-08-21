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

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.softwaremagico.tm.advisor.core.CharacterJsonManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidJsonException;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = CharacterEntity.CHARACTER_PLAYER_TABLE)
public class CharacterEntity extends BaseEntity {
    public static final String CHARACTER_PLAYER_TABLE = "character_player";

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "player")
    public String player;

    @ColumnInfo(name = "specie")
    public String specie;

    @ColumnInfo(name = "faction")
    public String faction;

    @ColumnInfo(name = "threat")
    public int threat;

    @ColumnInfo(name = "character_as_json")
    public String json;

    //Not persisted. Caches the deserialized character to avoid re-parsing the JSON payload
    //(an expensive Jackson reflection-based operation) on every call to getCharacterPlayer(),
    //which happens repeatedly while scrolling the character loader list.
    @androidx.room.Ignore
    private transient CharacterPlayer cachedCharacterPlayer;
    @androidx.room.Ignore
    private transient String cachedCharacterPlayerJson;

    public CharacterEntity() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public CharacterEntity(CharacterPlayer characterPlayer) {
        this();
        setCharacterPlayer(characterPlayer);
    }

    public final void setCharacterPlayer(CharacterPlayer characterPlayer) {
        if (characterPlayer == null) {
            return;
        }
        updateTime = new Timestamp(new Date().getTime());
        setJson(CharacterJsonManager.toJson(characterPlayer));
        //Cache the character we already have in hand instead of forcing a re-parse of the JSON
        //we just generated from it on the next getCharacterPlayer() call.
        cachedCharacterPlayer = characterPlayer;
        cachedCharacterPlayerJson = getJson();
        setName(characterPlayer.getCompleteNameRepresentation());
        if (characterPlayer.getSpecie() != null) {
            final com.softwaremagico.tm.character.specie.Specie specie = SpecieFactory.getInstance().getElement(characterPlayer.getSpecie());
            if (specie != null && specie.getNameRepresentation() != null) {
                setSpecie(specie.getNameRepresentation());
            } else {
                setSpecie(String.valueOf(characterPlayer.getSpecie()));
            }
        }
        if (characterPlayer.getFaction() != null) {
            final com.softwaremagico.tm.character.factions.Faction faction = com.softwaremagico.tm.character.factions.FactionFactory.getInstance().getElement(characterPlayer.getFaction());
            if (faction != null && faction.getNameRepresentation() != null) {
                setFaction(faction.getNameRepresentation());
            } else {
                setFaction(String.valueOf(characterPlayer.getFaction()));
            }
        }
        if (characterPlayer.getInfo() != null) {
            setPlayer(characterPlayer.getInfo().getPlayer());
        }
    }

    public CharacterPlayer getCharacterPlayer() {
        final String currentJson = getJson();
        if (cachedCharacterPlayer != null && java.util.Objects.equals(cachedCharacterPlayerJson, currentJson)) {
            return cachedCharacterPlayer;
        }
        try {
            cachedCharacterPlayer = CharacterJsonManager.fromJson(currentJson);
            cachedCharacterPlayerJson = currentJson;
            return cachedCharacterPlayer;
        } catch (InvalidJsonException e) {
            cachedCharacterPlayer = null;
            cachedCharacterPlayerJson = currentJson;
            return null;
        }
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getSpecie() {
        return specie;
    }

    public final void setSpecie(String specie) {
        this.specie = specie;
    }

    public final String getFaction() {
        return faction;
    }

    public final void setFaction(String faction) {
        this.faction = faction;
    }

    public final String getJson() {
        return json;
    }

    public final void setJson(String json) {
        this.json = json;
    }

    public final int getThreat() {
        return threat;
    }

    public final void setThreat(int threat) {
        this.threat = threat;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
