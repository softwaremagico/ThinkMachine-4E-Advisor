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

package com.softwaremagico.tm.advisor.ui.character.info;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CharacterInfoViewModel extends ViewModel {
    private List<Gender> genders;


    public List<Faction> getAvailableFactions(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return FactionFactory.getInstance().getElements()
                        .stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return FactionFactory.getInstance().getElements()
                        .stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }

    public List<Planet> getAvailablePlanets(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return PlanetFactory.getInstance().getElements()
                        .stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return PlanetFactory.getInstance().getElements()
                        .stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }

    public List<Specie> getAvailableSpecies(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return SpecieFactory.getInstance().getElements()
                        .stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return SpecieFactory.getInstance().getElements()
                        .stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }

    public List<Gender> getAvailableGenders() {
        if (genders == null) {
            genders = Arrays.asList(Gender.values());
        }
        return genders;
    }
}
