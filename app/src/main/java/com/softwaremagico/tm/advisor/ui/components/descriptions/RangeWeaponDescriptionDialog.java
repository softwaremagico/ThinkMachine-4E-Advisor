package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.Numbers;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;

import java.util.stream.Collectors;

public class RangeWeaponDescriptionDialog extends WeaponDescriptionDialog {

    public RangeWeaponDescriptionDialog(Weapon element) {
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
        final boolean hasAgoraGroups = weapon.getAgoraGroups() != null && !weapon.getAgoraGroups().isEmpty();
        final String weaponFeatures = normalizeWeaponFeatures(weapon.getWeaponOthersText());
        StringBuilder stringBuilder = new StringBuilder("<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">");
        stringBuilder.append("<tr>");
        if (weapon.getWeaponDamages().size() > 1) {
            stringBuilder.append("<th>" + safeTranslateTextTag("weapon", "Weapon") + "</th>");
        }
        stringBuilder.append("<th>" + safeTranslateTextTag("techLevel", "Tech") + "</th>" +
                "<th>" + safeTranslateTextTag("weaponGoal", "Goal") + "</th>" +
                "<th>" + safeTranslateTextTag("weaponDamage", "Damage") + "</th>" +
                "<th>" + safeTranslateTextTag("weaponRange", "Range") + "</th>" +
                "<th>" + safeTranslateTextTag("weaponShots", "Shots") + "</th>" +
                "<th>" + safeTranslateTextTag("weaponRate", "Rate") + "</th>" +
                "<th>" + safeTranslateTextTag("size", "Size") + "</th>" +
                "<th>" + getString(R.string.agora) + "</th>" +
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
                    "<td style=\"text-align:center\" >" + weaponDamage.getRange() + "</td>" +
                    "<td style=\"text-align:center\" >" + (weaponDamage.getShots() != null ? weaponDamage.getShots() : "") + "</td>" +
                    "<td style=\"text-align:center\" >" + weaponDamage.getRate() + "</td>" +
                    "<td style=\"text-align:center\" >" + (weaponDamage.getSize() != null ? weaponDamage.getSize().toString()
                    : (weapon.getSize() != null ? weapon.getSize() : "")) + "</td>" +
                    "</tr>");
        }
        stringBuilder.append("</table>" +
                (!weaponFeatures.isEmpty() ?
                        "<br><b>" + safeTranslateTextTag("weaponsOthers", "Others") + ":</b> " +
                                weaponFeatures : "") +
                "<br><b>" + getString(R.string.cost) + "</b> " +
                wrapWithColorIfEnabled(Numbers.PRICE_FORMAT.format(weapon.getCost()), costLimited || costProhibited,
                        costProhibited ? R.color.unaffordableMoney : R.color.insufficientMoney) +
                " " + safeTranslateTextTag("firebirds", "firebirds") +
                ((weapon.getAgora() != null || hasAgoraGroups)
                        //Title
                        ? "<br><b>" + getString(R.string.agora) + "</b> "
                        //Agora
                        + (weapon.getAgora() != null ? translateAgora(weapon.getAgora()) : "")
                        //Comma separator if both agora and agora group.
                        + (weapon.getAgora() != null && hasAgoraGroups ? ", " : "")
                        //Agora Groups
                        + ((hasAgoraGroups
                        ? weapon.getAgoraGroups().stream().map(this::translateAgoraGroup).collect(Collectors.joining(", "))
                        : ""))
                        : ""));
        return stringBuilder.toString();
    }


}
