package com.softwaremagico.tm.advisor.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.R;
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.EquipmentOption;
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

    protected OptionSelectorLayout<Equipment, EquipmentOption> materialAwardsLayout;

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
            if (materialAwardsLayout != null) {
                materialAwardsLayout.setElements(EquipmentOption.class, new ArrayList<>(definitionStep.getMaterialAwards()),
                        new ArrayList<>(characterDefinitionStepSelection.getMaterialAwards()), mViewModel.getCharacterPlayer());
            }
        }
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {

    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        final TextView capabilitiesTitle = addSection(getString(R.string.capabilities), rootLayout);
        capabilityOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(capabilityOptionsLayout);
        View capabilitiesSpace = addSpace(rootLayout);
        capabilityOptionsLayout.addElementsSizeUpdatedListener(size -> {
            if (size == 0) {
                capabilitiesTitle.setVisibility(View.INVISIBLE);
                capabilitiesSpace.setVisibility(View.INVISIBLE);
            } else {
                capabilitiesTitle.setVisibility(View.VISIBLE);
                capabilitiesSpace.setVisibility(View.VISIBLE);
            }
        });
        capabilitiesTitle.setVisibility(View.INVISIBLE);
        capabilitiesSpace.setVisibility(View.INVISIBLE);


        final TextView characteristicsTitle = addSection(getString(R.string.characteristics), rootLayout);
        characteristicsOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(characteristicsOptionsLayout);
        View characteristicsSpace = addSpace(rootLayout);
        characteristicsOptionsLayout.addElementsSizeUpdatedListener(size -> {
            if (size == 0) {
                characteristicsTitle.setVisibility(View.INVISIBLE);
                characteristicsSpace.setVisibility(View.INVISIBLE);
            } else {
                characteristicsTitle.setVisibility(View.VISIBLE);
                characteristicsSpace.setVisibility(View.VISIBLE);
            }
        });
        characteristicsTitle.setVisibility(View.INVISIBLE);
        characteristicsSpace.setVisibility(View.INVISIBLE);


        final TextView skillsTitle = addSection(getString(R.string.skills), rootLayout);
        skillsOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(skillsOptionsLayout);
        View skillsSpace = addSpace(rootLayout);
        skillsOptionsLayout.addElementsSizeUpdatedListener(size -> {
            if (size == 0) {
                skillsTitle.setVisibility(View.INVISIBLE);
                skillsSpace.setVisibility(View.INVISIBLE);
            } else {
                skillsTitle.setVisibility(View.VISIBLE);
                skillsSpace.setVisibility(View.VISIBLE);
            }
        });
        skillsTitle.setVisibility(View.INVISIBLE);
        skillsSpace.setVisibility(View.INVISIBLE);


        final TextView perksTitle = addSection(getString(R.string.perks), rootLayout);
        perksOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(perksOptionsLayout);
        View perksSpace = addSpace(rootLayout);
        perksOptionsLayout.addElementsSizeUpdatedListener(size -> {
            if (size == 0) {
                perksTitle.setVisibility(View.INVISIBLE);
                perksSpace.setVisibility(View.INVISIBLE);
            } else {
                perksTitle.setVisibility(View.VISIBLE);
                perksSpace.setVisibility(View.VISIBLE);
            }
        });
        perksTitle.setVisibility(View.INVISIBLE);
        perksSpace.setVisibility(View.INVISIBLE);


        final TextView materialAwardsTitle = addSection(getString(R.string.material_awards), rootLayout);
        materialAwardsLayout = new OptionSelectorLayout<>(getContext(), null);
        rootLayout.addView(materialAwardsLayout);
        View materialAwardsSpace = addSpace(rootLayout);
        materialAwardsLayout.addElementsSizeUpdatedListener(size -> {
            if (size == 0) {
                materialAwardsTitle.setVisibility(View.INVISIBLE);
                materialAwardsSpace.setVisibility(View.INVISIBLE);
            } else {
                materialAwardsTitle.setVisibility(View.VISIBLE);
                materialAwardsSpace.setVisibility(View.VISIBLE);
            }
        });
        materialAwardsTitle.setVisibility(View.INVISIBLE);
        materialAwardsSpace.setVisibility(View.INVISIBLE);
    }
}
