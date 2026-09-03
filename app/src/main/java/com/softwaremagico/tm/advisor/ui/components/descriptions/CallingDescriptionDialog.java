package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.callings.Calling;

public class CallingDescriptionDialog extends CharacterDefinitionStepDescriptionDialog<Calling> {

    public CallingDescriptionDialog(Calling element) {
        super(element);
    }

    @Override
    protected String getDetails(Calling calling) {
        return buildDefinitionStepDetails(calling);
    }
}

