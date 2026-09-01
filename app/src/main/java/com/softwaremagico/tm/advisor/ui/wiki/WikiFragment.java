/*
 *  Copyright (C) 2026 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.wiki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.main.BookContentRefreshable;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WikiFragment extends Fragment implements BookContentRefreshable {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.character_wiki_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindWikiElements(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        final View view = getView();
        if (view != null) {
            bindWikiElements(view);
        }
    }

    private void bindWikiElements(@NonNull View view) {
        setupSpinner(view, R.id.wiki_specie, loadSpecies(), Specie.class);
        setupSpinner(view, R.id.wiki_upbringing, loadUpbringings(), Upbringing.class);
        setupSpinner(view, R.id.wiki_faction, loadFactions(), Faction.class);
        setupSpinner(view, R.id.wiki_calling, loadCallings(), Calling.class);
        setupSpinner(view, R.id.wiki_planet, loadPlanets(), Planet.class);

        setupSpinner(view, R.id.wiki_perk, loadPerks(), Perk.class);
        setupSpinner(view, R.id.wiki_capability, loadCapabilities(), Capability.class);
        setupSpinner(view, R.id.wiki_characteristic, loadCharacteristics(), CharacteristicDefinition.class);
        setupSpinner(view, R.id.wiki_skill, loadSkills(), Skill.class);

        setupSpinner(view, R.id.wiki_melee_weapon, loadMeleeWeapons(), Weapon.class);
        setupSpinner(view, R.id.wiki_ranged_weapon, loadRangedWeapons(), Weapon.class);
        setupSpinner(view, R.id.wiki_armor, loadArmors(), Armor.class);
        setupSpinner(view, R.id.wiki_handheld_shield, loadHandheldShields(), HandheldShield.class);
        setupSpinner(view, R.id.wiki_shield, loadShields(), Shield.class);
        setupSpinner(view, R.id.wiki_cyberdevice, loadCyberdevicePerks(), Perk.class);

        setupSpinner(view, R.id.wiki_occultism_path, loadOccultismPaths(), OccultismPath.class);
        setupSpinner(view, R.id.wiki_occultism_power, loadOccultismPowers(), OccultismPower.class);
    }

    @Override
    public void refreshBookContent() {
        final View view = getView();
        if (view != null && isAdded()) {
            bindWikiElements(view);
        }
    }

    private <E extends Element> void setupSpinner(View view, int id, List<E> elements, Class<E> clazz) {
        final ElementSpinner<E> selector = view.findViewById(id);
        if (selector == null) {
            return;
        }
        selector.setContextualStylesEnabled(false);
        final androidx.fragment.app.FragmentActivity activity = getActivity();
        if (activity == null || getContext() == null) {
            return;
        }

        final List<E> options = new ArrayList<>();
        if (elements != null) {
            for (E element : elements) {
                if (element != null) {
                    options.add(element);
                }
            }
        }
        options.sort((left, right) -> safeName(left).compareToIgnoreCase(safeName(right)));

        selector.setAdapter(new ElementAdapter<>(activity, options, false, clazz) {
            @Override
            protected void setElementColor(TextView elementRepresentation, E element, int position) {
                elementRepresentation.setTextColor(ContextCompat.getColor(activity, R.color.colorNormal));
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }
        });
    }

    private List<Specie> loadSpecies() {
        try {
            return SpecieFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Upbringing> loadUpbringings() {
        try {
            return UpbringingFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Faction> loadFactions() {
        try {
            return FactionFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Calling> loadCallings() {
        try {
            return CallingFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Planet> loadPlanets() {
        try {
            return PlanetFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Perk> loadPerks() {
        try {
            return PerkFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Capability> loadCapabilities() {
        try {
            return CapabilityFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<CharacteristicDefinition> loadCharacteristics() {
        try {
            return CharacteristicsDefinitionFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Skill> loadSkills() {
        try {
            return SkillFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Weapon> loadMeleeWeapons() {
        try {
            return WeaponFactory.getInstance().getSelectableElements().stream().filter(Objects::nonNull)
                    .filter(Weapon::isMeleeWeapon).collect(Collectors.toList());
        } catch (NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Weapon> loadRangedWeapons() {
        try {
            return WeaponFactory.getInstance().getSelectableElements().stream().filter(Objects::nonNull)
                    .filter(Weapon::isRangedWeapon).collect(Collectors.toList());
        } catch (NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Armor> loadArmors() {
        try {
            return ArmorFactory.getInstance().getSelectableElements();
        } catch (NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<HandheldShield> loadHandheldShields() {
        try {
            return HandheldShieldFactory.getInstance().getSelectableElements();
        } catch (NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Shield> loadShields() {
        try {
            return ShieldFactory.getInstance().getSelectableElements();
        } catch (NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<Perk> loadCyberdevicePerks() {
        try {
            return PerkFactory.getInstance().getSelectableElements().stream()
                    .filter(Objects::nonNull)
                    .filter(this::isCyberdevicePerk)
                    .sorted(Comparator.comparing(Perk::getNameRepresentation))
                    .collect(Collectors.toList());
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private boolean isCyberdevicePerk(Perk perk) {
        if (perk.getType() == null) {
            return false;
        }
        final String typeName = perk.getType().name();
        return "CYBERDEVICE".equalsIgnoreCase(typeName)
                || "CYBERDEVICES".equalsIgnoreCase(typeName)
                || "CYBERNETIC".equalsIgnoreCase(typeName)
                || "CYBERNETICS".equalsIgnoreCase(typeName);
    }

    private List<OccultismPath> loadOccultismPaths() {
        try {
            return OccultismPathFactory.getInstance().getSelectableElements();
        } catch (InvalidXmlElementException | NullPointerException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }

    private List<OccultismPower> loadOccultismPowers() {
        final Map<String, OccultismPower> powersById = new LinkedHashMap<>();

        for (OccultismPath path : loadOccultismPaths()) {
            if (path == null || path.getOccultismPowers() == null) {
                continue;
            }
            for (OccultismPower power : path.getOccultismPowers().values()) {
                if (power == null) {
                    continue;
                }
                powersById.put(power.getId(), power);
            }
        }

        return new ArrayList<>(powersById.values());
    }

    private String safeName(Element element) {
        try {
            return element.getNameRepresentation();
        } catch (InvalidXmlElementException e) {
            return "";
        }
    }
}
