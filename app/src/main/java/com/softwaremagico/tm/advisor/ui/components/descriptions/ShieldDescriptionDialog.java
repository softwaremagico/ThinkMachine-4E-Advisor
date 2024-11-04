package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.equipment.shields.Shield;

public class ShieldDescriptionDialog extends ElementDescriptionDialog<Shield> {

    public ShieldDescriptionDialog(Shield element) {
        super(element);
    }

    @Override
    protected String getDetails(Shield shield) {
        boolean costLimited = CharacterManager.getSelectedCharacter().getRemainingCash() < shield.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getCashMoney() < shield.getCost();
        return "<table cellpadding=\"" + TABLE_PADDING + "\" style=\"" + TABLE_STYLE + "\">" +
                "<tr>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("techLevel") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("impactForce") + "</th>" +
                "<th>" + ThinkMachineTranslator.getTranslatedText("shieldHits") + "</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"text-align:center\">" + shield.getImpact() + "/" + shield.getForce() + "</td>" +
                "<td style=\"text-align:center\" >" + shield.getHits() + "</td>" +
                "</tr>" +
                "</table>" +
                "<br><b>" + getString(R.string.cost) + "</b> " +
                (costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                        (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : "")) +
                shield.getCost() +
                (costLimited || costProhibited ? "</font>" : "") +
                " " + ThinkMachineTranslator.getTranslatedText("firebirds");
    }
}
