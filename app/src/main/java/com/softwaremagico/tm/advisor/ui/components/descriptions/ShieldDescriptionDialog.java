package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.Numbers;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.shields.Shield;

public class ShieldDescriptionDialog extends ElementDescriptionDialog<Shield> {

    public ShieldDescriptionDialog(Shield element) {
        super(element);
    }

    @Override
    protected String getDetails(Shield shield) {
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null && areContextualStylesEnabled()) {
            return "<b>No character selected</b>";
        }

        boolean techLimited = selectedCharacter != null && selectedCharacter.getTechLevel() < shield.getTechLevel();
        boolean costLimited = selectedCharacter != null && selectedCharacter.getRemainingCash() < shield.getCost();
        boolean costProhibited = selectedCharacter != null && selectedCharacter.getCashMoney() < shield.getCost();
        return "<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("impactForce") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("shieldHits") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("burnOut") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("distortion") + "</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"text-align:center\">" + wrapWithColorIfEnabled(String.valueOf(shield.getTechLevel()), techLimited, R.color.insufficientTechnology) + "</td>" +
                "<td style=\"text-align:center\">" + shield.getImpact() + "/" + shield.getForce() + "</td>" +
                "<td style=\"text-align:center\" >" + shield.getHits() + "</td>" +
                "<td style=\"text-align:center\" >" + shield.getBurnOut() + "</td>" +
                "<td style=\"text-align:center\" >" + shield.getDistortion() + "</td>" +
                "</tr>" +
                "</table>" +
                "<br><b>" + getString(R.string.cost) + "</b> " +
                wrapWithColorIfEnabled(Numbers.PRICE_FORMAT.format(shield.getCost()), costLimited || costProhibited,
                        costProhibited ? R.color.unaffordableMoney : R.color.insufficientMoney) +
                " " + ThinkMachineTranslator.getTranslatedText("firebirds");
    }
}
