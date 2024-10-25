package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.equipment.Equipment;

import java.util.List;

public class EquipmentAdapter<Q extends Equipment> extends ElementAdapter<Q> {

    public EquipmentAdapter(@NonNull Context context, @NonNull List<Q> objects, boolean nullAllowed, Class<Q> clazz) {
        super(context, objects, nullAllowed, clazz);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, Q element, int position) {
        if (isEnabled(position)) {
            if (CharacterManager.getSelectedCharacter().getCashMoney() < element.getCost()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.unaffordableMoney));
            } else if (CharacterManager.getSelectedCharacter().getRemainingCash() < element.getCost()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.insufficientMoney));
            } else if (!element.isOfficial()) {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.unofficialElement));
            } else {
                elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }
        } else {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
        }
    }

    @Override
    public String getElementRepresentation(Q element) {
        if (element.getId().equals(Element.DEFAULT_NULL_ID)) {
            return "";
        }
        return element.getName() + " (" + element.getCost() + " " + getContext().getResources().getString(R.string.firebird_abbrev) + ")";
    }
}
