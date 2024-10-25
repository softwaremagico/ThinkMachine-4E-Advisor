package com.softwaremagico.tm.advisor.ui.character.upbringing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionFragment;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;

public class UpbringingFragmentCharacter extends CharacterDefinitionFragment<Upbringing> {

    private CharacterDefinitionStepModel mViewModel;

    public static UpbringingFragmentCharacter newInstance(int index) {
        final UpbringingFragmentCharacter fragment = new UpbringingFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CharacterDefinitionStepModel.class);
        return rootView;
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        populateElements(root, UpbringingFactory.getInstance().getElement(character.getUpbringing()),
                mViewModel.getCharacterPlayer().getUpbringing(), mViewModel.getCharacterPlayer());
    }
}
