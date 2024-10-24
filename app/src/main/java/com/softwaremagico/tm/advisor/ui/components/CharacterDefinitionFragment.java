package com.softwaremagico.tm.advisor.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.R;
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOption;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.skills.SkillBonusOption;

import java.util.ArrayList;

public abstract class CharacterDefinitionFragment<T extends CharacterDefinitionStep<T>> extends CharacterCustomFragment {

    private OptionSelectorLayout<CapabilityOption> capabilityOptionsLayout;
    private OptionSelectorLayout<CharacteristicBonusOption> characteristicsOptionsLayout;
    private OptionSelectorLayout<SkillBonusOption> skillsOptionsLayout;
    private OptionSelectorLayout<PerkOption> perksOptionsLayout;
    //private OptionSelectorLayout<EquipmentOption> materialAwardsLayout;

    private View root;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_definition_fragment, container, false);
        return root;
    }


    protected void populateElements(View root, T definitionStep) {
        if (getContext() != null) {
            if (capabilityOptionsLayout != null) {
                capabilityOptionsLayout.setElements(new ArrayList<>(definitionStep.getCapabilityOptions()));
            }
            if (characteristicsOptionsLayout != null) {
                characteristicsOptionsLayout.setElements(new ArrayList<>(definitionStep.getCharacteristicOptions()));
            }
            if (skillsOptionsLayout != null) {
                skillsOptionsLayout.setElements(new ArrayList<>(definitionStep.getSkillOptions()));
            }
            if (perksOptionsLayout != null) {
                perksOptionsLayout.setElements(new ArrayList<>(definitionStep.getPerksOptions()));
            }
        }
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {

    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(getString(R.string.capabilities), rootLayout);
        capabilityOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(capabilityOptionsLayout);
        addSpace(rootLayout);

        addSection(getString(R.string.characteristics), rootLayout);
        characteristicsOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(characteristicsOptionsLayout);
        addSpace(rootLayout);

        addSection(getString(R.string.skills), rootLayout);
        skillsOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(skillsOptionsLayout);
        addSpace(rootLayout);

        addSection(getString(R.string.perks), rootLayout);
        perksOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(perksOptionsLayout);
        addSpace(rootLayout);
    }
}
