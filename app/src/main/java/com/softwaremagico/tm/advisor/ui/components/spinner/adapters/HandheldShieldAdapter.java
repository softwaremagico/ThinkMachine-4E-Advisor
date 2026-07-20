package com.softwaremagico.tm.advisor.ui.components.spinner.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.values.Phase;

import java.util.List;
import java.util.Objects;

public class HandheldShieldAdapter extends EquipmentAdapter<HandheldShield> {

    private static final String HANDHELD_SHIELD_CAPABILITY = "handheldShield";

    public HandheldShieldAdapter(@NonNull Context context, @NonNull List<HandheldShield> handheldShields, boolean nullAllowed) {
        super(context, handheldShields, nullAllowed, HandheldShield.class);
    }

    @Override
    protected void setElementColor(TextView elementRepresentation, HandheldShield handheldShield, int position) {
        if (Objects.equals(handheldShield, CharacterManager.getSelectedCharacter().getPurchasedHandheldShield())) {
            elementRepresentation.setTextColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
        } else {
            super.setElementColor(elementRepresentation, handheldShield, position);
        }
    }
}
