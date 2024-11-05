package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;

public class MeleeWeaponDescriptionDialog extends WeaponDescriptionDialog {

    public MeleeWeaponDescriptionDialog(Weapon element) {
        super(element);
    }

    @Override
    protected String getDetails(Weapon weapon) {
        boolean costLimited = CharacterManager.getSelectedCharacter().getRemainingCash() < weapon.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getCashMoney() < weapon.getCost();
        StringBuilder stringBuilder = new StringBuilder("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">");
        stringBuilder.append("<tr>");
        if (weapon.getWeaponDamages().size() > 1) {
            stringBuilder.append("<th>" + ThinkMachineTranslator.getTranslatedText("weapon") + "</th>");
        }
        stringBuilder.append(
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponGoal") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponDamage") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponStrength") + "</th>" +
                        "<th>" + ThinkMachineTranslator.getTranslatedText("weaponSize") + "</th>" +
                        "</tr>");
        for (WeaponDamage weaponDamage : weapon.getWeaponDamages()) {
            stringBuilder.append("<tr>");
            if (weapon.getWeaponDamages().size() > 1) {
                stringBuilder.append("<td style=\"text-align:center\">" + (weaponDamage.getName().getTranslatedText() != null
                        ? weaponDamage.getName().getTranslatedText() : weapon.getName().getTranslatedText()) + "</td>");
            }
            stringBuilder.append(
                    "<td style=\"text-align:center\">" + weaponDamage.getGoal() + "</td>" +
                            "<td style=\"text-align:center\" >" + getDamage(weaponDamage) + "</td>" +
                            "<td style=\"text-align:center\" >" + weaponDamage.getStrength() + "</td>" +
                            "<td style=\"text-align:center\" >" + (weapon.getSize() != null ? weapon.getSize().toString() : "") + "</td>" +
                            "</tr>");
        }
        stringBuilder.append(
                "</table>" +
                        (!weapon.getWeaponOthersText().isEmpty() ?
                                "<br><b>" + ThinkMachineTranslator.getTranslatedText("weaponsOthers") + ":</b> " +
                                        weapon.getWeaponOthersText() : "") +
                        "<br><b>" + getString(R.string.cost) + "</b> " +
                        (costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                                (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : "")) +
                        weapon.getCost() +
                        (costLimited || costProhibited ? "</font>" : "") +
                        " " + ThinkMachineTranslator.getTranslatedText("firebirds"));
        return stringBuilder.toString();
    }
}
