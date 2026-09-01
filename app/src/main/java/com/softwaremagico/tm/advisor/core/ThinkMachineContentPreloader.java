package com.softwaremagico.tm.advisor.core;

import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.Arrays;
import java.util.List;

public final class ThinkMachineContentPreloader {
    private ThinkMachineContentPreloader() {
    }

    private static List<XmlFactory<?>> getFactories() {
        return Arrays.asList(
                SpecieFactory.getInstance(),
                UpbringingFactory.getInstance(),
                FactionFactory.getInstance(),
                CallingFactory.getInstance(),
                PlanetFactory.getInstance(),
                PerkFactory.getInstance(),
                CapabilityFactory.getInstance(),
                CharacteristicsDefinitionFactory.getInstance(),
                SkillFactory.getInstance(),
                WeaponFactory.getInstance(),
                ArmorFactory.getInstance(),
                HandheldShieldFactory.getInstance(),
                ShieldFactory.getInstance(),
                OccultismPathFactory.getInstance()
        );
    }

    public static int getFactoryCount() {
        return getFactories().size();
    }

    public static void addElementsLoadedListener(XmlFactory.ElementsLoadedListener listener) {
        if (listener == null) {
            return;
        }
        for (XmlFactory<?> factory : getFactories()) {
            factory.addElementsLoadedListener(listener);
        }
    }

    public static void removeElementsLoadedListener(XmlFactory.ElementsLoadedListener listener) {
        if (listener == null) {
            return;
        }
        for (XmlFactory<?> factory : getFactories()) {
            factory.removeElementsLoadedListener(listener);
        }
    }

    public static void preloadAll() {
        for (XmlFactory<?> factory : getFactories()) {
            try {
                factory.getSelectableElements();
            } catch (RuntimeException e) {
                AdvisorLog.errorMessage(ThinkMachineContentPreloader.class.getName(), e);
            }
        }
    }
}


