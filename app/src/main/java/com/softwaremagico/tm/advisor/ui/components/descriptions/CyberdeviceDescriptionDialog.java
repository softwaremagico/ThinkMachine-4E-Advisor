package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;

public class CyberdeviceDescriptionDialog extends ElementDescriptionDialog<Cyberdevice> {

    public CyberdeviceDescriptionDialog(Cyberdevice element) {
        super(element);
    }

    @Override
    protected String getDetails(Cyberdevice cyberdevice) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("techLevel")).append(": </b>")
                .append(cyberdevice.getTechLevel());
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("benefice")).append(": </b>")
                .append(cyberdevice.getBenefice().getTranslatedText());
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("techCompulsion")).append(": </b>")
                .append(adaptText(TechCompulsionFactory.getInstance().getElement(cyberdevice.getTechCompulsion()).getNameRepresentation()));

        return stringBuilder.toString();
    }
}
