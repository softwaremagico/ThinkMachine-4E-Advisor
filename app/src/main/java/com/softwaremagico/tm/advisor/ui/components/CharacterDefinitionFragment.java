package com.softwaremagico.tm.advisor.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.R;
import com.softwaremagico.tm.advisor.ui.character.upbringing.CharacterDefinitionStepModel;
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillBonusOption;

import java.util.ArrayList;

public abstract class CharacterDefinitionFragment<T extends CharacterDefinitionStep<T>> extends CharacterCustomFragment {
    protected OptionSelectorLayout<Capability, CapabilityOption> capabilityOptionsLayout;
    protected OptionSelectorLayout<CharacteristicDefinition, CharacteristicBonusOption> characteristicsOptionsLayout;
    protected OptionSelectorLayout<Skill, SkillBonusOption> skillsOptionsLayout;
    protected OptionSelectorLayout<Perk, PerkOption> perksOptionsLayout;
    //protected OptionSelectorLayout<EquipmentOption> materialAwardsLayout;

    private CharacterDefinitionStepModel mViewModel;

    private View root;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_definition_fragment, container, false);
        return root;
    }

    protected void setCharacterDefinitionStepModel(CharacterDefinitionStepModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    protected void populateElements(View root, T definitionStep, CharacterDefinitionStepSelection characterDefinitionStepSelection) {
        lazyInitData();
        if (getContext() != null && definitionStep != null && characterDefinitionStepSelection != null && mViewModel.getCharacterPlayer() != null) {
            if (capabilityOptionsLayout != null) {
                capabilityOptionsLayout.setElements(CapabilityOption.class, new ArrayList<>(definitionStep.getCapabilityOptions()),
                        characterDefinitionStepSelection.getCapabilityOptions(), mViewModel.getCharacterPlayer());
            }
            if (characteristicsOptionsLayout != null) {
                characteristicsOptionsLayout.setElements(CharacteristicBonusOption.class, new ArrayList<>(definitionStep.getCharacteristicOptions()),
                        characterDefinitionStepSelection.getCharacteristicOptions(), mViewModel.getCharacterPlayer());
            }
            if (skillsOptionsLayout != null) {
                skillsOptionsLayout.setElements(SkillBonusOption.class, new ArrayList<>(definitionStep.getSkillOptions()),
                        characterDefinitionStepSelection.getSkillOptions(), mViewModel.getCharacterPlayer());
            }
            if (perksOptionsLayout != null) {
                perksOptionsLayout.setElements(PerkOption.class, new ArrayList<>(definitionStep.getPerksOptions()),
                        characterDefinitionStepSelection.getPerksOptions(), mViewModel.getCharacterPlayer());
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
