package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.specie.ElementValues;
import com.softwaremagico.tm.character.specie.Specie;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpecieDescriptionDialog extends CharacterDefinitionStepDescriptionDialog<Specie> {

    public SpecieDescriptionDialog(Specie element) {
        super(element);
    }

    @Override
    protected String getDetails(Specie specie) {
        final StringBuilder sb = new StringBuilder();

        // Characteristics initial/max values
        if (specie.getSpecieCharacteristics() != null && !specie.getSpecieCharacteristics().isEmpty()) {
            sb.append("<br><b>").append(getString(com.softwaremagico.tm.advisor.R.string.characteristics)).append(":</b>");
            sb.append("<br><table cellpadding=\"").append(TABLE_PADDING).append("\" style=\"").append(TABLE_STYLE).append("\">");
            sb.append("<tr><th>").append(safeTranslateTextTag("characteristic", "Characteristic")).append("</th>")
                    .append("<th>Init</th><th>Max init</th><th>Max</th></tr>");
            for (ElementValues ev : specie.getSpecieCharacteristics()) {
                String charName;
                try {
                    charName = CharacteristicsDefinitionFactory.getInstance()
                            .getElement(ev.getCharacteristic().name().toLowerCase()).getNameRepresentation();
                } catch (Exception e) {
                    charName = ev.getCharacteristic().name();
                }
                sb.append("<tr><td>").append(charName).append("</td>")
                        .append("<td style=\"text-align:center\">").append(ev.getInitialValue()).append("</td>")
                        .append("<td style=\"text-align:center\">").append(ev.getMaximumInitialValue()).append("</td>")
                        .append("<td style=\"text-align:center\">").append(ev.getMaximumValue()).append("</td>")
                        .append("</tr>");
            }
            sb.append("</table>");
        }

        // Size and vitality
        if (sb.length() > 0) {
            sb.append("<br><br>");
        }
        sb.append("<b>").append(safeTranslateTextTag("size", "Size")).append(":</b> ").append(specie.getSize());
        if (specie.getVitalityBonus() != 0) {
            sb.append("&nbsp;&nbsp;<b>").append(safeTranslateTextTag("vitality", "Vitality")).append(":</b> +")
                    .append(specie.getVitalityBonus());
        }

        // Starting perks
        if (specie.getPerks() != null && !specie.getPerks().isEmpty()) {
            sb.append("<br><br><b>").append(getString(com.softwaremagico.tm.advisor.R.string.perks)).append(":</b>");
            List<PerkOption> sortedPerks = specie.getPerks().stream()
                    .sorted(Comparator.comparing(this::translatedPerkOptionName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
            for (PerkOption perkOption : sortedPerks) {
                sb.append("<br>&nbsp;&nbsp;&bull; ").append(translatedPerkOptionName(perkOption));
            }
        }

        // Common CharacterDefinitionStep fields
        final String commonDetails = buildDefinitionStepDetails(specie);
        if (!commonDetails.isBlank()) {
            sb.append("<br>").append(commonDetails);
        }

        return sb.toString();
    }
}

