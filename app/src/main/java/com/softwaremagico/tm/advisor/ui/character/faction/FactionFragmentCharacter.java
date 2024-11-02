package com.softwaremagico.tm.advisor.ui.character.faction;

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
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;

public class FactionFragmentCharacter extends CharacterDefinitionFragment<Faction> {

    private CharacterDefinitionStepModel mViewModel;
    private View root;

    public static FactionFragmentCharacter newInstance(int index) {
        final FactionFragmentCharacter fragment = new FactionFragmentCharacter();
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
        CharacterManager.addCharacterFactionUpdatedListener(this::updateFaction);
        updateFaction(CharacterManager.getSelectedCharacter());
        return root;
    }

    private void updateFaction(CharacterPlayer characterPlayer) {
        populateElements(this.root, characterPlayer);
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        populateElements(root, FactionFactory.getInstance().getElement(character.getFaction()),
                mViewModel.getCharacterPlayer().getFaction());
    }
}
