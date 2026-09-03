package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.character.TimeFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.skills.SkillFactory;

import java.util.Comparator;
import java.util.stream.Collectors;

public class OccultismPathDescriptionDialog extends ElementDescriptionDialog<OccultismPath> {

    public OccultismPathDescriptionDialog(OccultismPath element) {
        super(element);
    }

    @Override
    protected String getDetails(OccultismPath path) {
        final StringBuilder sb = new StringBuilder();

        // Occultism type
        if (path.getOccultismType() != null) {
            sb.append("<br><b>").append(safeTranslateTextTag("type", "Type")).append(":</b> ")
                    .append(safeTranslateTextTag(path.getOccultismType(), path.getOccultismType()));
        }

        // Factions allowed
        if (path.getFactionsAllowed() != null && !path.getFactionsAllowed().isEmpty()) {
            sb.append("<br><b>").append(getString(com.softwaremagico.tm.advisor.R.string.faction)).append(":</b> ");
            sb.append(path.getFactionsAllowed().stream()
                    .map(id -> {
                        try {
                            return FactionFactory.getInstance().getElement(id).getNameRepresentation();
                        } catch (Exception e) {
                            return id;
                        }
                    })
                    .collect(Collectors.joining(", ")));
        }

        // Powers with full details
        if (path.getOccultismPowers() != null && !path.getOccultismPowers().isEmpty()) {
            sb.append("<br><br><b>").append(safeTranslateTextTag("occultismTablePowers", "Powers")).append(":</b>");
            path.getOccultismPowers().values().stream()
                    .sorted(Comparator.comparingInt(OccultismPower::getOccultismLevel)
                            .thenComparing(OccultismPower::getNameRepresentation))
                    .forEach(power -> appendPowerDetails(sb, power));
        }

        return sb.toString();
    }

    private void appendPowerDetails(StringBuilder sb, OccultismPower power) {
        // Header: level + name
        sb.append("<br><b>&bull; ")
          .append(safeTranslateTextTag("occultismTableLevel", "Level")).append(" ")
          .append(power.getOccultismLevel()).append(" — ")
          .append(power.getNameRepresentation())
          .append("</b>");

        // Description
        String desc = power.getDescriptionRepresentation();
        if (desc != null && !desc.isBlank()) {
            sb.append("<br>&nbsp;&nbsp;<i>").append(adaptText(desc)).append("</i>");
        }

        // Time
        try {
            if (power.getTime() != null && !power.getTime().isBlank()) {
                String timeName = TimeFactory.getInstance().getElement(power.getTime())
                        .getName().getTranslatedText();
                sb.append("<br>&nbsp;&nbsp;<b>")
                  .append(safeTranslateTextTag("occultismTableTime", "Time")).append(":</b> ")
                  .append(timeName);
            }
        } catch (Exception ignored) { }

        // Cost
        try {
            if (power.getCost() != null && !power.getCost().getTranslatedText().isBlank()) {
                sb.append(" &nbsp; <b>")
                  .append(safeTranslateTextTag("occultismTableCost", "Cost")).append(":</b> ")
                  .append(power.getCost().getTranslatedText());
            }
        } catch (Exception ignored) { }

        // Roll: skills + characteristic
        try {
            String skillStr = SkillFactory.getInstance().getElements(power.getSkills()).stream()
                    .map(s -> s.getName().getTranslatedText())
                    .collect(Collectors.joining("/"));
            String charStr = CharacteristicsDefinitionFactory.getInstance()
                    .getElement(power.getCharacteristic()).getName().getTranslatedText();
            if (!skillStr.isBlank() || !charStr.isBlank()) {
                sb.append("<br>&nbsp;&nbsp;<b>")
                  .append(safeTranslateTextTag("occultismTableRoll", "Roll")).append(":</b> ")
                  .append(skillStr).append(" + ").append(charStr);
            }
        } catch (Exception ignored) { }

        // Resistance
        try {
            String resistance = power.getResistance().getTranslatedText();
            if (!resistance.isBlank()) {
                sb.append("<br>&nbsp;&nbsp;<b>")
                  .append(safeTranslateTextTag("occultismTableResistance", "Resistance")).append(":</b> ")
                  .append(adaptText(resistance));
            }
        } catch (Exception ignored) { }

        // Impact
        try {
            String impact = power.getImpact().getTranslatedText();
            if (!impact.isBlank()) {
                sb.append("<br>&nbsp;&nbsp;<b>")
                  .append(safeTranslateTextTag("occultismTableImpact", "Impact")).append(":</b> ")
                  .append(adaptText(impact));
            }
        } catch (Exception ignored) { }

        sb.append("<br>");
    }
}

