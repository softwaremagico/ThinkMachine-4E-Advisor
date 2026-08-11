package com.softwaremagico.tm.advisor.ui.random.preferences;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.EnumAdapter;

import java.util.List;

public class RandomEnumAdapter<T> extends EnumAdapter<T> {

    public RandomEnumAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            Context ctx = getContext();
            if (ctx == null) {
                AdvisorLog.warning(RandomEnumAdapter.class, "Context is null in getDropDownView");
                return new View(parent.getContext());
            }
            listItem = LayoutInflater.from(ctx).inflate(R.layout.element_list, parent, false);
        }

        if (position < 0 || position >= getElements().size()) {
            AdvisorLog.warning(RandomEnumAdapter.class, "Position " + position + " out of bounds for list size " + getElements().size());
            return listItem;
        }

        final Object element = getElements().get(position);

        if (element != null) {
            final TextView name = listItem.findViewById(R.id.selected_item);
            if (element instanceof Enum) {
                try {
                    Context ctx = getContext();
                    if (ctx != null) {
                        name.setText(ctx.getResources().getString(ctx.getResources().getIdentifier(getOptionTranslation((Enum) element),
                                "string", ctx.getPackageName())));
                    }
                } catch (Resources.NotFoundException e) {
                    AdvisorLog.warning(this.getClass().getName(), "No translation found for '" + getOptionTranslation((Enum) element) + "'.");
                }
            }
        }

        return listItem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            Context ctx = getContext();
            if (ctx == null) {
                AdvisorLog.warning(RandomEnumAdapter.class, "Context is null in getView");
                return new View(parent.getContext());
            }
            listItem = LayoutInflater.from(ctx).inflate(R.layout.element_list, parent, false);
        }

        if (position < 0 || position >= getElements().size()) {
            AdvisorLog.warning(RandomEnumAdapter.class, "Position " + position + " out of bounds for list size " + getElements().size());
            return listItem;
        }

        final Object element = getElements().get(position);

        if (element != null) {
            final TextView elementName = listItem.findViewById(R.id.selected_item);
            if (element instanceof Enum) {
                try {
                    Context ctx = getContext();
                    if (ctx != null) {
                        elementName.setText(ctx.getResources().getString(ctx.getResources().getIdentifier(getOptionTranslation((Enum) element),
                                "string", ctx.getPackageName())));
                    }
                } catch (Resources.NotFoundException e) {
                    AdvisorLog.warning(this.getClass().getName(), "No translation found for '" + getOptionTranslation((Enum) element) + "'.");
                }
            }
        }

        return listItem;
    }

    private String getOptionTranslation(Enum element) {
        return "preference_option_" + element.name().toLowerCase();
    }
}
