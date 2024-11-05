package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.equipment.armors.Armor;

import java.util.List;
import java.util.Objects;

public class ArmorAdapter extends EquipmentAdapter<Armor> {
    public ArmorAdapter(@NonNull Context context, @NonNull List<Armor> armours, boolean nullAllowed) {
        super(context, armours, nullAllowed, Armor.class);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, Armor armour, int position) {
        if (Objects.equals(armour, CharacterManager.getSelectedCharacter().getPurchasedArmor())) {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
        } else {
            super.setElementColor(elementRepresentation, armour, position);
        }
    }
}
