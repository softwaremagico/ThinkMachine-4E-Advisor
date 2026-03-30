/*
 *  Copyright (C) 2026 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.character.cybernetics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementTextHelp;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;

import java.util.ArrayList;
import java.util.List;

public class CyberneticsFragmentCharacter extends CharacterCustomFragment {
    private CyberneticsViewModel mViewModel;

    private final List<ElementTextHelp<Cyberdevice>> cyberDeviceList = new ArrayList<>();

    private View root;

    public static CyberneticsFragmentCharacter newInstance(int index) {
        final CyberneticsFragmentCharacter fragment = new CyberneticsFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void populateElements(View root, CharacterPlayer character) {
        if (getContext() != null) {
            final LinearLayout rootLayout = root.findViewById(R.id.cyberdevices_root_container);
            setContent(rootLayout, character);
        }
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.cyberdevices_root_container);
        setContent(rootLayout, CharacterManager.getSelectedCharacter());
        populateElements(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null) {
            final LinearLayout linearLayout = root.findViewById(R.id.cyberdevices_root_container);
            linearLayout.removeAllViews();
            setContent(linearLayout, characterPlayer);
            populateElements(root, characterPlayer);
        }
    }

    private void setContent(LinearLayout rootLayout, CharacterPlayer characterPlayer) {
        rootLayout.removeAllViews();
        addSubSection(ThinkMachineTranslator.getTranslatedText("cyberdevices"), rootLayout);
        addSpace(rootLayout);
        setCyberdevices(rootLayout, characterPlayer);
    }

    private void setCyberdevices(LinearLayout rootLayout, CharacterPlayer characterPlayer) {
        LinearLayout cyberdevicesLayout = new LinearLayout(getContext());
        cyberdevicesLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        cyberdevicesLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(cyberdevicesLayout);

        final List<Cyberdevice> cyberdevices = mViewModel.getAvailableCyberdevices(characterPlayer);
        cyberDeviceList.clear();
        cyberdevices.forEach(cyberdevice -> addCyberdeviceToLayout(cyberdevice, cyberdevicesLayout));
    }

    private void addCyberdeviceToLayout(Cyberdevice cyberdevice, LinearLayout cyberdevicesLayout) {
        final ElementTextHelp<Cyberdevice> cyberDeviceSelector = new ElementTextHelp<>(getContext(), cyberdevice);
        cyberdevicesLayout.addView(cyberDeviceSelector);
        if (!cyberDeviceList.contains(cyberDeviceSelector)) {
            cyberDeviceList.add(cyberDeviceSelector);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_cybernetics_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CyberneticsViewModel.class);

        CharacterManager.addCharacterFactionUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterSpecieUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterAgeUpdatedListener(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addPerkUpdatedListeners(characterPlayer -> populateElements(root, characterPlayer));
        CharacterManager.addCharacterSettingsUpdateListeners(this::updateSettings);

        return root;
    }

}
