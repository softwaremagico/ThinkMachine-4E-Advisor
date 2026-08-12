package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.Numbers;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;

public class MeleeWeaponDescriptionDialog extends WeaponDescriptionDialog {

    public MeleeWeaponDescriptionDialog(Weapon element) {
        super(element);
    }

    @Override
    protected String getDetails(Weapon weapon) {
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null && areContextualStylesEnabled()) {
            return "<b>No character selected</b>";
        }

        boolean techLimited = selectedCharacter != null && selectedCharacter.getTechLevel() < weapon.getTechLevel();
        boolean costLimited = selectedCharacter != null && selectedCharacter.getRemainingCash() < weapon.getCost();
        boolean costProhibited = selectedCharacter != null && selectedCharacter.getCashMoney() < weapon.getCost();
        StringBuilder stringBuilder = new StringBuilder("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">");
        stringBuilder.append("<tr>");
        if (weapon.getWeaponDamages().size() > 1) {
            stringBuilder.append("<th>" + ThinkMachineTranslator.getTranslatedText("weapon") + "</th>");
        }
        stringBuilder.append("<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponGoal") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponDamage") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("weaponStrength") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("size") + "</th>" +
                "</tr>");
        for (WeaponDamage weaponDamage : weapon.getWeaponDamages()) {
            boolean techDamageLimited = selectedCharacter != null && weaponDamage.getDamageTechLevel() != null &&
                    selectedCharacter.getTechLevel() < weaponDamage.getDamageTechLevel();
            stringBuilder.append("<tr>");
            if (weapon.getWeaponDamages().size() > 1) {
                stringBuilder.append("<td style=\"text-align:center\">" + (weaponDamage.getName() != null && weaponDamage.getName().getTranslatedText() != null
                        ? weaponDamage.getName().getTranslatedText() : weapon.getName().getTranslatedText()) + "</td>");
            }
            stringBuilder.append("<td style=\"text-align:center\">" +
                    wrapWithColorIfEnabled(String.valueOf(weaponDamage.getDamageTechLevel() == null ? weapon.getTechLevel() : weaponDamage.getDamageTechLevel()),
                            techLimited || techDamageLimited, R.color.insufficientTechnology) +
                    "</td>" +
                    "<td style=\"text-align:center\">" + weaponDamage.getGoal() + "</td>" +
                    "<td style=\"text-align:center\" >" + getDamage(weaponDamage) + "</td>" +
                    "<td style=\"text-align:center\" >" + weaponDamage.getStrength() + "</td>" +
                    "<td style=\"text-align:center\" >" + (weapon.getSize() != null ? weapon.getSize().toString() : "") + "</td>" +
                    "</tr>");
        }
        final String weaponFeatures = normalizeWeaponFeatures(weapon.getWeaponOthersText());
        stringBuilder.append(
                "</table>" +
                        (!weaponFeatures.isEmpty() ?
                                "<br><b>" + ThinkMachineTranslator.getTranslatedText("weaponsOthers") + ":</b> " +
                                        weaponFeatures : "") +
                        "<br><b>" + getString(R.string.cost) + "</b> " +
                        wrapWithColorIfEnabled(Numbers.PRICE_FORMAT.format(weapon.getCost()), costLimited || costProhibited,
                                costProhibited ? R.color.unaffordableMoney : R.color.insufficientMoney) +
                        " " + ThinkMachineTranslator.getTranslatedText("firebirds"));
        return stringBuilder.toString();
    }
}
