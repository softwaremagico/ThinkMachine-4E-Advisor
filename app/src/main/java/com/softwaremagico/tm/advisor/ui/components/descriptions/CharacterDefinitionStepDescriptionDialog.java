package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.EquipmentOption;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.skills.SkillBonusOption;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mixin that adds CharacterDefinitionStep-specific tables (characteristics,
 * skills, perks, capabilities, material awards) to description dialogs.
 */
public abstract class CharacterDefinitionStepDescriptionDialog<T extends CharacterDefinitionStep>
        extends ElementDescriptionDialog<T> {

    public CharacterDefinitionStepDescriptionDialog(T element) {
        super(element);
    }

    // ─── helpers ────────────────────────────────────────────────────────────

    protected String buildDefinitionStepDetails(T step) {
        final StringBuilder sb = new StringBuilder();

        appendCharacteristics(sb, step);
        appendSkills(sb, step);
        appendPerks(sb, step);
        appendCapabilities(sb, step);
        appendMaterialAwards(sb, step);

        return sb.toString();
    }

    private void appendCharacteristics(StringBuilder sb, T step) {
        if (step.getCharacteristicOptions() == null || step.getCharacteristicOptions().isEmpty()) {
            return;
        }
        appendSectionBreak(sb);
        sb.append("<br><b>").append(getString(R.string.characteristics)).append(":</b>");
        sb.append("<br>");
        for (CharacteristicBonusOptions opts : step.getCharacteristicOptions()) {
            if (opts.getOptions() == null || opts.getOptions().isEmpty()) continue;
            final int bonus = opts.getBonus();
            String names = opts.getOptions().stream()
                    .map(CharacteristicBonusOption::getName)
                    .filter(n -> n != null)
                    .map(n -> n.getTranslatedText())
                    .collect(Collectors.joining(" / "));
            sb.append("&nbsp;&nbsp;+").append(bonus).append(" ").append(names);
            sb.append("<br>");
        }
    }

    private void appendSkills(StringBuilder sb, T step) {
        if (step.getSkillOptions() == null || step.getSkillOptions().isEmpty()) {
            return;
        }
        appendSectionBreak(sb);
        sb.append("<br><b>").append(getString(R.string.skills)).append(":</b>");
        sb.append("<br>");
        for (SkillBonusOptions opts : step.getSkillOptions()) {
            if (opts.getOptions() == null || opts.getOptions().isEmpty()) continue;
            final int bonus = opts.getBonus();
            String names = opts.getOptions().stream()
                    .map(SkillBonusOption::getName)
                    .filter(n -> n != null)
                    .map(n -> n.getTranslatedText())
                    .collect(Collectors.joining(" / "));
            sb.append("&nbsp;&nbsp;+").append(bonus).append(" ").append(names);
            sb.append("<br>");
        }
    }

    private void appendPerks(StringBuilder sb, T step) {
        if (step.getSourcePerks() == null || step.getSourcePerks().isEmpty()) {
            return;
        }
        final Set<String> shownPerks = new HashSet<>();
        appendSectionBreak(sb);
        sb.append("<br><b>").append(getString(R.string.perks)).append(":</b><br>");
        for (PerkOptions opts : step.getSourcePerks()) {
            if (opts.getOptions() == null || opts.getOptions().isEmpty()) continue;
            final int total = opts.getTotalOptions();
            if (total > 1) {
                sb.append("&nbsp;&nbsp;<i>")
                  .append(safeTranslateTextTag("choose", "Choose")).append(" ").append(total)
                  .append(":</i><br>");
            }
            List<PerkOption> sortedPerks = opts.getOptions().stream()
                    .sorted(Comparator.comparing(this::translatedPerkOptionName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
            for (PerkOption opt : sortedPerks) {
                String name = translatedPerkOptionName(opt);
                final String key = name.trim().toLowerCase(Locale.ROOT);
                if (!shownPerks.add(key)) {
                    continue;
                }
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&bull; ").append(name).append("<br>");
            }
        }
    }

    private void appendCapabilities(StringBuilder sb, T step) {
        if (step.getCapabilityOptions() == null || step.getCapabilityOptions().isEmpty()) {
            return;
        }
        appendSectionBreak(sb);
        sb.append("<br><b>").append(getString(R.string.capabilities)).append(":</b><br>");
        for (CapabilityOptions opts : step.getCapabilityOptions()) {
            if (opts.getOptions() == null || opts.getOptions().isEmpty()) continue;
            final int total = opts.getTotalOptions();
            if (total > 1) {
                sb.append("&nbsp;&nbsp;<i>")
                  .append(safeTranslateTextTag("choose", "Choose")).append(" ").append(total)
                  .append(":</i><br>");
            }
            List<CapabilityOption> sortedCapabilities = opts.getOptions().stream()
                    .sorted(Comparator.comparing(this::translatedCapabilityOptionName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
            for (CapabilityOption opt : sortedCapabilities) {
                String name = translatedCapabilityOptionName(opt);
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&bull; ").append(name).append("<br>");
            }
        }
    }

    private void appendMaterialAwards(StringBuilder sb, T step) {
        if (step.getMaterialAwards() == null || step.getMaterialAwards().isEmpty()) {
            return;
        }
        appendSectionBreak(sb);
        sb.append("<br><b>").append(getString(R.string.material_awards)).append(":</b><br>");
        for (EquipmentOptions opts : step.getMaterialAwards()) {
            if (opts.getOptions() == null || opts.getOptions().isEmpty()) continue;
            final int total = opts.getTotalOptions();
            if (total > 1) {
                sb.append("&nbsp;&nbsp;<i>")
                  .append(safeTranslateTextTag("choose", "Choose")).append(" ").append(total)
                  .append(":</i><br>");
            }
            for (EquipmentOption opt : opts.getOptions()) {
                String name = translatedEquipmentOptionName(opt);
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&bull; ").append(name).append("<br>");
            }
        }
    }

    private void appendSectionBreak(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.append("<br>");
        }
    }

    /** Devuelve el nombre traducido de un PerkOption. */
    protected String translatedPerkOptionName(PerkOption opt) {
        try {
            if (opt.getElement() != null && opt.getElement().getName() != null) {
                return opt.getElement().getName().getTranslatedText();
            }
        } catch (Exception ignored) { /* elemento no resuelto */ }
        if (opt.getName() != null && opt.getName().getTranslatedText() != null
                && !opt.getName().getTranslatedText().isBlank()) {
            return opt.getName().getTranslatedText();
        }
        return opt.getId() != null ? opt.getId() : opt.toString();
    }

    private String translatedCapabilityOptionName(CapabilityOption opt) {
        if (opt.getName() != null && opt.getName().getTranslatedText() != null
                && !opt.getName().getTranslatedText().isBlank()) {
            return opt.getName().getTranslatedText();
        }
        return opt.getId() != null ? opt.getId() : opt.toString();
    }

    /** Devuelve el nombre traducido de un EquipmentOption con cantidad opcional. */
    private String translatedEquipmentOptionName(EquipmentOption opt) {
        String name;
        try {
            if (opt.getElement() != null && opt.getElement().getName() != null) {
                name = opt.getElement().getName().getTranslatedText();
            } else if (opt.getName() != null && opt.getName().getTranslatedText() != null
                    && !opt.getName().getTranslatedText().isBlank()) {
                name = opt.getName().getTranslatedText();
            } else {
                name = opt.getId() != null ? opt.getId() : opt.toString();
            }
        } catch (Exception ignored) {
            name = opt.getId() != null ? opt.getId() : opt.toString();
        }
        if (opt.getQuantity() > 1) {
            name = "x" + opt.getQuantity() + " " + name;
        }
        return name;
    }
}

