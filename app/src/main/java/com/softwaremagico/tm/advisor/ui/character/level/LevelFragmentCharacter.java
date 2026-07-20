package com.softwaremagico.tm.advisor.ui.character.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionFragment;
import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionStepModel;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.callings.CallingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.level.Level;
import com.softwaremagico.tm.character.level.LevelFactory;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LevelFragmentCharacter extends CharacterDefinitionFragment<Level> {

    private CharacterDefinitionStepModel mViewModel;
    private View root;
    private boolean updatingLevelUI;

    public LevelFragmentCharacter() {
    }

    public static LevelFragmentCharacter newInstance(int index) {
        final LevelFragmentCharacter fragment = new LevelFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_levels_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CharacterDefinitionStepModel.class);
        setCharacterDefinitionStepModel(mViewModel);
        CharacterManager.addLevelUpdatedListeners(this::updateLevels);
        updateLevels(CharacterManager.getSelectedCharacter());
        return root;
    }

    @Override
    protected LinearLayout getLayoutContainer() {
        return root.findViewById(R.id.level_root_container);
    }

    private void updateLevels(CharacterPlayer characterPlayer) {
        populateElements(this.root, characterPlayer);
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        final LinearLayout rootLayout = root.findViewById(R.id.level_root_container);
        initData();
        updatingLevelUI = true;
        for (int i = 2; i <= character.getLevel(); i++) {
            addSection(ThinkMachineTranslator.getTranslatedText("level") + " " + i, rootLayout);
            final LevelSelector levelSelector = mViewModel.getCharacterPlayer().getLevel(i - 1);
            addCallingSelector(rootLayout, character, levelSelector, i, character.getLevel() != i);
            addSpace(rootLayout);
            populateElements(root, LevelFactory.getInstance().getElement(character, i),
                    levelSelector,
                    character.getLevel() != i);
        }
        updatingLevelUI = false;
    }

    private void addCallingSelector(LinearLayout rootLayout, CharacterPlayer character, LevelSelector levelSelector,
                                    int level, boolean disabled) {
        final ElementSpinner<Calling> callingSelector = new ElementSpinner<>(requireContext());
        final TextView tagText = callingSelector.findViewById(R.id.translated_tag);
        if (tagText != null) {
            tagText.setText(getString(R.string.calling));
        }

        final List<Calling> options = getAvailableCallings(character.getSettings().isOnlyOfficialAllowed());
        options.add(0, null);

        callingSelector.setAdapter(new ElementAdapter<>(requireContext(), options, false, Calling.class) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        (!getItem(position).getRestrictions().isRestricted() && !getItem(position).getRestrictions().isRestricted(character) && !disabled);
            }
        });

        final String selectedCallingId = ensureSelectedCalling(levelSelector, character, level);
        if (selectedCallingId != null) {
            callingSelector.setSelection(options.stream().filter(calling -> calling != null && Objects.equals(calling.getId(), selectedCallingId)).findFirst().orElse(null));
        } else {
            callingSelector.setSelection(null);
        }

        callingSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Calling selectedCalling = (Calling) parent.getItemAtPosition(position);
                final String selectedId = selectedCalling == null ? null : selectedCalling.getId();
                if (!Objects.equals(levelSelector.getCallingId(), selectedId)) {
                    levelSelector.setCalling(selectedId);
                    if (!updatingLevelUI) {
                        updateLevels(mViewModel.getCharacterPlayer());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (levelSelector.getCallingId() != null) {
                    levelSelector.setCalling(null);
                    if (!updatingLevelUI) {
                        updateLevels(mViewModel.getCharacterPlayer());
                    }
                }
            }
        });

        callingSelector.setEnabled(!disabled && options.size() > 1);
        rootLayout.addView(callingSelector);
    }

    private String ensureSelectedCalling(LevelSelector levelSelector, CharacterPlayer character, int level) {
        if (levelSelector.getCallingId() != null) {
            return levelSelector.getCallingId();
        }

        final String latestCallingId = getLatestCallingId(character, level);
        if (latestCallingId != null) {
            levelSelector.setCalling(latestCallingId);
        }
        return latestCallingId;
    }

    private String getLatestCallingId(CharacterPlayer character, int level) {
        if (level > 1) {
            final CallingCharacterDefinitionStepSelection previousCalling = character.getCallingAtLevel(level - 1);
            if (previousCalling != null && previousCalling.getId() != null) {
                return previousCalling.getId();
            }
        }

        if (character.getCalling() != null && character.getCalling().getId() != null) {
            return character.getCalling().getId();
        }
        return null;
    }

    private List<Calling> getAvailableCallings(boolean onlyOfficial) {
        try {
            if (onlyOfficial) {
                return CallingFactory.getInstance().getSelectableElements().stream()
                        .filter(Objects::nonNull)
                        .filter(Calling::isOfficial)
                        .sorted(Comparator.comparing(Calling::getNameRepresentation))
                        .collect(Collectors.toList());
            }
            return CallingFactory.getInstance().getSelectableElements().stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(Calling::getNameRepresentation))
                    .collect(Collectors.toList());
        } catch (InvalidXmlElementException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
            return new ArrayList<>();
        }
    }
}
