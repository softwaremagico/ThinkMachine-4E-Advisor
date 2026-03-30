package com.softwaremagico.tm.advisor.ui.character.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionFragment;
import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionStepModel;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.level.Level;
import com.softwaremagico.tm.character.level.LevelFactory;

public class LevelFragmentCharacter extends CharacterDefinitionFragment<Level> {

    private CharacterDefinitionStepModel mViewModel;
    private View root;

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
        rootLayout.removeAllViews();
        for (int i = 2; i <= character.getLevel(); i++) {
            addSection(ThinkMachineTranslator.getTranslatedText("level") + " " + i, rootLayout);
            addSpace(rootLayout);
            populateElements(LevelFactory.getInstance().getElement(character, i),
                    mViewModel.getCharacterPlayer().getLevel(i - 1));
        }
    }
}
