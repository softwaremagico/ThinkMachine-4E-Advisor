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

import java.util.ArrayList;
import java.util.List;

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

        elementSelector.setAdapter(new ElementAdapter<>(getContext(), options, false, clazz) {
            @Override
            public boolean isEnabled(int position) {
                return getItem(position) == null || !CharacterManager.getSelectedCharacter().getSettings().isRestrictionsChecked() ||
                        !(getItem(position).getRestrictions().isRestricted() || getItem(position).getRestrictions().isRestricted(characterPlayer));
            }
        });

        if (options.size() > 1) {
            options.add(0, null);
        } else if (!options.isEmpty()) {
            elementSelector.setSelection(options.get(0));
            elementSelector.setEnabled(false);
        }

        elementSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    selections.clear();
                } else {
                    if (position > 0) {
                        selections.add(new Selection(optionSelector.getOptions().get(position - 1).getId()));
                    } else {
                        selections.clear();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selections.clear();
            }
        });
        return elementSelector;
    }
}
