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

package com.softwaremagico.tm.advisor.ui.character.cybernetics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSelector;
import com.softwaremagico.tm.advisor.ui.components.counters.CyberneticsExtraCounter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.exceptions.InvalidCyberdeviceException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CyberneticsFragmentCharacter extends CharacterCustomFragment {
    private CyberneticsViewModel mViewModel;

    private final List<ElementSelector<Cyberdevice>> selectors = new ArrayList<>();
    private boolean enabled = true;
    private CyberneticsExtraCounter extraCounter;

    private View root;

    public static CyberneticsFragmentCharacter newInstance(int index) {
        final CyberneticsFragmentCharacter fragment = new CyberneticsFragmentCharacter();
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
            enabled = false;
            selectors.forEach(cyberdeviceElementSelector ->
                    cyberdeviceElementSelector.setChecked(character.hasDevice(cyberdeviceElementSelector.getSelection())));
            enabled = true;

            updateContent(character);
        }
    }

    private void updateContent(CharacterPlayer characterPlayer) {
        enableCyberdevices(characterPlayer);
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addContent(rootLayout, CharacterManager.getSelectedCharacter());
        updateContent(CharacterManager.getSelectedCharacter());

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
        addSection(ThinkMachineTranslator.getTranslatedText("cyberdevices"), rootLayout);
        addSpace(rootLayout);
        setCyberdevices(rootLayout, characterPlayer);
    }

    private void setCyberdevices(LinearLayout rootLayout, CharacterPlayer characterPlayer) {

        LinearLayout cyberdevicesLayout = new LinearLayout(getContext());
        cyberdevicesLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        cyberdevicesLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(cyberdevicesLayout);

        final List<Cyberdevice> cyberdevices = mViewModel.getAvailableCyberdevices(!characterPlayer.getSettings().isOnlyOfficialAllowed());
        cyberdevices.stream().sorted(
                        Comparator.comparing(Cyberdevice::getNameRepresentation))
                .forEach(
                        cyberdevice -> addCyberdeviceToLayout(characterPlayer, cyberdevice, cyberdevicesLayout)
                );
    }

    private void addCyberdeviceToLayout(CharacterPlayer characterPlayer, Cyberdevice cyberdevice, LinearLayout cyberdevicesLayout) {
        final ElementSelector<Cyberdevice> cyberDeviceSelector = new ElementSelector<>(getContext(), cyberdevice);
        cyberdevicesLayout.addView(cyberDeviceSelector);
        cyberDeviceSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (enabled) {
                if (isChecked) {
                    try {
                        CharacterManager.getSelectedCharacter().addCyberdevice(cyberDeviceSelector.getSelection());
                    } catch (InvalidCyberdeviceException e) {
                        cyberDeviceSelector.setChecked(false);
                    } catch (UnofficialElementNotAllowedException e) {
                        SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                        cyberDeviceSelector.setChecked(false);
                    }
                } else {
                    CharacterManager.getSelectedCharacter().removeCyberdevice(cyberDeviceSelector.getSelection());
                }
                enableCyberdevices(characterPlayer, cyberDeviceSelector);
            }
        });
        if (!selectors.contains(cyberDeviceSelector)) {
            selectors.add(cyberDeviceSelector);
        }
    }

    private void enableCyberdevices(CharacterPlayer characterPlayer) {
        selectors.forEach(selector -> enableCyberdevices(characterPlayer, selector));
    }

    private void enableCyberdevices(CharacterPlayer characterPlayer, ElementSelector<Cyberdevice> cyberdeviceSelector) {
        if (cyberdeviceSelector != null) {
            try {
                cyberdeviceSelector.setEnabled(cyberdeviceSelector.isChecked() || characterPlayer.canAddCyberdevice(cyberdeviceSelector.getSelection()));
            } catch (InvalidCyberdeviceException e) {
                cyberdeviceSelector.setEnabled(false);
            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_cybernetics_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CyberneticsViewModel.class);

        extraCounter = root.findViewById(R.id.extra_counter);

        CharacterManager.addCharacterFactionUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterSpecieUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterAgeUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addSelectedCharacterListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterSettingsUpdateListeners(this::updateSettings);

        return root;
    }

}
