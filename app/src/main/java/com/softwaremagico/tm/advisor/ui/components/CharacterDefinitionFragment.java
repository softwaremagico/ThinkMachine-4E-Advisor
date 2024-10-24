package com.softwaremagico.tm.advisor.ui.components;

import android.view.View;
import android.widget.LinearLayout;

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
        rootLayout.addView(capabilityOptionsLayout);
        addSpace(rootLayout);

        addSection(getString(R.string.characteristics), rootLayout);
        rootLayout.addView(characteristicsOptionsLayout);
        addSpace(rootLayout);

        addSection(getString(R.string.skills), rootLayout);
        rootLayout.addView(skillsOptionsLayout);
        addSpace(rootLayout);

        addSection(getString(R.string.perks), rootLayout);
        rootLayout.addView(perksOptionsLayout);
        addSpace(rootLayout);
    }
}
