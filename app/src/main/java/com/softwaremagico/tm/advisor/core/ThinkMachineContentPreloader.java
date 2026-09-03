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

    /**
     * Preloads all ThinkMachine character factories in strict dependency order.
     *
     * <p><b>PURPOSE:</b> Ensures that factories with XML/JSON cross-references are loaded
     * after their dependency factories, so Jackson can resolve abstract types correctly
     * during deserialization.
     *
     * <p><b>FACTORY LOAD ORDER &amp; DEPENDENCIES:</b></p>
     *
     * <p><b>TIER 1 — Root Entities (no dependencies):</b>
     * <ul>
     *   <li>SpecieFactory</li>
     *   <li>UpbringingFactory</li>
     *   <li>FactionFactory</li>
     *   <li>PlanetFactory</li>
     * </ul>
     * These are independent core character creation elements.
     * </p>
     *
     * <p><b>TIER 2 — Base Attributes &amp; Abilities:</b>
     * <ul>
     *   <li>CharacteristicsDefinitionFactory (must load first)</li>
     *   <li>CapabilityFactory (depends on Characteristics)</li>
     *   <li>SkillFactory</li>
     * </ul>
     * These define character attributes. CharacteristicsDefinitionFactory must precede
     * CapabilityFactory because Capability XML references characteristic types.
     * </p>
     *
     * <p><b>TIER 3 — Complex Entities (depends on TIER 2):</b>
     * <ul>
     *   <li><b style="color:red">CallingFactory</b> — <b>CRITICAL:</b> must load AFTER CapabilityFactory</li>
     * </ul>
     *
     * <p><b>Why Calling needs Capability first:</b>
     * <ul>
     *   <li>Calling XML contains {@code CapabilityOptions} objects (Jackson sees them as abstract)</li>
     *   <li>CapabilityOptions has multiple concrete subtypes (e.g., LogicalCapabilityOption, SelectCapabilityOption)</li>
     *   <li>If CapabilityFactory hasn't loaded, Jackson doesn't know these subtypes exist</li>
     *   <li>Result: {@code "Cannot construct instance of CapabilityOptions (no Creators, like default constructor, exist)"}</li>
     *   <li>Solution: Load CapabilityFactory.getSelectableElements() BEFORE CallingFactory.getSelectableElements()</li>
     * </ul>
     * </p>
     *
     * <p><b>TIER 4 — Equipment &amp; Specializations:</b>
     * <ul>
     *   <li>PerkFactory</li>
     *   <li>WeaponFactory, ArmorFactory, HandheldShieldFactory, ShieldFactory</li>
     *   <li>OccultismPathFactory</li>
     * </ul>
     * These are mostly independent; can load in any order after TIER 3.
     * </p>
     *
     * <p><b>TECHNICAL BACKGROUND:</b>
     * ThinkMachine uses Jackson to deserialize character elements from XML files.
     * When an element's XML tree references abstract types defined in OTHER factories,
     * Jackson needs those factories' type information registered first. Violating this order
     * causes Jackson to fail when instantiating abstract types without a registered concrete implementation.
     * </p>
     *
     * <p><b>FAILURE SCENARIO (if order is wrong):</b>
     * <pre>
     * Calling XML (excerpt):
     *   {
     *     "capabilities": [
     *       {
     *         "options": [
     *           { "@type": "LogicalCapabilityOption", ... }
     *         ]
     *       }
     *     ]
     *   }
     *
     * If CapabilityFactory NOT yet loaded:
     *   Jackson: "What is LogicalCapabilityOption? I don't know..."
     *   Result: RuntimeException about abstract types
     * </pre>
     * </p>
     */
    public static void preloadAll() {
        // Load factories in strict dependency order to avoid deserialization issues
        List<XmlFactory<?>> prioritized = Arrays.asList(
                // ============ TIER 1: No dependencies ============
                SpecieFactory.getInstance(),
                UpbringingFactory.getInstance(),
                FactionFactory.getInstance(),
                PlanetFactory.getInstance(),

                // ============ TIER 2: Base definitions ============
                // Load Characteristics BEFORE Capabilities (Capability needs CharacteristicDefinition)
                CharacteristicsDefinitionFactory.getInstance(),
                CapabilityFactory.getInstance(),
                SkillFactory.getInstance(),

                // ============ TIER 3: Depends on TIER 2 ============
                // CRITICAL: Load AFTER CapabilityFactory
                // Calling contains CapabilityOptions which Jackson needs to resolve
                CallingFactory.getInstance(),

                // ============ TIER 4: Equipment & special ============
                // These can load in any order now that TIER 3 is ready
                PerkFactory.getInstance(),
                WeaponFactory.getInstance(),
                ArmorFactory.getInstance(),
                HandheldShieldFactory.getInstance(),
                ShieldFactory.getInstance(),
                OccultismPathFactory.getInstance()
        );

        for (XmlFactory<?> factory : prioritized) {
            try {
                factory.getSelectableElements();
            } catch (RuntimeException e) {
                AdvisorLog.errorMessage(ThinkMachineContentPreloader.class.getName(),
                        "Failed to preload " + factory.getClass().getSimpleName() +
                        " (check dependency order in ThinkMachineContentPreloader)", e);
            }
        }
    }
}


