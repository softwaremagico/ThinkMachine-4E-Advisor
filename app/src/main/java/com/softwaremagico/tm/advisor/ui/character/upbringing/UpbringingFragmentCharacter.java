package com.softwaremagico.tm.advisor.ui.character.upbringing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionFragment;
import com.softwaremagico.tm.advisor.ui.components.CharacterDefinitionStepModel;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;

public class UpbringingFragmentCharacter extends CharacterDefinitionFragment<Upbringing> {

    private CharacterDefinitionStepModel mViewModel;
    private View root;

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
        root = super.onCreateView(inflater, container, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CharacterDefinitionStepModel.class);
        setCharacterDefinitionStepModel(mViewModel);
        CharacterManager.addCharacterUpbringingUpdatedListener(this::updateUpbringing);
        updateUpbringing(CharacterManager.getSelectedCharacter());
        return root;
    }

    private void updateUpbringing(CharacterPlayer characterPlayer) {
        populateElements(this.root, characterPlayer);
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        initData();
        populateElements(root, UpbringingFactory.getInstance().getElement(character.getUpbringing()),
                mViewModel.getCharacterPlayer().getUpbringing(),
                character.getLevel() != 1);
    }

    @Override
    protected void addElements(Upbringing definitionStep, CharacterDefinitionStepSelection characterDefinitionStepSelection, boolean disabled) {
        super.addElements(definitionStep, characterDefinitionStepSelection, disabled);
    }

    @Override
    protected void initData(Upbringing step) {
        final SwitchCompat raisedInSpace = createRaisedInSpaceSelector();
        final LinearLayout rootLayout = getLayoutContainer();

        rootLayout.addView(raisedInSpace);
        elements.add(raisedInSpace);

        super.initData(step);
    }

    private SwitchCompat createRaisedInSpaceSelector() {
        SwitchCompat raisedInSpaceSelector = new SwitchCompat(requireContext());
        raisedInSpaceSelector.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        raisedInSpaceSelector.setText(ThinkMachineTranslator.getTranslatedText("raisedInSpace"));

        if (mViewModel.getCharacterPlayer().getUpbringing() != null) {
            raisedInSpaceSelector.setChecked(mViewModel.getCharacterPlayer().getUpbringing().isRaisedInSpace());
        }

        raisedInSpaceSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mViewModel.getCharacterPlayer().getUpbringing().setRaisedInSpace(isChecked);
           updateUpbringing(mViewModel.getCharacterPlayer());
        });

        return raisedInSpaceSelector;
    }
}
