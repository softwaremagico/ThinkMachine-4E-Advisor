package com.softwaremagico.tm.advisor.ui.components.descriptions;

import com.softwaremagico.tm.character.upbringing.Upbringing;

public class UpbringingDescriptionDialog extends CharacterDefinitionStepDescriptionDialog<Upbringing> {

    public UpbringingDescriptionDialog(Upbringing element) {
        super(element);
    }

    @Override
    protected String getDetails(Upbringing upbringing) {
        return buildDefinitionStepDetails(upbringing);
    }
}

