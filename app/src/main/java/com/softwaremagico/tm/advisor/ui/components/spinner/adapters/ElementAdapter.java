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

package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElementAdapter<E extends Element> extends ArrayAdapter<E> {
    private List<E> elements;
    private List<E> originalElements;

    //For filtering
    private ElementFilter elementFilter;

    public ElementAdapter(@NonNull Context context, @NonNull List<E> objects, boolean nullAllowed, Class<E> clazz) {
        super(context, android.R.layout.simple_spinner_dropdown_item, objects);
        this.elements = new ArrayList<>(objects);
        if (nullAllowed) {
            addNullValue(clazz);
        }
    }

    private void addNullValue(Class<E> clazz) {
        try {
            //Create null instance.
            final E instance = clazz.newInstance();
            if (elements.isEmpty() || !getItem(0).getId().equals(Element.DEFAULT_NULL_ID)) {
                insert(instance, 0);
                elements.add(0, instance);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            AdvisorLog.errorMessage(this.getClass().getName(), e);
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.element_list, parent, false);
        }

        final E element = elements.get(position);

        if (element != null) {
            final TextView name = listItem.findViewById(R.id.selected_item);
            name.setText(getElementRepresentation(element));
            setElementColor(name, element, position);
        }

        return listItem;
    }

    protected void setElementColor(TextView elementRepresentation, E element, int position) {
        if (isEnabled(position)) {
            if (!element.isOfficial()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.unofficialElement));
            } else {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_onPrimaryContainer));
            }
        } else {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
        }
    }

    public String getElementRepresentation(E element) {
        if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
            return "";
        }
        try {
            return element.getNameRepresentation();
        }catch(InvalidXmlElementException e){
            return "<<not implemented>>";
        }
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.element_list, parent, false);
        }

        try {
            final E element = getItem(position);
            final TextView elementName = listItem.findViewById(R.id.selected_item);
            if (element != null) {
                elementName.setText(getElementRepresentation(element));
                setElementColor(elementName, element, position);
            } else {
                //For an strange reason, the searchable list dialog shows a random element for null if text is empty.
                elementName.setText(" ");
            }
        } catch (IndexOutOfBoundsException e) {
            //Filtered elements.
        }

        return listItem;
    }

    public int indexOf(E element) {
        return elements.indexOf(element);
    }

    @Override
    public E getItem(int position) {
        return elements.get(position);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Filter getFilter() {
        if (elementFilter == null) {
            elementFilter = new ElementFilter();
        }
        return elementFilter;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ElementFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (originalElements == null) {
                synchronized (this) {
                    if (CharacterManager.getSelectedCharacter().getSettings().isOnlyOfficialAllowed()) {
                        originalElements = elements.stream().filter(e -> e == null || e.isOfficial()).collect(Collectors.toList());
                    } else {
                        originalElements = new ArrayList<>(elements);
                    }
                }
            }

            if (prefix == null || prefix.length() == 0) {
                // No filter implemented we return all the original list
                final ArrayList<E> list;
                synchronized (this) {
                    list = new ArrayList<>(originalElements);
                }
                results.values = list;
                results.count = list.size();
            } else {
                // We perform filtering operation
                List<E> elementList = new ArrayList<E>();
                final String prefixString = removeDiacriticalMarks(prefix.toString().toLowerCase());

                for (E element : originalElements) {
                    if (element == null) {
                        continue;
                    }
                    String name = removeDiacriticalMarks(element.getNameRepresentation().toLowerCase());
                    if (name.startsWith(prefixString)) {
                        elementList.add(element);
                    } else {
                        final String[] words = name.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                elementList.add(element);
                                break;
                            }
                        }
                    }
                }

                results.values = elementList;
                results.count = elementList.size();
            }
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            if (results.values != null) {
                elements = (List<E>) results.values;
            } else {
                elements = new ArrayList<>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


    public static String removeDiacriticalMarks(String string) {
        if (string == null) {
            return "";
        }
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
