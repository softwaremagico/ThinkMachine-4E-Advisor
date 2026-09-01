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

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.EnumSpinner;
import com.softwaremagico.tm.advisor.ui.components.TranslatedEditText;
import com.softwaremagico.tm.advisor.ui.main.BookContentRefreshable;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.EnumAdapter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.IncompleteSelectedElementException;
import com.softwaremagico.tm.exceptions.InvalidCallingException;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import com.softwaremagico.tm.exceptions.InvalidLevelException;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidUpbringingException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.character.names.RandomName;
import com.softwaremagico.tm.random.character.names.RandomSurname;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterInfoFragmentCharacter extends CharacterCustomFragment implements BookContentRefreshable {
    private CharacterInfoViewModel mViewModel;
    private View root;
    private ElementSpinner<Specie> specieSelector;
    private ElementSpinner<Upbringing> upbringingSelector;
    private ElementSpinner<Faction> factionsSelector;
    private ElementSpinner<Calling> callingSelector;
    private ElementSpinner<Planet> planetSelector;
    private TranslatedEditText levelTextEditor;
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
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null) {
            return;
        }
        updateTranslatedTextField(root, R.id.character_name, value -> {
            if (!updatingCharacter) {
                selectedCharacter.getInfo().setNames(value);
            }
        });
        updateTranslatedTextField(root, R.id.character_surname, value -> {
            if (!updatingCharacter) {
                selectedCharacter.getInfo().setSurname(value);
            }
        });
        updateTranslatedTextField(root, R.id.character_age, value -> {
            try {
                final CharacterPlayer currentCharacter = CharacterManager.getSelectedCharacter();
                if (currentCharacter == null) {
                    return;
                }
                if (!Objects.equals(currentCharacter.getInfo().getAge() + "", value)) {
                    currentCharacter.getInfo().setAge(Integer.parseInt(value));
                    //Force to update all costs.
                    if (!updatingCharacter) {
                        CharacterManager.launchCharacterAgeUpdatedListeners(currentCharacter);
                    }
                }
            } catch (NumberFormatException e) {
                final CharacterPlayer currentCharacter = CharacterManager.getSelectedCharacter();
                if (currentCharacter != null) {
                    currentCharacter.getInfo().setAge(null);
                    CharacterManager.launchCharacterAgeUpdatedListeners(currentCharacter);
                }
            }
        });
        updateTranslatedTextField(root, R.id.character_level, value -> {
            try {
                final CharacterPlayer currentCharacter = CharacterManager.getSelectedCharacter();
                if (currentCharacter == null) {
                    return;
                }
                if (!Objects.equals(currentCharacter.getLevel() + "", value)) {
                    try {
                        CharacterManager.setCharacterLevel(Integer.parseInt(value));
                        //Force to update all costs.
                        if (!updatingCharacter) {
                            CharacterManager.launchLevelUpdatedListeners(CharacterManager.getSelectedCharacter());
                        }
                    } catch (InvalidLevelException e) {
                        SnackbarGenerator.getErrorMessage(root, R.string.message_incomplete_level).show();
                        final CharacterPlayer selected = CharacterManager.getSelectedCharacter();
                        if (selected != null) {
                            levelTextEditor.setText(selected.getLevel() + "");
                        }
                        AdvisorLog.errorMessage(this.getClass(), e);
                    }
                }
            } catch (NumberFormatException e) {
                final CharacterPlayer currentCharacter = CharacterManager.getSelectedCharacter();
                if (currentCharacter != null) {
                    levelTextEditor.setText(currentCharacter.getLevel() + "");
                    CharacterManager.launchLevelUpdatedListeners(currentCharacter);
                }
            }
        });

        setLevelButtonsActions();

        createGenderSpinner(root);

        populateElements(root, selectedCharacter);

        ImageView randomNameButton = root.findViewById(R.id.button_random_name);
        if (randomNameButton != null) {
            randomNameButton.setOnClickListener(v -> {
                updatingCharacter = true;
                selectedCharacter.getInfo().setNames(new ArrayList<>());
                final RandomName randomName;
                try {
                    randomName = new RandomName(selectedCharacter, null);
                    randomName.assign();
                    final TranslatedEditText nameTextEditor = root.findViewById(R.id.character_name);
                    nameTextEditor.setText(selectedCharacter.getInfo().getNameRepresentation());
                } catch (InvalidXmlElementException | InvalidRandomElementSelectedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.selectFactionAndMore).show();
                }
                updatingCharacter = false;
            });
        }

        ImageView randomSurnameButton = root.findViewById(R.id.button_random_surname);
        if (randomSurnameButton != null) {
            randomSurnameButton.setOnClickListener(v -> {
                updatingCharacter = true;
                selectedCharacter.getInfo().setSurname((Surname) null);
                final RandomSurname randomSurname;
                try {
                    randomSurname = new RandomSurname(selectedCharacter, null);
                    randomSurname.assign();
                    final TranslatedEditText surnameTextEditor = root.findViewById(R.id.character_surname);
                    if (selectedCharacter.getInfo().getSurname() != null) {
                        surnameTextEditor.setText(selectedCharacter.getInfo().getSurname().getNameRepresentation());
                    } else {
                        surnameTextEditor.setText("");
                    }
                } catch (InvalidXmlElementException | InvalidRandomElementSelectedException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.selectFactionAndMore).show();
                }
                updatingCharacter = false;
            });
        }


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
        levelTextEditor = root.findViewById(R.id.character_level);
        levelTextEditor.setAsNumberEditor();
        CharacterManager.addLevelUpdatedListeners((characterPlayer) -> {
            levelTextEditor.setText(characterPlayer.getLevel() + "");
        });

        return root;
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null && characterPlayer != null) {
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
        if (character == null) {
            return;
        }
        updatingCharacter = true;
        final TranslatedEditText nameTextEditor = root.findViewById(R.id.character_name);
        nameTextEditor.setText(character.getInfo().getNameRepresentation());
        final TranslatedEditText surnameTextEditor = root.findViewById(R.id.character_surname);
        if (character.getInfo().getSurname() != null) {
            surnameTextEditor.setText(character.getInfo().getSurname().getNameRepresentation());
        } else {
            surnameTextEditor.setText("");
        }
        final EnumSpinner genderSelector = root.findViewById(R.id.character_gender);
        genderSelector.setSelection(character.getInfo().getGender());
        final TranslatedEditText ageTextEditor = root.findViewById(R.id.character_age);
        ageTextEditor.setAsNumberEditor();
        if (character.getInfo().getAge() != null) {
            ageTextEditor.setText(character.getInfo().getAge().toString());
        } else {
            ageTextEditor.setText("");
        }


        specieSelector.setSelection(SpecieFactory.getInstance().getElement(character.getSpecie()));
        upbringingSelector.setSelection(UpbringingFactory.getInstance().getElement(character.getUpbringing()));
        factionsSelector.setSelection(FactionFactory.getInstance().getElement(character.getFaction()));
        callingSelector.setSelection(CallingFactory.getInstance().getElement(character.getCalling()));
        planetSelector.setSelection(PlanetFactory.getInstance().getElement(character.getInfo().getPlanet()));
        levelTextEditor.setText(character.getLevel() + "");

        updateSettings(character);

        updatingCharacter = false;
    }

    @Override
    public void refreshBookContent() {
        final View view = getView();
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (view != null && selectedCharacter != null && isAdded()) {
            populateElements(view, selectedCharacter);
        }
    }


    private void createGenderSpinner(View root) {
        final EnumSpinner genderSelector = root.findViewById(R.id.character_gender);
        final Activity activity = getActivity();
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (activity == null || selectedCharacter == null) {
            return;
        }
        List<Gender> options = new ArrayList<>(mViewModel.getAvailableGenders());
        options.add(0, null);
        genderSelector.setAdapter(new EnumAdapter<>(activity, android.R.layout.simple_spinner_item, options));
        genderSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    Gender selectedGender = getSafeElementAt(mViewModel.getAvailableGenders(), position - 1);
                    if (selectedGender != null) {
                        selectedCharacter.getInfo().setGender(selectedGender);
                    }
                } else {
                    selectedCharacter.getInfo().setGender(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Nothing
            }
        });
    }

    private void updateSpinnersStatus() {
        if (specieSelector == null || upbringingSelector == null || factionsSelector == null || callingSelector == null) {
            return;
        }
        upbringingSelector.setEnabled(specieSelector.getSelection() != null);
        factionsSelector.setEnabled(upbringingSelector.getSelection() != null);
        callingSelector.setEnabled(factionsSelector.getSelection() != null);
    }

    private void createSpecieSpinner(CharacterPlayer characterPlayer, boolean nonOfficial) {
        final Activity activity = getActivity();
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (activity == null || selectedCharacter == null) {
            return;
        }
        List<Specie> options = new ArrayList<>(mViewModel.getAvailableSpecies(nonOfficial));
        options.add(0, null);
        specieSelector.setAdapter(new ElementAdapter<>(activity, options, false, Specie.class) {
            @Override
            public boolean isEnabled(int position) {
                //Faction limitations
                return getItem(position) == null || !selectedCharacter.getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        specieSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0) {
                        CharacterManager.setSpecie(null);
                    } else {
                        if (position > 0) {
                            Specie selectedSpecie = getSafeElementAt(mViewModel.getAvailableSpecies(nonOfficial), position - 1);
                            if (selectedSpecie != null) {
                                CharacterManager.setSpecie(selectedSpecie);
                            }
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
                } catch (IncompleteSelectedElementException e) {

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
        final Activity activity = getActivity();
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (activity == null || selectedCharacter == null) {
            return;
        }
        List<Upbringing> options = new ArrayList<>(mViewModel.getAvailableUpbringings(nonOfficial));
        options.add(0, null);
        upbringingSelector.setAdapter(new ElementAdapter<>(activity, options, false, Upbringing.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !selectedCharacter.getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        upbringingSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0) {
                        try {
                            CharacterManager.setUpbringing(null);
                        } catch (InvalidUpbringingException e) {
                            //Nothing
                        }
                    } else {
                        if (position > 0) {
                            Upbringing selectedUpbringing = getSafeElementAt(mViewModel.getAvailableUpbringings(nonOfficial), position - 1);
                            if (selectedUpbringing != null) {
                                CharacterManager.setUpbringing(selectedUpbringing);
                            }
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
        final Activity activity = getActivity();
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (activity == null || selectedCharacter == null) {
            return;
        }
        List<Faction> options = new ArrayList<>(mViewModel.getAvailableFactions(nonOfficial));
        options.add(0, null);
        factionsSelector.setAdapter(new ElementAdapter<>(activity, options, false, Faction.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !selectedCharacter.getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        factionsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0) {
                        try {
                            CharacterManager.setFaction(null);
                        } catch (InvalidFactionException e) {
                            //Nothing
                        }
                    } else {
                        if (position > 0) {
                            Faction selectedFaction = getSafeElementAt(mViewModel.getAvailableFactions(nonOfficial), position - 1);
                            if (selectedFaction != null) {
                                CharacterManager.setFaction(selectedFaction);
                            }
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
        final Activity activity = getActivity();
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (activity == null || selectedCharacter == null) {
            return;
        }
        List<Calling> options = new ArrayList<>(mViewModel.getAvailableCallings(nonOfficial));
        options.add(0, null);
        callingSelector.setAdapter(new ElementAdapter<>(activity, options, false, Calling.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !selectedCharacter.getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });
        callingSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (position == 0) {
                        try {
                            CharacterManager.setCalling(null);
                        } catch (InvalidCallingException e) {
                            //Nothing
                        }
                    } else if (position > 0) {
                        Calling selectedCalling = getSafeElementAt(mViewModel.getAvailableCallings(nonOfficial), position - 1);
                        if (selectedCalling != null && selectedCalling.getId() != null && !selectedCalling.getId().equals(Element.DEFAULT_NULL_ID)) {
                            CharacterManager.setCalling(selectedCalling);
                        } else {
                            try {
                                CharacterManager.setCalling(null);
                            } catch (InvalidCallingException e) {
                                //Nothing
                            }
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
        final Activity activity = getActivity();
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (activity == null || selectedCharacter == null) {
            return;
        }
        List<Planet> options = new ArrayList<>(mViewModel.getAvailablePlanets(nonOfficial));
        options.add(0, null);
        planetSelector.setAdapter(new ElementAdapter<>(activity, options, false, Planet.class) {
            @Override
            public boolean isEnabled(int position) {
                if (getItem(position) == null || !selectedCharacter.getSettings().isRestrictionsChecked()
                        || selectedCharacter.getSpecie() == null) {
                    return true;
                }
                
                Specie specie = SpecieFactory.getInstance().getElement(selectedCharacter.getSpecie());
                if (specie == null || specie.getPlanets() == null || specie.getPlanets().isEmpty()) {
                    return true;
                }
                
                return specie.getPlanets().contains(getItem(position).getId());
            }

            @Override
            protected void setElementColor(TextView elementRepresentation, Planet planet, int position) {
                if (selectedCharacter.getSpecie() != null && planet.getSpecies().contains(selectedCharacter.getSpecie().getId())) {
                    elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorHighlyRecommended));
                } else if (selectedCharacter.getFaction() != null && planet.getFactions().contains(selectedCharacter.getFaction().getId())) {
                    elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRecommended));
                } else {
                    elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.unofficialElement));
                }
            }
        });
        planetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    CharacterManager.setPlanet(null);
                } else if (position > 0) {
                    Planet selectedPlanet = getSafeElementAt(mViewModel.getAvailablePlanets(nonOfficial), position - 1);
                    if (selectedPlanet != null && selectedPlanet.getId() != null && !selectedPlanet.getId().equals(Element.DEFAULT_NULL_ID)) {
                        CharacterManager.setPlanet(selectedPlanet);
                    } else {
                        CharacterManager.setPlanet(null);
                    }
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

    private void setLevelButtonsActions() {
        final ImageView addLevelButton = root.findViewById(R.id.button_add_level);
        final ImageView removeLevelButton = root.findViewById(R.id.button_remove_level);
        if (addLevelButton != null) {
            addLevelButton.setOnClickListener(v -> {
                try {
                    final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
                    if (selectedCharacter == null) {
                        return;
                    }
                    updatingCharacter = true;
                    CharacterManager.addCharacterLevel();
                    if (removeLevelButton != null) {
                        removeLevelButton.setEnabled(selectedCharacter.getLevel() > 1);
                    }
                    updatingCharacter = false;
                } catch (InvalidLevelException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_incomplete_level).show();
                    final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
                    if (selectedCharacter != null) {
                        levelTextEditor.setText(selectedCharacter.getLevel() + "");
                    }
                    AdvisorLog.errorMessage(this.getClass(), e);
                }
            });
        }

        if (removeLevelButton != null) {
            removeLevelButton.setOnClickListener(v -> {
                try {
                    final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
                    if (selectedCharacter != null && selectedCharacter.getLevel() > 1) {
                        updatingCharacter = true;
                        CharacterManager.removeCharacterLevel();
                        final CharacterPlayer updatedCharacter = CharacterManager.getSelectedCharacter();
                        if (updatedCharacter != null) {
                            removeLevelButton.setEnabled(updatedCharacter.getLevel() > 1);
                        }
                        updatingCharacter = false;
                    } else {
                        SnackbarGenerator.getWarningMessage(root, R.string.message_minimum_level_zero);
                    }
                } catch (InvalidLevelException e) {
                    SnackbarGenerator.getErrorMessage(root, R.string.message_incomplete_level).show();
                    final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
                    if (selectedCharacter != null) {
                        levelTextEditor.setText(selectedCharacter.getLevel() + "");
                    }
                    AdvisorLog.errorMessage(this.getClass(), e);
                }
            });
            final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
            if (selectedCharacter != null) {
                removeLevelButton.setEnabled(selectedCharacter.getLevel() > 1);
            }
        }
    }


    public <T> T getSafeElementAt(java.util.List<T> list, int position) {
        if (list == null || position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }
}
