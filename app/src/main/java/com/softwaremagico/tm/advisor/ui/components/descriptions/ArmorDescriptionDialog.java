package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.Numbers;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.armors.ArmourSpecification;
import com.softwaremagico.tm.character.equipment.armors.ArmourSpecificationFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;

public class ArmorDescriptionDialog extends ElementDescriptionDialog<Armor> {

    public ArmorDescriptionDialog(Armor element) {
        super(element);
    }

    @Override
    protected String getDetails(Armor armour) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getTechLevel() <
                armour.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getCashMoney() < armour.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getRemainingCash() < armour.getCost();
        StringBuilder stringBuilder = new StringBuilder("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("armorRating") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("dexterity") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("strength") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("initiative") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("endurance") + "</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"text-align:center\">" +
                (techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "") +
                armour.getTechLevel() +
                (techLimited ? "</font>" : "") +
                "</td>" +
                "<td style=\"text-align:center\">" + armour.getProtection() + "</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getDexterityModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getDexterityModification() != 0 ? "/" +
                armour.getSpecialPenalization().getDexterityModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getStrengthModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getStrengthModification() != 0 ? "/" +
                armour.getSpecialPenalization().getStrengthModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getInitiativeModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getInitiativeModification() != 0 ? "/" +
                armour.getSpecialPenalization().getInitiativeModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armour.getStandardPenalization() != null ? armour.getStandardPenalization().getEnduranceModification() : "")
                + (armour.getSpecialPenalization() != null && armour.getSpecialPenalization().getEnduranceModification() != 0 ? "/" +
                armour.getSpecialPenalization().getEnduranceModification() : "") + "</td>" +
                "</tr>" +
                "</table>");
        if (!armour.getAllowedShields().isEmpty()) {
            stringBuilder.append("<br><b>").append(ThinkMachineTranslator.getTranslatedText("shield")).append(":</b> ");
            String separator = "";
            for (String shieldName : armour.getAllowedShields()) {
                final Shield shield = ShieldFactory.getInstance().getElement(shieldName);
                stringBuilder.append(separator);
                stringBuilder.append(shield.getName().getTranslatedText());
                separator = ", ";
            }
        }
        if (!armour.getSpecifications().isEmpty()) {
            stringBuilder.append("<br><b>").append(ThinkMachineTranslator.getTranslatedText("othersTable")).append(":</b> ");
            String separator = "";
            for (String specificationName : armour.getSpecifications()) {
                final ArmourSpecification armourSpecification = ArmourSpecificationFactory.getInstance().getElement(specificationName);
                stringBuilder.append(separator);
                stringBuilder.append(armourSpecification.getName().getTranslatedText());
                if (armourSpecification.getDescription() != null && !armourSpecification.getDescription().getTranslatedText().isEmpty()) {
                    stringBuilder.append(" (").append(armourSpecification.getDescription().getTranslatedText()).append(")");
                }
                separator = ", ";
            }
        }
        if (!armour.getDamageTypes().isEmpty()) {
            stringBuilder.append("<br><b>").append(getString(R.string.properties)).append(":</b> ");
            String separator = "";
            for (String damageTypeName : armour.getDamageTypes()) {
                final DamageType damageType = DamageTypeFactory.getInstance().getElement(damageTypeName);
                stringBuilder.append(separator);
                stringBuilder.append(damageType.getName().getTranslatedText());
                separator = ", ";
            }
        }
        stringBuilder.append("<br><b>").append(getString(R.string.cost)).append("</b> ").append(costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : ""))
                .append(Numbers.PRICE_FORMAT.format(armour.getCost())).append(costLimited || costProhibited ? "</font>" : "").append(" ").append(ThinkMachineTranslator.getTranslatedText("firebirds"));
        return stringBuilder.toString();
    }
}
