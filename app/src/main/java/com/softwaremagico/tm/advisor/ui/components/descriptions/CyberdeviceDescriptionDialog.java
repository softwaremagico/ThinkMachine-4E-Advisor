package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.values.Bonification;

public class CyberdeviceDescriptionDialog extends ElementDescriptionDialog<Cyberdevice> {

    public CyberdeviceDescriptionDialog(Cyberdevice element) {
        super(element);
    }

    @Override
    protected String getDetails(Cyberdevice cyberdevice) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append(safeTranslateTextTag("techLevel", "Tech Level")).append(": </b>")
                .append(cyberdevice.getTechLevel());
        stringBuilder.append("<br><br>");
        stringBuilder.append("<b>").append(safeTranslateTextTag("benefice", "Benefice")).append(": </b>")
                .append(cyberdevice.getBenefice().getTranslatedText());
        stringBuilder.append("<br><br>");
        stringBuilder.append("<b>").append(safeTranslateTextTag("techCompulsion", "Tech Compulsion")).append(": </b>")
                .append(adaptText(TechCompulsionFactory.getInstance().getElement(cyberdevice.getTechCompulsion()).getNameRepresentation()));

        if (cyberdevice.getSize() != null) {
            stringBuilder.append("<br><br>");
            stringBuilder.append("<b>").append(safeTranslateTextTag("size", "Size")).append(": </b>")
                    .append(cyberdevice.getSize());
        }

        if (cyberdevice.getBonifications() != null && !cyberdevice.getBonifications().isEmpty()) {
            stringBuilder.append("<br><br>");
            stringBuilder.append("<b>").append(safeTranslateTextTag("bonification", "Bonification")).append(": </b>");
            for (Bonification bon : cyberdevice.getBonifications()) {
                stringBuilder.append("<br>&nbsp;&nbsp;");
                if (bon.getValue() != null) {
                    stringBuilder.append(bon.getValue() >= 0 ? "+" : "").append(bon.getValue()).append(" ");
                }
                if (bon.getAffects() != null) {
                    stringBuilder.append(bon.getAffects().toString());
                }
                if (bon.getSituation() != null && !bon.getSituation().isBlank()) {
                    stringBuilder.append(" (").append(adaptText(bon.getSituation())).append(")");
                }
            }
        }

        return stringBuilder.toString();
    }
}
