/*
 *  Copyright (C) 2020 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.character.occultism;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSelector;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.components.counters.OccultismExtraCounter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.exceptions.InvalidOccultismPowerException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class OccultismFragmentCharacter extends CharacterCustomFragment {
    private OccultismViewModel mViewModel;


    private final Map<OccultismType, TranslatedEditText> occultismLevelFields = new HashMap<>();
    private final Map<OccultismPath, LinearLayout> occultismPathLayout = new HashMap<>();
    private final Map<OccultismPath, Set<ElementSelector<OccultismPower>>> selectors = new HashMap<>();
    private boolean enabled = true;
    private OccultismExtraCounter extraCounter;

    private View root;

    public static OccultismFragmentCharacter newInstance(int index) {
        final OccultismFragmentCharacter fragment = new OccultismFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        if (getContext() != null) {
            if (extraCounter != null) {
                extraCounter.setCharacter(character);
            }
            try {
                for (OccultismType occultismType : OccultismTypeFactory.getInstance().getElements()) {
                    if (occultismLevelFields.get(occultismType) != null) {
                        occultismLevelFields.get(occultismType).setText(String.valueOf(character.getOccultismLevel(occultismType)));
                    }
                }
            } catch (InvalidXmlElementException e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
            }
            enabled = false;
            selectors.values().forEach(elementSelectors -> elementSelectors.forEach(occultismPowerElementSelector ->
                    occultismPowerElementSelector.setChecked(character.hasOccultismPower(occultismPowerElementSelector.getSelection()))));
            enabled = true;

            updateContent();
        }
    }

    private void updateContent() {
        enableOccultismPowers();
        updateVisibility();
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addContent(rootLayout, CharacterManager.getSelectedCharacter());
        updateContent();

        populateElements(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null) {
            final LinearLayout linearLayout = root.findViewById(R.id.root_container);
            linearLayout.removeAllViews();
            addContent(linearLayout, characterPlayer);
            populateElements(root, characterPlayer);
        }
    }

    private void addContent(LinearLayout rootLayout, CharacterPlayer characterPlayer) {
        addSection(ThinkMachineTranslator.getTranslatedText("occultism"), rootLayout);
        createOccultismInfo(rootLayout, OccultismTypeFactory.getPsi());
        createOccultismInfo(rootLayout, OccultismTypeFactory.getTheurgy());

        setOccultismPaths(rootLayout, characterPlayer);
    }

    private void setOccultismPaths(LinearLayout rootLayout, CharacterPlayer characterPlayer) {
        List<OccultismPath> occultismPaths = mViewModel.getAvailableOccultismPath(!characterPlayer.getSettings().isOnlyOfficialAllowed());
        occultismPaths.stream().sorted(
                        Comparator.comparing(OccultismPath::getOccultismType).thenComparing(OccultismPath::getName))
                .forEach(
                        occultismPath -> setOccultismPathLayout(occultismPath, rootLayout)
                );
    }

    private void setOccultismPathLayout(OccultismPath occultismPath, LinearLayout rootLayout) {
        LinearLayout occultismLayout = new LinearLayout(getContext());
        occultismLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        occultismLayout.setOrientation(LinearLayout.VERTICAL);
        addSection(occultismPath.getName().getTranslatedText(), occultismLayout);
        selectors.put(occultismPath, new HashSet<>());
        occultismPath.getOccultismPowers().values().stream().sorted(
                Comparator.comparing(OccultismPower::getOccultismLevel).thenComparing(OccultismPower::getName)).forEach(occultismPower -> {
            final ElementSelector<OccultismPower> occultismPowerSelector = new ElementSelector<>(getContext(), occultismPower);
            occultismLayout.addView(occultismPowerSelector);
            occultismPowerSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (enabled) {
                    if (isChecked) {
                        try {
                            CharacterManager.getSelectedCharacter().addOccultismPower(occultismPowerSelector.getSelection());
                        } catch (InvalidOccultismPowerException e) {
                            occultismPowerSelector.setChecked(false);
                        } catch (UnofficialElementNotAllowedException e) {
                            SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                            occultismPowerSelector.setChecked(false);
                        }
                    } else {
                        CharacterManager.getSelectedCharacter().removeOccultismPower(occultismPowerSelector.getSelection());
                    }
                    enableOccultismPowers(occultismPath);
                }
                updateVisibility();
            });
            if (selectors.get(occultismPath) != null) {
                selectors.get(occultismPath).add(occultismPowerSelector);
            }
        });
        addSpace(occultismLayout);
        rootLayout.addView(occultismLayout);
        occultismPathLayout.put(occultismPath, occultismLayout);
    }

    private void enableOccultismPowers() {
        selectors.keySet().forEach(this::enableOccultismPowers);
    }

    private void enableOccultismPowers(OccultismPath occultismPath) {
        if (selectors.get(occultismPath) != null) {
            selectors.get(occultismPath).forEach(occultismPowerElementSelector ->
                    occultismPowerElementSelector.setEnabled(CharacterManager.getSelectedCharacter().canAddOccultismPower(occultismPowerElementSelector.getSelection())));
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_occultism_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(OccultismViewModel.class);

        extraCounter = root.findViewById(R.id.extra_counter);

        CharacterManager.addCharacterFactionUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterSpecieUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterAgeUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addSelectedCharacterListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterSettingsUpdateListeners(this::updateSettings);

        return root;
    }


    private void updateVisibility() {
        final OccultismType characterOccultismType = CharacterManager.getSelectedCharacter().getOccultismType();
        occultismLevelFields.forEach((key, value) -> {
            if (characterOccultismType == null || Objects.equals(key, characterOccultismType)) {
                value.setVisibility(View.VISIBLE);
            } else {
                value.setVisibility(View.GONE);
            }
        });
        occultismPathLayout.forEach((key, value) -> {
            if (characterOccultismType == null || Objects.equals(key.getOccultismType(), characterOccultismType.getId())) {
                value.setVisibility(View.VISIBLE);
            } else {
                value.setVisibility(View.GONE);
            }
        });
    }

    private void createOccultismInfo(LinearLayout linearLayout, OccultismType occultismType) {
        final TranslatedEditText occultismLevelText = new TranslatedEditText(getContext(), null);
        occultismLevelFields.put(occultismType, occultismLevelText);

        occultismLevelText.setLabel(ThinkMachineTranslator.getTranslatedText(occultismType.getId()));
        occultismLevelText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        occultismLevelText.setPadding(50, 20, 20, 20);

        // Add EditText to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(occultismLevelText);
        }

        occultismLevelText.setFocusable(false);

        occultismLevelText.setText(String.valueOf(CharacterManager.getSelectedCharacter().getOccultismLevel(occultismType)));
        CharacterManager.addCharacterCharacteristicsUpdatedListener(characterPlayer ->
                occultismLevelText.setText(String.valueOf(characterPlayer.getOccultismLevel(occultismType))));
        CharacterManager.addCharacterSpecieUpdatedListener(characterPlayer ->
                occultismLevelText.setText(String.valueOf(characterPlayer.getOccultismLevel(occultismType))));
        CharacterManager.addCharacterFactionUpdatedListener(characterPlayer ->
                occultismLevelText.setText(String.valueOf(characterPlayer.getOccultismLevel(occultismType))));
        CharacterManager.addCharacterCallingUpdatedListener(characterPlayer ->
                occultismLevelText.setText(String.valueOf(characterPlayer.getOccultismLevel(occultismType))));
    }

}
