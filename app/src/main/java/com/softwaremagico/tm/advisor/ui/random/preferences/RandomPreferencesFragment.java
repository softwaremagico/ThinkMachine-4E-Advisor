/*
 *  Copyright (C) 2026 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.random.preferences;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.random.PreferenceGroup;
import com.softwaremagico.tm.advisor.core.random.PreferenceOption;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomPreferencesFragment extends CharacterCustomFragment {
    private View root;
    private final Set<EnumSpinner> optionsAvailable = new HashSet<>();

    public static RandomPreferencesFragment newInstance(int index) {
        final RandomPreferencesFragment fragment = new RandomPreferencesFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        final LinearLayout linearLayout = root.findViewById(R.id.preferences_container);
        if (linearLayout == null) {
            return;
        }
        for (PreferenceGroup preferenceGroup : PreferenceGroup.values()) {
            try {
                addSection(getResources().getString(getResources().getIdentifier(getGroupPreferenceStringResource(preferenceGroup), "string",
                        getContext().getPackageName())), linearLayout);
                for (PreferenceOption preferenceOption : preferenceGroup.getOptions()) {
                    final EnumSpinner optionsSelector = new EnumSpinner(getContext(), null);
                    final List<IRandomPreference> options = new ArrayList<>(Arrays.asList(preferenceOption.getRandomPreferences()));
                    if (preferenceOption.getRandomPreferences().length == 0 || preferenceOption.getDefaultOption() == null) {
                        options.add(0, null);
                    }
                    optionsSelector.setAdapter(new RandomEnumAdapter(getActivity(), android.R.layout.simple_spinner_item, options));
                    try {
                        optionsSelector.setText(getResources().getString(getResources().getIdentifier(getPreferenceStringResource(preferenceOption), "string",
                                getContext().getPackageName())));
                    } catch (Resources.NotFoundException e) {
                        AdvisorLog.severe(this.getClass().getName(), "Preference option '" + preferenceOption + "' has no translation '" +
                                getPreferenceStringResource(preferenceOption) + "'.");
                        optionsSelector.setText(getPreferenceStringResource(preferenceOption));
                    }
                    if (preferenceOption.getDefaultOption() != null) {
                        optionsSelector.setSelection(preferenceOption.getDefaultOption());
                    }
                    linearLayout.addView(optionsSelector);
                    optionsAvailable.add(optionsSelector);
                }
                addSpace(linearLayout);
            } catch (Resources.NotFoundException e) {
                AdvisorLog.severe(this.getClass().getName(), "Preference group '" + preferenceGroup.name() + "' has no translation '" +
                        getGroupPreferenceStringResource(preferenceGroup) + "'.");
            }
            addFinalSpace(linearLayout);
        }
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        //No data to update.
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        // Nothing yet.
    }

    public static <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }


    private String getGroupPreferenceStringResource(PreferenceGroup preferenceGroup) {
        return "preference_group_" + preferenceGroup.name().toLowerCase();
    }

    private String getPreferenceStringResource(PreferenceOption preference) {
        return "preference_" + preference.toString().toLowerCase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.random_preferences_fragment, container, false);

        final FloatingActionButton fab = root.findViewById(R.id.random);
        fab.setOnClickListener(view -> {
//            if (CharacterManager.getCostCalculator().getStatus().ordinal() > CharacterProgressionStatus.IN_PROGRESS.ordinal()) {
            if (false) {
                SnackbarGenerator.getWarningMessage(root, R.string.message_random_character_already_finished,
                        R.string.action_new, action -> {
                            // Store settings to redo option if needed.
                            CharacterManager.addNewCharacter();
                            generateCharacter();
                        }).show();
            } else {
                // Store settings to redo option if needed.
                generateCharacter();
            }
        });

        return root;
    }

    private void generateCharacter() {
        try {
            CharacterManager.randomizeCharacter(getSelectedOptions());
            SnackbarGenerator.getInfoMessage(root, R.string.message_random_character_success).show();
        } catch (InvalidXmlElementException | InvalidRandomElementSelectedException e) {
            SnackbarGenerator.getErrorMessage(root, R.string.message_random_character_error).show();
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    public Set<IRandomPreference> getSelectedOptions() {
        Set<IRandomPreference> selectedPreferences = new HashSet<>();
        for (EnumSpinner optionSelected : optionsAvailable) {
            if (optionSelected.getSelectedItem() != null) {
                PreferenceOption optionGroup = PreferenceOption.get(optionSelected.<IRandomPreference>getSelectedItem());
                if (optionGroup != null && optionGroup.getDefaultOption() != optionSelected.<IRandomPreference>getSelectedItem()) {
                    selectedPreferences.add(optionSelected.getSelectedItem());
                }
            }
        }
        return selectedPreferences;
    }


}
