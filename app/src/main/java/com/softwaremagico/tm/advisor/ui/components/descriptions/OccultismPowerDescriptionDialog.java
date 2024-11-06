package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.TimeFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.skills.SkillFactory;

public class OccultismPowerDescriptionDialog extends ElementDescriptionDialog<OccultismPower> {

    public OccultismPowerDescriptionDialog(OccultismPower element) {
        super(element);
    }

    @Override
    protected String getDetails(OccultismPower occultismPower) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableLevel")).append(":</b>")
                .append(occultismPower.getLevel());
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableTime")).append(":</b>")
                .append(TimeFactory.getInstance().getElement(occultismPower.getTime()).getName().getTranslatedText());
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableCost")).append(":</b>")
                .append(occultismPower.getCost().getTranslatedText());
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableRoll")).append(":</b>")
                .append(SkillFactory.getInstance().getElement(occultismPower.getSkill()).getName().getTranslatedText())
                .append(" + ")
                .append(CharacteristicsDefinitionFactory.getInstance().getElement(occultismPower.getCharacteristic()).getName().getTranslatedText());
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableResistance")).append(":</b>")
                .append(occultismPower.getResistance().getTranslatedText());
        stringBuilder.append("<br>");
        stringBuilder.append("<b>").append(ThinkMachineTranslator.getTranslatedText("occultismTableImpact")).append(":</b>")
                .append(occultismPower.getImpact().getTranslatedText());

        return stringBuilder.toString();
    }
}
