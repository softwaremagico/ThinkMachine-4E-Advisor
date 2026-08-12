package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.CharacterPlayer;
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
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null && areContextualStylesEnabled()) {
            return "<b>No character selected</b>";
        }
        boolean techLimited = selectedCharacter != null && selectedCharacter.getTechLevel() < shield.getTechLevel();
        boolean costLimited = selectedCharacter != null && selectedCharacter.getRemainingCash() < shield.getCost();
        boolean costProhibited = selectedCharacter != null && selectedCharacter.getCashMoney() < shield.getCost();

        final StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"").append(TABLE_PADDING).append("\" style=\"").append(TABLE_STYLE).append("\">");
        sb.append("<tr>")
                .append("<th>").append(ThinkMachineTranslator.getTranslatedText("techLevel")).append("</th>")
                .append("<th>").append(ThinkMachineTranslator.getTranslatedText("size")).append("</th>")
                .append("</tr>");
        sb.append("<tr>")
                .append("<td style=\"text-align:center\">")
                .append(wrapWithColorIfEnabled(String.valueOf(shield.getTechLevel()), techLimited, R.color.insufficientTechnology))
                .append("</td>")
                .append("<td style=\"text-align:center\">").append(shield.getSize() != null ? shield.getSize() : "").append("</td>")
                .append("</tr>");
        sb.append("</table>");

        sb.append("<br><b>").append(getString(R.string.cost)).append("</b> ")
                .append(wrapWithColorIfEnabled(Numbers.PRICE_FORMAT.format(shield.getCost()), costLimited || costProhibited,
                        costProhibited ? R.color.unaffordableMoney : R.color.insufficientMoney))
                .append(" ").append(ThinkMachineTranslator.getTranslatedText("firebirds"));

        return sb.toString();
    }
}
