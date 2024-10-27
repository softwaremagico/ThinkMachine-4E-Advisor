/*
 *  Copyright (C) 2024 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.character.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.EnumAdapter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.exceptions.InvalidCallingException;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidUpbringingException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialCharacterException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterInfoFragmentCharacter extends CharacterCustomFragment {
    private CharacterInfoViewModel mViewModel;
    private View root;
    private SwitchCompat nonOfficialEnabled;
    private SwitchCompat restrictionsIgnored;
    private ElementSpinner<Specie> specieSelector;
    private ElementSpinner<Upbringing> upbringingSelector;
    private ElementSpinner<Faction> factionsSelector;
    private ElementSpinner<Calling> callingSelector;
    private ElementSpinner<Planet> planetSelector;
    private boolean updatingCharacter = false;

    public static CharacterInfoFragmentCharacter newInstance(int index) {
        final CharacterInfoFragmentCharacter fragment = new CharacterInfoFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        updateTranslatedTextField(root, R.id.character_name, value -> {
            if (!updatingCharacter) {
                CharacterManager.getSelectedCharacter().getInfo().setNames(value);
            }
        });
        updateTranslatedTextField(root, R.id.character_surname, value -> {
            if (!updatingCharacter) {
                CharacterManager.getSelectedCharacter().getInfo().setSurname(value);
            }
        });
        updateTranslatedTextField(root, R.id.character_age, value -> {
            try {
                if (!Objects.equals(CharacterManager.getSelectedCharacter().getInfo().getAge() + "", value)) {
                    CharacterManager.getSelectedCharacter().getInfo().setAge(Integer.parseInt(value));
                    //Force to update all costs.
                    if (!updatingCharacter) {
                        CharacterManager.launchCharacterAgeUpdatedListeners(CharacterManager.getSelectedCharacter());
                    }
                }
            } catch (NumberFormatException e) {
                CharacterManager.getSelectedCharacter().getInfo().setAge(null);
                CharacterManager.launchCharacterAgeUpdatedListeners(CharacterManager.getSelectedCharacter());
            }

        });
        createGenderSpinner(root);

        populateElements(root, CharacterManager.getSelectedCharacter());

//        ImageView randomNameButton = root.findViewById(R.id.button_random_name);
//        if (randomNameButton != null) {
//            randomNameButton.setOnClickListener(v -> {
//                updatingCharacter = true;
//                CharacterManager.getSelectedCharacter().getInfo().setNames(new ArrayList<>());
//                final RandomName randomName;
//                try {
//                    randomName = new RandomName(CharacterManager.getSelectedCharacter(), null);
//                    randomName.assign();
//                    final TranslatedEditText nameTextEditor = root.findViewById(R.id.character_name);
//                    nameTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getNameRepresentation());
//                } catch (InvalidXmlElementException | InvalidRandomElementSelectedException |
//                         RestrictedElementException e) {
//                    SnackbarGenerator.getErrorMessage(root, R.string.selectFactionAndMore).show();
//                } catch (UnofficialElementNotAllowedException e) {
//                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
//                }
//                updatingCharacter = false;
//            });
//        }
//
//        ImageView randomSurnameButton = root.findViewById(R.id.button_random_surname);
//        if (randomSurnameButton != null) {
//            randomSurnameButton.setOnClickListener(v -> {
//                updatingCharacter = true;
//                CharacterManager.getSelectedCharacter().getInfo().setSurname((Surname) null);
//                final RandomSurname randomSurname;
//                try {
//                    randomSurname = new RandomSurname(CharacterManager.getSelectedCharacter(), null);
//                    randomSurname.assign();
//                    final TranslatedEditText surnameTextEditor = root.findViewById(R.id.character_surname);
//                    if (CharacterManager.getSelectedCharacter().getInfo().getSurname() != null) {
//                        surnameTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getSurname().getName());
//                    } else {
//                        surnameTextEditor.setText("");
//                    }
//                } catch (InvalidXmlElementException | InvalidRandomElementSelectedException |
//                         RestrictedElementException e) {
//                    SnackbarGenerator.getErrorMessage(root, R.string.selectFactionAndMore).show();
//                } catch (UnofficialElementNotAllowedException e) {
//                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
//                }
//                updatingCharacter = false;
//            });
//        }


        nonOfficialEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                try {
                    CharacterManager.getSelectedCharacter().checkIsOfficial();
                    CharacterManager.getSelectedCharacter().getSettings().setOnlyOfficialAllowed(!isChecked);
                    CharacterManager.updateSettings();
                } catch (UnofficialCharacterException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_setting_unofficial_not_changed).show();
                    CharacterManager.getSelectedCharacter().getSettings().setOnlyOfficialAllowed(false);
                    nonOfficialEnabled.setChecked(true);
                }
            } else {
                CharacterManager.getSelectedCharacter().getSettings().setOnlyOfficialAllowed(!isChecked);
            }
        });

        restrictionsIgnored.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                try {
                    CharacterManager.getSelectedCharacter().checkIsNotRestricted();
                    CharacterManager.getSelectedCharacter().getSettings().setRestrictionsChecked(!isChecked);
                    CharacterManager.updateSettings();
                } catch (RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_setting_restriction_not_changed).show();
                    CharacterManager.getSelectedCharacter().getSettings().setRestrictionsChecked(false);
                    restrictionsIgnored.setChecked(true);
                }
            } else {
                CharacterManager.getSelectedCharacter().getSettings().setRestrictionsChecked(!isChecked);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_info_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CharacterInfoViewModel.class);

        specieSelector = root.findViewById(R.id.character_specie);
        upbringingSelector = root.findViewById(R.id.character_upbringing);
        factionsSelector = root.findViewById(R.id.character_faction);
        callingSelector = root.findViewById(R.id.character_calling);
        planetSelector = root.findViewById(R.id.character_planet);

        nonOfficialEnabled = root.findViewById(R.id.official_selector);
        restrictionsIgnored = root.findViewById(R.id.restricted_selector);

        CharacterManager.addCharacterSettingsUpdateListeners(this::updateSettings);
        CharacterManager.addSelectedCharacterListener(characterPlayer -> populateElements(root, characterPlayer));

        return root;
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null) {
            //Avoid to set a different value when changing the ElementAdapter.
            specieSelector.setOnItemSelectedListener(null);
            upbringingSelector.setOnItemSelectedListener(null);
            factionsSelector.setOnItemSelectedListener(null);
            callingSelector.setOnItemSelectedListener(null);
            planetSelector.setOnItemSelectedListener(null);

            //Storing old selected value.
            final Specie selectedSpecie = specieSelector.getSelection();
            final Upbringing selectedUpbringing = upbringingSelector.getSelection();
            final Faction selectedFaction = factionsSelector.getSelection();
            final Calling selectedCalling = callingSelector.getSelection();
            final Planet selectedPlanet = planetSelector.getSelection();

            //Create new adapter with the new settings.
            createSpecieSpinner(characterPlayer, !characterPlayer.getSettings().isOnlyOfficialAllowed());
            createUpbringingSpinner(characterPlayer, !characterPlayer.getSettings().isOnlyOfficialAllowed());
            createFactionSpinner(characterPlayer, !characterPlayer.getSettings().isOnlyOfficialAllowed());
            createCallingSpinner(characterPlayer, !characterPlayer.getSettings().isOnlyOfficialAllowed());
            createPlanetSpinner(characterPlayer, !characterPlayer.getSettings().isOnlyOfficialAllowed());

            //Recovering old selected value.
            specieSelector.setSelection(selectedSpecie);
            upbringingSelector.setSelection(selectedUpbringing);
            factionsSelector.setSelection(selectedFaction);
            callingSelector.setSelection(selectedCalling);
            planetSelector.setSelection(selectedPlanet);
        }
    }

    @Override
    public void populateElements(View root, CharacterPlayer character) {
        updatingCharacter = true;
        final TranslatedEditText nameTextEditor = root.findViewById(R.id.character_name);
        nameTextEditor.setText(character.getInfo().getNameRepresentation());
        final TranslatedEditText surnameTextEditor = root.findViewById(R.id.character_surname);
        if (CharacterManager.getSelectedCharacter().getInfo().getSurname() != null) {
            surnameTextEditor.setText(character.getInfo().getSurname().getNameRepresentation());
        } else {
            surnameTextEditor.setText("");
        }
        final EnumSpinner genderSelector = root.findViewById(R.id.character_gender);
        genderSelector.setSelection(character.getInfo().getGender());
        final TranslatedEditText ageTextEditor = root.findViewById(R.id.character_age);
        if (CharacterManager.getSelectedCharacter().getInfo().getAge() != null) {
            ageTextEditor.setText(CharacterManager.getSelectedCharacter().getInfo().getAge().toString());
        } else {
            ageTextEditor.setText("");
        }

        nonOfficialEnabled.setChecked(!character.getSettings().isOnlyOfficialAllowed());
        restrictionsIgnored.setChecked(!character.getSettings().isRestrictionsChecked());

        specieSelector.setSelection(SpecieFactory.getInstance().getElement(CharacterManager.getSelectedCharacter().getSpecie()));
        factionsSelector.setSelection(FactionFactory.getInstance().getElement(CharacterManager.getSelectedCharacter().getFaction()));
        callingSelector.setSelection(CallingFactory.getInstance().getElement(CharacterManager.getSelectedCharacter().getCalling()));
        planetSelector.setSelection(PlanetFactory.getInstance().getElement(CharacterManager.getSelectedCharacter().getInfo().getPlanet()));

        updateSettings(character);

        updatingCharacter = false;
    }


    private void createGenderSpinner(View root) {
        final EnumSpinner genderSelector = root.findViewById(R.id.character_gender);
        List<Gender> options = new ArrayList<>(mViewModel.getAvailableGenders());
        options.add(0, null);
        genderSelector.setAdapter(new EnumAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options));
        genderSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    CharacterManager.getSelectedCharacter().getInfo().setGender(mViewModel.getAvailableGenders().get(position - 1));
                } else {
                    CharacterManager.getSelectedCharacter().getInfo().setGender(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Nothing
            }
        });
    }

    private void updateSpinnersStatus() {
        upbringingSelector.setEnabled(specieSelector.getSelection() != null);
        if (specieSelector == null) {
            upbringingSelector.setSelection(null);
        }

        factionsSelector.setEnabled(upbringingSelector.getSelection() != null);
        if (upbringingSelector == null) {
            factionsSelector.setSelection(null);
        }

        callingSelector.setEnabled(factionsSelector.getSelection() != null);
        if (factionsSelector == null) {
            callingSelector.setSelection(null);
        }
    }

    private void createSpecieSpinner(CharacterPlayer characterPlayer, boolean nonOfficial) {
        List<Specie> options = new ArrayList<>(mViewModel.getAvailableSpecies(nonOfficial));
        options.add(0, null);
        specieSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Specie.class) {
            @Override
            public boolean isEnabled(int position) {
                //Faction limitations
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        specieSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0 || mViewModel.getAvailableSpecies(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                        CharacterManager.setSpecie(null);
                    } else {
                        if (position > 0) {
                            CharacterManager.setSpecie(mViewModel.getAvailableSpecies(nonOfficial).get(position - 1));
                        } else {
                            CharacterManager.setSpecie(null);
                        }
                    }
                } catch (InvalidSpecieException | RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.invalidFactionAndRace).show();
                    specieSelector.setSelection(null);
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                    specieSelector.setSelection(null);
                }
                updateSpinnersStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.setSpecie(null);
                } catch (InvalidSpecieException | RestrictedElementException |
                         UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
                updateSpinnersStatus();
            }
        });
    }

    private void createUpbringingSpinner(CharacterPlayer characterPlayer, boolean nonOfficial) {
        List<Upbringing> options = new ArrayList<>(mViewModel.getAvailableUpbringings(nonOfficial));
        options.add(0, null);
        upbringingSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Upbringing.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        upbringingSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0 || mViewModel.getAvailableUpbringings(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                        try {
                            CharacterManager.setUpbringing(null);
                        } catch (InvalidUpbringingException e) {
                            //Nothing
                        }
                    } else {
                        if (position > 0) {
                            CharacterManager.setUpbringing(mViewModel.getAvailableUpbringings(nonOfficial).get(position - 1));
                        } else {
                            CharacterManager.setUpbringing(null);
                        }
                    }
                } catch (InvalidUpbringingException | RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.invalidUpbringing).show();
                    upbringingSelector.setSelection(null);
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                    upbringingSelector.setSelection(null);
                }
                updateSpinnersStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.setUpbringing(null);
                } catch (InvalidUpbringingException | RestrictedElementException |
                         UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
                updateSpinnersStatus();
            }
        });
    }


    private void createFactionSpinner(CharacterPlayer characterPlayer, boolean nonOfficial) {
        List<Faction> options = new ArrayList<>(mViewModel.getAvailableFactions(nonOfficial));
        options.add(0, null);
        factionsSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Faction.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        factionsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0 || mViewModel.getAvailableFactions(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                        try {
                            CharacterManager.setFaction(null);
                        } catch (InvalidFactionException e) {
                            //Nothing
                        }
                    } else {
                        if (position > 0) {
                            CharacterManager.setFaction(mViewModel.getAvailableFactions(nonOfficial).get(position - 1));
                        } else {
                            CharacterManager.setFaction(null);
                        }
                    }
                } catch (InvalidFactionException | RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.invalidFactionAndRace).show();
                    factionsSelector.setSelection(null);
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                    factionsSelector.setSelection(null);
                }
                updateSpinnersStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.setFaction(null);
                } catch (InvalidFactionException | RestrictedElementException |
                         UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
                updateSpinnersStatus();
            }
        });
    }

    private void createCallingSpinner(CharacterPlayer characterPlayer, boolean nonOfficial) {
        List<Calling> options = new ArrayList<>(mViewModel.getAvailableCallings(nonOfficial));
        options.add(0, null);
        callingSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Calling.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        callingSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0 || mViewModel.getAvailableCallings(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                        try {
                            CharacterManager.setCalling(null);
                        } catch (InvalidCallingException e) {
                            //Nothing
                        }
                    } else {
                        if (position > 0) {
                            CharacterManager.setCalling(mViewModel.getAvailableCallings(nonOfficial).get(position - 1));
                        } else {
                            CharacterManager.setCalling(null);
                        }
                    }
                } catch (InvalidCallingException | RestrictedElementException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.invalidCalling).show();
                    callingSelector.setSelection(null);
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_unofficial_element_not_allowed).show();
                    callingSelector.setSelection(null);
                }
                updateSpinnersStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    CharacterManager.setCalling(null);
                } catch (InvalidCallingException | RestrictedElementException |
                         UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
                updateSpinnersStatus();
            }
        });
    }

    private void createPlanetSpinner(CharacterPlayer characterPlayer, boolean nonOfficial) {
        List<Planet> options = new ArrayList<>(mViewModel.getAvailablePlanets(nonOfficial));
        options.add(0, null);
        planetSelector.setAdapter(new ElementAdapter<>(getActivity(), options, false, Planet.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked()
                        || CharacterManager.getSelectedCharacter().getSpecie() == null
                        || SpecieFactory.getInstance().getElement(CharacterManager.getSelectedCharacter().getSpecie()).getPlanets() == null
                        || SpecieFactory.getInstance().getElement(CharacterManager.getSelectedCharacter().getSpecie()).getPlanets().isEmpty()
                        || SpecieFactory.getInstance().getElement(CharacterManager.getSelectedCharacter().getSpecie()).getPlanets().contains(getItem(position).getId());
            }
        });
        planetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0 || mViewModel.getAvailablePlanets(nonOfficial).get(position - 1).getId().equals(Element.DEFAULT_NULL_ID)) {
                    CharacterManager.setPlanet(null);
                } else {
                    if (position > 0) {
                        CharacterManager.setPlanet(mViewModel.getAvailablePlanets(nonOfficial).get(position - 1));
                    } else {
                        CharacterManager.setPlanet(null);
                    }
                    updateSpinnersStatus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                CharacterManager.setPlanet(null);
                updateSpinnersStatus();
            }
        });
    }


}
