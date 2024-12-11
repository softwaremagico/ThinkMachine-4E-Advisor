package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.Numbers;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.armors.ArmourSpecification;
import com.softwaremagico.tm.character.equipment.armors.ArmourSpecificationFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.resistances.ResistanceType;

public class ArmorDescriptionDialog extends ElementDescriptionDialog<Armor> {

    public ArmorDescriptionDialog(Armor element) {
        super(element);
    }

    @Override
    protected String getDetails(Armor armor) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getTechLevel() <
                armor.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getCashMoney() < armor.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getRemainingCash() < armor.getCost();
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
                armor.getTechLevel() +
                (techLimited ? "</font>" : "") +
                "</td>" +
                "<td style=\"text-align:center\">" + armor.getResistanceValue(ResistanceType.BODY) + "</td>" +
                "<td style=\"text-align:center\">" + (armor.getStandardPenalization() != null ? armor.getStandardPenalization().getDexterityModification() : "")
                + (armor.getSpecialPenalization() != null && armor.getSpecialPenalization().getDexterityModification() != 0 ? "/" +
                armor.getSpecialPenalization().getDexterityModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armor.getStandardPenalization() != null ? armor.getStandardPenalization().getStrengthModification() : "")
                + (armor.getSpecialPenalization() != null && armor.getSpecialPenalization().getStrengthModification() != 0 ? "/" +
                armor.getSpecialPenalization().getStrengthModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armor.getStandardPenalization() != null ? armor.getStandardPenalization().getInitiativeModification() : "")
                + (armor.getSpecialPenalization() != null && armor.getSpecialPenalization().getInitiativeModification() != 0 ? "/" +
                armor.getSpecialPenalization().getInitiativeModification() : "") + "</td>" +
                "<td style=\"text-align:center\">" + (armor.getStandardPenalization() != null ? armor.getStandardPenalization().getEnduranceModification() : "")
                + (armor.getSpecialPenalization() != null && armor.getSpecialPenalization().getEnduranceModification() != 0 ? "/" +
                armor.getSpecialPenalization().getEnduranceModification() : "") + "</td>" +
                "</tr>" +
                "</table>");
        if (!armor.getAllowedShields().isEmpty()) {
            stringBuilder.append("<br><b>").append(ThinkMachineTranslator.getTranslatedText("shield")).append(":</b> ");
            String separator = "";
            for (String shieldName : armor.getAllowedShields()) {
                final Shield shield = ShieldFactory.getInstance().getElement(shieldName);
                stringBuilder.append(separator);
                stringBuilder.append(shield.getName().getTranslatedText());
                separator = ", ";
            }
        }
        if (!armor.getSpecifications().isEmpty()) {
            stringBuilder.append("<br><b>").append(ThinkMachineTranslator.getTranslatedText("othersTable")).append(":</b> ");
            String separator = "";
            for (String specificationName : armor.getSpecifications()) {
                final ArmourSpecification armourSpecification = ArmourSpecificationFactory.getInstance().getElement(specificationName);
                stringBuilder.append(separator);
                stringBuilder.append(armourSpecification.getName().getTranslatedText());
                if (armourSpecification.getDescription() != null && !armourSpecification.getDescription().getTranslatedText().isEmpty()) {
                    stringBuilder.append(" (").append(armourSpecification.getDescription().getTranslatedText()).append(")");
                }
                separator = ", ";
            }
        }
        if (!armor.getDamageTypes().isEmpty()) {
            stringBuilder.append("<br><b>").append(getString(R.string.properties)).append(":</b> ");
            String separator = "";
            for (String damageTypeName : armor.getDamageTypes()) {
                final DamageType damageType = DamageTypeFactory.getInstance().getElement(damageTypeName);
                stringBuilder.append(separator);
                stringBuilder.append(damageType.getName().getTranslatedText());
                separator = ", ";
            }
        }
        stringBuilder.append("<br><b>").append(getString(R.string.cost)).append("</b> ").append(costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                        (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : ""))
                .append(Numbers.PRICE_FORMAT.format(armor.getCost())).append(costLimited || costProhibited ? "</font>" : "").append(" ").append(ThinkMachineTranslator.getTranslatedText("firebirds"));
        return stringBuilder.toString();
    }
}
