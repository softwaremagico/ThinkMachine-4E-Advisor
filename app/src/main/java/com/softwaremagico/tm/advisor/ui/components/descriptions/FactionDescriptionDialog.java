package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.callings.CallingFactory;

import java.util.stream.Collectors;

public class FactionDescriptionDialog extends CharacterDefinitionStepDescriptionDialog<Faction> {

    public FactionDescriptionDialog(Faction element) {
        super(element);
    }

    @Override
    protected String getDetails(Faction faction) {
        final StringBuilder sb = new StringBuilder();

        // Blessed / Curse
        if (faction.getBlessing() != null) {
            sb.append("<br><b>").append(safeTranslateTextTag("blessing", "Blessing")).append(":</b> ")
                    .append(faction.getBlessing().getNameRepresentation());
            if (faction.getBlessing().getBenefice() != null) {
                sb.append(" — ").append(adaptText(faction.getBlessing().getBenefice().getTranslatedText()));
            }
        }
        if (faction.getCurse() != null) {
            sb.append("<br><b>").append(safeTranslateTextTag("curse", "Curse")).append(":</b> ")
                    .append(faction.getCurse().getNameRepresentation());
            if (faction.getCurse().getBenefice() != null) {
                sb.append(" — ").append(adaptText(faction.getCurse().getBenefice().getTranslatedText()));
            }
        }

        // Favored callings
        if (faction.getFavoredCallings() != null && !faction.getFavoredCallings().isEmpty()) {
            sb.append("<br><b>").append(getString(R.string.callings)).append(":</b> ");
            sb.append(faction.getFavoredCallings().stream()
                    .map(id -> {
                        try {
                            return CallingFactory.getInstance().getElement(id).getNameRepresentation();
                        } catch (Exception e) {
                            return id;
                        }
                    })
                    .collect(Collectors.joining(", ")));
        }

        // Common CharacterDefinitionStep fields
        sb.append(buildDefinitionStepDetails(faction));

        return sb.toString();
    }
}

