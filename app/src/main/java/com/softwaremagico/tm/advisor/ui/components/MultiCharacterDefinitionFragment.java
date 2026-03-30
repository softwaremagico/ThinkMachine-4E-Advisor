package com.softwaremagico.tm.advisor.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
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
import java.util.List;
import java.util.Map;

public abstract class MultiCharacterDefinitionFragment<T extends CharacterDefinitionStep> extends CharacterCustomFragment {
    protected Map<T, OptionSelectorLayout<Capability, CapabilityOption>> capabilityOptionsLayouts;
    protected Map<T, OptionSelectorLayout<CharacteristicDefinition, CharacteristicBonusOption>> characteristicsOptionsLayouts;
    protected Map<T, OptionSelectorLayout<Skill, SkillBonusOption>> skillsOptionsLayouts;
    protected Map<T, OptionSelectorLayout<Perk, PerkOption>> perksOptionsLayouts;
    protected Map<T, OptionSelectorLayout<Equipment, EquipmentOption>> materialAwardsLayouts;

    private CharacterDefinitionStepModel mViewModel;

    private List<T> steps;

    private View root;

    private TextView noDataText;

    private final List<View> elements = new ArrayList<>();

    protected LinearLayout getLayoutContainer() {
        return root.findViewById(R.id.root_container);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_definition_fragment, container, false);
        return root;
    }

    protected void setCharacterDefinitionStepModel(CharacterDefinitionStepModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    protected void populateElements(T definitionStep, CharacterDefinitionStepSelection characterDefinitionStepSelection) {
        lazyInitData();
        initData(definitionStep);

        if (getContext() != null && definitionStep != null && characterDefinitionStepSelection != null && mViewModel.getCharacterPlayer() != null) {
            noDataText.setVisibility(View.INVISIBLE);
            elements.forEach(element -> element.setVisibility(View.VISIBLE));
            if (capabilityOptionsLayouts != null && capabilityOptionsLayouts.get(definitionStep) != null) {
                capabilityOptionsLayouts.get(definitionStep).setElements(CapabilityOption.class, new ArrayList<>(definitionStep.getCapabilityOptions()),
                        characterDefinitionStepSelection.getSelectedCapabilityOptions(), mViewModel.getCharacterPlayer());
            }
            if (characteristicsOptionsLayouts != null && characteristicsOptionsLayouts.get(definitionStep) != null) {
                characteristicsOptionsLayouts.get(definitionStep).setElements(CharacteristicBonusOption.class, new ArrayList<>(definitionStep.getCharacteristicOptions()),
                        characterDefinitionStepSelection.getSelectedCharacteristicOptions(), mViewModel.getCharacterPlayer());
                characteristicsOptionsLayouts.get(definitionStep).addElementsSelectedListener(selections ->
                        CharacterManager.launchCharacterCharacteristicsUpdatedListeners(mViewModel.getCharacterPlayer()));
            }
            if (skillsOptionsLayouts != null && skillsOptionsLayouts.get(definitionStep) != null) {
                skillsOptionsLayouts.get(definitionStep).setElements(SkillBonusOption.class, new ArrayList<>(definitionStep.getSkillOptions()),
                        characterDefinitionStepSelection.getSelectedSkillOptions(), mViewModel.getCharacterPlayer());
            }
            if (perksOptionsLayouts != null && perksOptionsLayouts.get(definitionStep) != null) {
                perksOptionsLayouts.get(definitionStep).setElements(PerkOption.class, new ArrayList<>(definitionStep.getCharacterAvailablePerksOptions()),
                        characterDefinitionStepSelection.getSelectedPerksOptions(), mViewModel.getCharacterPlayer());
                perksOptionsLayouts.get(definitionStep).addElementsSelectedListener(selections ->
                        CharacterManager.launchPerkUpdatedListeners(mViewModel.getCharacterPlayer()));
            }
            if (materialAwardsLayouts != null && materialAwardsLayouts.get(definitionStep) != null) {
                if (definitionStep.getMaterialAwards() != null && !definitionStep.getMaterialAwards().isEmpty() && characterDefinitionStepSelection.getSelectedMaterialAwards() != null) {
                    materialAwardsLayouts.get(definitionStep).setElements(EquipmentOption.class, new ArrayList<>(definitionStep.getMaterialAwards()),
                            new ArrayList<>(characterDefinitionStepSelection.getSelectedMaterialAwards()), mViewModel.getCharacterPlayer());
                } else {
                    materialAwardsLayouts.get(definitionStep).removeAllViews();
                }
            }
        } else {
            noDataText.setVisibility(View.VISIBLE);
            elements.forEach(element -> element.setVisibility(View.INVISIBLE));
        }
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {

    }

    protected List<View> setCapabilitiesElements(OptionSelectorLayout<Capability, CapabilityOption> capabilityOptionsLayout, LinearLayout rootLayout) {
        List<View> elements = new ArrayList<>();
        final TextView capabilitiesTitle = addSection(getString(R.string.capabilities), rootLayout);
        elements.add(capabilitiesTitle);
        elements.add(capabilityOptionsLayout);
        rootLayout.addView(capabilityOptionsLayout);
        View capabilitiesSpace = addSpace(rootLayout);
        elements.add(capabilitiesSpace);
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
        return elements;
    }

    private List<View> setCharacteristicsElements(OptionSelectorLayout<CharacteristicDefinition, CharacteristicBonusOption> characteristicsOptionsLayout, LinearLayout rootLayout) {
        List<View> elements = new ArrayList<>();
        final TextView characteristicsTitle = addSection(getString(R.string.characteristics), rootLayout);
        elements.add(characteristicsTitle);
        elements.add(characteristicsOptionsLayout);
        rootLayout.addView(characteristicsOptionsLayout);
        View characteristicsSpace = addSpace(rootLayout);
        elements.add(characteristicsSpace);
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
        return elements;
    }

    private List<View> setSkillsElements(OptionSelectorLayout<Skill, SkillBonusOption> skillsOptionsLayout, LinearLayout rootLayout) {
        List<View> elements = new ArrayList<>();
        final TextView skillsTitle = addSection(getString(R.string.skills), rootLayout);
        elements.add(skillsTitle);
        elements.add(skillsOptionsLayout);
        rootLayout.addView(skillsOptionsLayout);
        View skillsSpace = addSpace(rootLayout);
        elements.add(skillsSpace);
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
        return elements;
    }

    private List<View> setPerksElements(OptionSelectorLayout<Perk, PerkOption> perksOptionsLayout, LinearLayout rootLayout) {
        List<View> elements = new ArrayList<>();
        final TextView perksTitle = addSection(getString(R.string.perks), rootLayout);
        elements.add(perksTitle);
        elements.add(perksOptionsLayout);
        rootLayout.addView(perksOptionsLayout);
        View perksSpace = addSpace(rootLayout);
        elements.add(perksSpace);
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
        return elements;
    }

    private List<View> setMaterialAwardsElements(OptionSelectorLayout<Equipment, EquipmentOption> materialAwardsLayout, LinearLayout rootLayout) {
        List<View> elements = new ArrayList<>();
        final TextView materialAwardsTitle = addSection(getString(R.string.material_awards), rootLayout);
        elements.add(materialAwardsTitle);
        elements.add(materialAwardsLayout);
        rootLayout.addView(materialAwardsLayout);
        View materialAwardsSpace = addSpace(rootLayout);
        elements.add(materialAwardsSpace);
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
        return elements;
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = getLayoutContainer();
        elements.clear();

        noDataText = noDataText();
        noDataText.setVisibility(View.VISIBLE);
        rootLayout.addView(noDataText);
    }

    protected void initData(T step) {
        final LinearLayout rootLayout = getLayoutContainer();
        OptionSelectorLayout<Capability, CapabilityOption> capabilityOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        capabilityOptionsLayouts.put(step, capabilityOptionsLayout);
        elements.addAll(setCapabilitiesElements(capabilityOptionsLayout, rootLayout));

        OptionSelectorLayout<CharacteristicDefinition, CharacteristicBonusOption> characteristicsOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        characteristicsOptionsLayouts.put(step, characteristicsOptionsLayout);
        elements.addAll(setCharacteristicsElements(characteristicsOptionsLayout, rootLayout));

        OptionSelectorLayout<Skill, SkillBonusOption> skillsOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        skillsOptionsLayouts.put(step, skillsOptionsLayout);
        elements.addAll(setSkillsElements(skillsOptionsLayout, rootLayout));

        OptionSelectorLayout<Perk, PerkOption> perksOptionsLayout = new OptionSelectorLayout<>(getContext(), null);
        perksOptionsLayouts.put(step, perksOptionsLayout);
        elements.addAll(setPerksElements(perksOptionsLayout, rootLayout));

        OptionSelectorLayout<Equipment, EquipmentOption> materialAwardsLayout = new OptionSelectorLayout<>(getContext(), null);
        materialAwardsLayouts.put(step, materialAwardsLayout);
        elements.addAll(setMaterialAwardsElements(materialAwardsLayout, rootLayout));
    }
}
