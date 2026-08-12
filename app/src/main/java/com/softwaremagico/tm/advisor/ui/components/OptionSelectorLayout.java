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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OptionSelectorLayout<E extends Element, O extends Option<E>> extends LinearLayout {

    private final Set<ElementsSizeUpdatedListener> optionsSizeUpdatedListeners = new HashSet<>();
    private final Set<ElementsSelectedListener> elementsSelectedUpdatedListeners = new HashSet<>();

    public interface ElementsSizeUpdatedListener {
        void sizeChanged(int size);
    }

    public interface ElementsSelectedListener {
        void selectedElements(Collection<Selection> selections);
    }

    public OptionSelectorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void launchElementsSizeUpdatedListeners(List<OptionSelector<E, O>> optionSelectors) {
        for (final ElementsSizeUpdatedListener listener : optionsSizeUpdatedListeners) {
            listener.sizeChanged(optionSelectors.size());
        }
    }

    public void addElementsSizeUpdatedListener(ElementsSizeUpdatedListener listener) {
        optionsSizeUpdatedListeners.add(listener);
    }

    public void launchElementsSelectedListener(Collection<Selection> selections) {
        for (final ElementsSelectedListener listener : elementsSelectedUpdatedListeners) {
            listener.selectedElements(selections);
        }
    }

    public void addElementsSelectedListener(ElementsSelectedListener listener) {
        elementsSelectedUpdatedListeners.add(listener);
    }


    public void setElements(Class<O> clazz, List<OptionSelector<E, O>> optionSelectors,
                            List<CharacterSelectedElement> selections, boolean disabled, boolean nullAllowed, CharacterPlayer characterPlayer) {
        super.removeAllViews();
        for (int i = 0; i < optionSelectors.size(); i++) {
            //selections.get(i).getSelections().clear();
            final ElementSpinner<O> spinner = createSpinner(clazz, optionSelectors.get(i), selections.get(i).getSelections(), false, disabled, nullAllowed, characterPlayer);
            if (spinner != null) {
                super.addView(spinner);
            }
        }
        launchElementsSizeUpdatedListeners(optionSelectors);
    }

    //Currently only one option is allowed.
    private ElementSpinner<O> createSpinner(Class<O> clazz, OptionSelector<E, O> optionSelector, Collection<Selection> selections,
                                            boolean nonOfficial, boolean disabled, boolean nullAllowed, CharacterPlayer characterPlayer) {
        if (getContext() == null || optionSelector == null) {
            return null;
        }
        ElementSpinner<O> elementSelector = new ElementSpinner<>(getContext());
        final List<O> options = new ArrayList<>(optionSelector.getOptions());
        Collections.sort(options);

        elementSelector.setAdapter(new ElementAdapter<>(getContext(), options, nullAllowed, clazz) {
            @Override
            public boolean isEnabled(int position) {
                final O item = getItem(position);
                final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
                if (selectedCharacter == null) {
                    return item == null;
                }
                return item == null || !selectedCharacter.getSettings().isRestrictionsChecked() ||
                        (!item.getRestrictions().isRestricted() && !item.getRestrictions().isRestricted(characterPlayer) && !disabled && getCount() != 1);
            }
        });

        elementSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position < 0 || position >= options.size()) {
                    if (selections != null) {
                        selections.clear();
                    }
                    launchElementsSelectedListener(selections);
                    return;
                }
                if (optionSelector.getTotalOptions() <= 1) {
                    if (selections != null) {
                        selections.clear();
                    }
                }
                final O selectedOption = options.get(position);
                if (selectedOption == null || selectedOption.getId() == null) {
                    if (selections != null) {
                        selections.clear();
                    }
                } else {
                    if (selectedOption instanceof CapabilityOption) {
                        if (selections != null) {
                            selections.add(new Selection(selectedOption, ((CapabilityOption) selectedOption).getSelectedSpecialization()));
                        }
                    } else {
                        if (selections != null) {
                            selections.add(new Selection(selectedOption));
                        }
                    }
                }
                launchElementsSelectedListener(selections);
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
                elementSelector.setSelection(options.stream().filter(o -> Objects.equals(o.getId(), selections.iterator().next().getId())).findFirst().orElse(null));
            }
        } else if (!options.isEmpty()) {
           O selectedOption = options.get(0);
           if (selectedOption != null) {
               elementSelector.setSelection(selectedOption);
               elementSelector.setEnabled(false);
           }
        }

        return elementSelector;
    }
}
