package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.TimeFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.skills.SkillFactory;

import java.util.stream.Collectors;

public class OccultismPowerDescriptionDialog extends ElementDescriptionDialog<OccultismPower> {

    public OccultismPowerDescriptionDialog(OccultismPower element) {
        super(element);
    }

    @Override
    protected String getDetails(OccultismPower occultismPower) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableLevel")).append(": </b>")
                .append(occultismPower.getOccultismLevel());
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableTime")).append(": </b>")
                .append(TimeFactory.getInstance().getElement(occultismPower.getTime()).getName().getTranslatedText());
        if (!TimeFactory.getInstance().getElement(occultismPower.getTime()).getDescription().getTranslatedText().isBlank()) {
            stringBuilder.append(" (").append(TimeFactory.getInstance().getElement(occultismPower.getTime()).getDescription().getTranslatedText()).append(")");
        }
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableCost")).append(": </b>")
                .append(occultismPower.getCost().getTranslatedText());
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableRoll")).append(": </b>")
                .append(SkillFactory.getInstance().getElements(occultismPower.getSkills()).stream()
                                .map(skill -> skill.getName().getTranslatedText()).collect(Collectors.joining("/")))
                .append(" + ")
                .append(CharacteristicsDefinitionFactory.getInstance().getElement(occultismPower.getCharacteristic()).getName().getTranslatedText());
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableResistance")).append(": </b>")
                .append(adaptText(occultismPower.getResistance().getTranslatedText()));
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableImpact")).append(": </b>")
                .append(adaptText(occultismPower.getImpact().getTranslatedText()));

        return stringBuilder.toString();
    }
}
