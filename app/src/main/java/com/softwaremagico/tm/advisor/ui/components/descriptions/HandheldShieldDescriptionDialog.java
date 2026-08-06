package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.advisor.ui.character.Numbers;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;

public class HandheldShieldDescriptionDialog extends ElementDescriptionDialog<HandheldShield> {

    public HandheldShieldDescriptionDialog(HandheldShield element) {
        super(element);
    }

    @Override
    protected String getDetails(HandheldShield shield) {
        boolean techLimited = CharacterManager.getSelectedCharacter().getTechLevel() < shield.getTechLevel();
        boolean costLimited = CharacterManager.getSelectedCharacter().getRemainingCash() < shield.getCost();
        boolean costProhibited = CharacterManager.getSelectedCharacter().getCashMoney() < shield.getCost();

        final StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"").append(TABLE_PADDING).append("\" style=\"").append(TABLE_STYLE).append("\">");
        sb.append("<tr>")
                .append("<th>").append(ThinkMachineTranslator.getTranslatedText("techLevel")).append("</th>")
                .append("<th>").append(ThinkMachineTranslator.getTranslatedText("size")).append("</th>")
                .append("</tr>");
        sb.append("<tr>")
                .append("<td style=\"text-align:center\">")
                .append(techLimited ? "<font color=\"" + getColor(R.color.insufficientTechnology) + "\">" : "")
                .append(shield.getTechLevel())
                .append(techLimited ? "</font>" : "")
                .append("</td>")
                .append("<td style=\"text-align:center\">").append(shield.getSize() != null ? shield.getSize() : "").append("</td>")
                .append("</tr>");
        sb.append("</table>");

        sb.append("<br><b>").append(getString(R.string.cost)).append("</b> ")
                .append(costProhibited ? "<font color=\"" + getColor(R.color.unaffordableMoney) + "\">" :
                        (costLimited ? "<font color=\"" + getColor(R.color.insufficientMoney) + "\">" : ""))
                .append(Numbers.PRICE_FORMAT.format(shield.getCost()))
                .append(costLimited || costProhibited ? "</font>" : "")
                .append(" ").append(ThinkMachineTranslator.getTranslatedText("firebirds"));

        return sb.toString();
    }
}
