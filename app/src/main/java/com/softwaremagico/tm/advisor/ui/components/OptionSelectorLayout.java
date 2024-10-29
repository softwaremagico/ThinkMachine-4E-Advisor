package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.CharacterSelectedElement;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OptionSelectorLayout<E extends Element, O extends Option<E>> extends LinearLayout {

    public OptionSelectorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
    }


    public void setElements(Class<O> clazz, List<OptionSelector<E, O>> optionSelectors, List<CharacterSelectedElement> selections, CharacterPlayer characterPlayer) {
        super.removeAllViews();
        for (int i = 0; i < optionSelectors.size(); i++) {
            selections.get(i).getSelections().clear();
            super.addView(createSpinner(clazz, optionSelectors.get(i), selections.get(i).getSelections(), false, characterPlayer));
        }
    }

    //Currently only one option is allowed.
    private ElementSpinner<O> createSpinner(Class<O> clazz, OptionSelector<E, O> optionSelector, List<Selection> selections, boolean nonOfficial, CharacterPlayer characterPlayer) {
        ElementSpinner<O> elementSelector = new ElementSpinner<>(getContext());
        final List<O> options = new ArrayList<>(optionSelector.getOptions());
        Collections.sort(options);

        elementSelector.setAdapter(new ElementAdapter<>(getContext(), options, false, clazz) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });

        elementSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (optionSelector.getTotalOptions() <= 1) {
                    selections.clear();
                }
                if (options.get(position) instanceof CapabilityOption) {
                    selections.add(new Selection(options.get(position).getId(), ((CapabilityOption) options.get(position)).getSelectedSpecialization()));
                } else {
                    selections.add(new Selection(options.get(position).getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                if (selections != null) {
                    selections.clear();
                }
            }

        });

        //Refresh selections.
        if (options.size() > 1) {
            //Set Selection.
            if (selections != null && !selections.isEmpty()) {
                elementSelector.setSelection(options.stream().filter(o -> Objects.equals(o.getId(), selections.get(0).getId())).findFirst().orElse(null));
            }
        } else if (!options.isEmpty()) {
            elementSelector.setSelection(options.get(0));
            elementSelector.setEnabled(false);
        }

        return elementSelector;
    }
}
