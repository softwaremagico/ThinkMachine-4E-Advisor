package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.specie.ElementValues;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;

public class PerkDescriptionDialog extends ElementDescriptionDialog<Perk> {

    public PerkDescriptionDialog(Perk element) {
        super(element);
    }

    @Override
    protected String getDetails(Perk perk) {
        final StringBuilder sb = new StringBuilder();

        // Benefice text
        if (perk.getBenefice() != null && !perk.getBenefice().getTranslatedText().isBlank()) {
            sb.append("<br><b>").append(safeTranslateTextTag("benefice", "Benefice")).append(":</b> ")
                    .append(adaptText(perk.getBenefice().getTranslatedText()));
        }

        // Type
        if (perk.getType() != null) {
            sb.append("<br><b>").append(safeTranslateTextTag("type", "Type")).append(":</b> ")
                    .append(safeTranslateTextTag(perk.getType().name().toLowerCase(), perk.getType().name()));
        }

        // Characteristic modifiers
        if (perk.getCharacteristicValues() != null && !perk.getCharacteristicValues().isEmpty()) {
            sb.append("<br><b>").append(getString(com.softwaremagico.tm.advisor.R.string.characteristics)).append(":</b>");
            for (ElementValues ev : perk.getCharacteristicValues()) {
                String charName;
                try {
                    charName = CharacteristicsDefinitionFactory.getInstance()
                            .getElement(ev.getCharacteristic().name().toLowerCase()).getNameRepresentation();
                } catch (Exception e) {
                    charName = ev.getCharacteristic().name();
                }
                sb.append("<br>&nbsp;&nbsp;").append(charName)
                        .append(" init: ").append(ev.getInitialValue())
                        .append(" / max: ").append(ev.getMaximumValue());
            }
        }

        return sb.toString();
    }
}

