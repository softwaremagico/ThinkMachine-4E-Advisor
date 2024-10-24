package com.softwaremagico.tm.advisor.ui.character.upbringing;

import android.os.Bundle;
import android.view.View;

import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionFragment;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;

public class UpbringingFragmentCharacter extends CharacterDefinitionFragment<Upbringing> {

    public static UpbringingFragmentCharacter newInstance(int index) {
        final UpbringingFragmentCharacter fragment = new UpbringingFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        populateElements(root, UpbringingFactory.getInstance().getElement(character.getUpbringing()));
    }
}
