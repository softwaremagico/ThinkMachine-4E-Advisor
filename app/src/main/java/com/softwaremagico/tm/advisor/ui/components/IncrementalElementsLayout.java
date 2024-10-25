/*
 *  Copyright (C) 2024 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IncrementalElementsLayout<E extends Element> extends LinearLayout {
    private ElementAdapter<E> elementAdapter;
    private final List<ElementSpinner<E>> defaultElementSpinners;
    private final List<ElementSpinner<E>> elementSpinners;
    private boolean enabled = true;
    private final boolean nullAllowed;
    private final Set<AdapterView.OnItemSelectedListener> listeners;
    private final int maxElements;

    public IncrementalElementsLayout(Context context, boolean nullAllowed, int maxElements) {
        this(context, null, nullAllowed, maxElements);
    }


    public IncrementalElementsLayout(Context context, boolean nullAllowed) {
        this(context, null, nullAllowed, 100);
    }

    public IncrementalElementsLayout(Context context, @Nullable AttributeSet attrs, boolean nullAllowed, int maxElements) {
        super(context, attrs);
        this.nullAllowed = nullAllowed;
        this.maxElements = maxElements;
        elementSpinners = new ArrayList<>();
        defaultElementSpinners = new ArrayList<>();
        listeners = new HashSet<>();
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        updateContent();
    }

    protected void updateContent() {
        if (!enabled) {
            return;
        }
        if (elementSpinners.isEmpty()) {
            addElementSpinner(createElementSpinner());
            return;
        }
        boolean previousIsNull = false;
        for (int i = elementSpinners.size() - 1; i >= 0; i--) {
            //Not the last spinner.
            if (i < elementSpinners.size() - 1) {
                if (elementSpinners.get(i).getSelection() == null) {
                    if (previousIsNull) {
                        removeView(elementSpinners.get(i + 1));
                        elementSpinners.remove(i + 1);
                    } else {
                        removeView(elementSpinners.get(i));
                        elementSpinners.remove(i);
                    }
                    previousIsNull = true;
                } else {
                    previousIsNull = false;
                }
            } else {
                //Last must be an empty one.
                if (nullAllowed && elementSpinners.get(i).getSelection() != null) {
                    addElementSpinner(createElementSpinner());
                    previousIsNull = false;
                } else {
                    previousIsNull = true;
                }
            }
        }
    }

    private boolean removeDuplicates(List<ElementSpinner<E>> spinnerList) {
        int i = 0;
        Set<E> selections = new HashSet<>();
        boolean removed = false;
        while (i < spinnerList.size()) {
            if (spinnerList.get(i).getSelection() != null) {
                if (!selections.contains(spinnerList.get(i).getSelection())) {
                    selections.add(spinnerList.get(i).getSelection());
                } else {
                    removeView(spinnerList.get(i));
                    spinnerList.remove(i);
                    removed = true;
                }
            }
            i++;
        }
        if (removed) {
            updateContent();
        }
        return removed;
    }

    protected boolean removeDuplicates() {
        return removeDuplicates(elementSpinners);
    }

    private void clear() {
        removeAllViewsInLayout();
        elementSpinners.clear();
        defaultElementSpinners.clear();
    }

    public void setElement(E element) {
        final List<E> elements = new ArrayList<>();
        elements.add(element);
        addElements(elements);
    }

    public void setElements(Collection<E> elements) {
        setElements(elements, new HashSet<>());
    }

    public void setElements(Collection<E> elements, Collection<E> defaultElements) {
        enabled = false;
        clear();
        addDefaults(defaultElements);
        addElements(elements);
        enabled = true;
    }

    private void addElements(Collection<E> elements) {
        for (final E element : elements) {
            final ElementSpinner<E> spinner = createElementSpinner();
            spinner.setSelection(element);
            addElementSpinner(spinner);
        }
        //Last must be an empty one.
        if (nullAllowed) {
            addElementSpinner(createElementSpinner());
        }
    }

    public List<ElementSpinner<E>> getElementSpinners() {
        return elementSpinners;
    }

    private void addDefaults(Collection<E> elements) {
        elements.stream().sorted().forEach(element -> {
            final ElementSpinner<E> spinner = createElementSpinner();
            spinner.setSelection(element);
            super.addView(spinner);
            setElementSpinnerProperties(spinner);
            spinner.setEnabled(false);
            defaultElementSpinners.add(spinner);
        });
        removeDuplicates(defaultElementSpinners);
    }

    private void addElementSpinner(ElementSpinner<E> spinner) {
        if (elementSpinners.size() >= maxElements) {
            return;
        }
        super.addView(spinner);
        enabled = false;
        setElementSpinnerProperties(spinner);
        elementSpinners.add(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (final AdapterView.OnItemSelectedListener listener : listeners) {
                    listener.onItemSelected(parent, view, position, id);
                }
                updateContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                for (final AdapterView.OnItemSelectedListener listener : listeners) {
                    listener.onNothingSelected(parent);
                }
                updateContent();
            }
        });
        enabled = true;
    }

    private void setElementSpinnerProperties(ElementSpinner<E> spinner) {
        spinner.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    private ElementSpinner<E> createElementSpinner() {
        final ElementSpinner<E> selector = new ElementSpinner<>(getContext());
        selector.setAdapter(getElementAdapter(true));
        return selector;
    }

    public void updateElementAdapter(boolean nonOfficial) {
        elementAdapter = createElementAdapter(nonOfficial);
        elementSpinners.forEach(elementSpinner -> elementSpinner.setAdapter(elementAdapter));
    }

    protected abstract ElementAdapter<E> createElementAdapter(boolean nonOfficial);

    private ElementAdapter<E> getElementAdapter(boolean nonOfficial) {
        if (elementAdapter == null) {
            elementAdapter = createElementAdapter(nonOfficial);
        }
        return elementAdapter;
    }

    protected boolean isNullAllowed() {
        return nullAllowed;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        listeners.add(listener);
    }
}
