package com.softwaremagico.tm.advisor.ui.character.calling;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionFragment;
import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionStepModel;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;

public class CallingFragmentCharacter extends CharacterDefinitionFragment<Calling> {

    private CharacterDefinitionStepModel mViewModel;
    private View root;

    public static CallingFragmentCharacter newInstance(int index) {
        final CallingFragmentCharacter fragment = new CallingFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CharacterDefinitionStepModel.class);
        setCharacterDefinitionStepModel(mViewModel);
        CharacterManager.addCharacterCallingUpdatedListener(this::updateCalling);
        updateCalling(CharacterManager.getSelectedCharacter());
        return root;
    }

    private void updateCalling(CharacterPlayer characterPlayer) {
        populateElements(this.root, characterPlayer);
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        populateElements(root, CallingFactory.getInstance().getElement(character.getCalling()),
                mViewModel.getCharacterPlayer().getCalling());
    }
}
