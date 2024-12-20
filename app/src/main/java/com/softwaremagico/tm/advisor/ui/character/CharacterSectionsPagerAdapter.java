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

package com.softwaremagico.tm.advisor.ui.character;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.character.calling.CallingFragmentCharacter;
import com.softwaremagico.tm.advisor.ui.character.cybernetics.CyberneticsFragmentCharacter;
import com.softwaremagico.tm.advisor.ui.character.description.CharacterDescriptionFragmentCharacter;
import com.softwaremagico.tm.advisor.ui.character.equipment.EquipmentFragmentCharacter;
import com.softwaremagico.tm.advisor.ui.character.faction.FactionFragmentCharacter;
import com.softwaremagico.tm.advisor.ui.character.info.CharacterInfoFragmentCharacter;
import com.softwaremagico.tm.advisor.ui.character.occultism.OccultismFragmentCharacter;
import com.softwaremagico.tm.advisor.ui.character.upbringing.UpbringingFragmentCharacter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class CharacterSectionsPagerAdapter extends FragmentStateAdapter {

    @StringRes
    public static final int[] TAB_TITLES = new int[]{R.string.tab_character_info, R.string.tab_character_description, R.string.upbringing, R.string.faction, R.string.calling, R.string.tab_character_occultism, R.string.tab_character_cybernetics, R.string.tab_character_equipment};

    CharacterSectionsPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return CharacterInfoFragmentCharacter.newInstance(position + 1);
        }

        if (position == 1) {
            return CharacterDescriptionFragmentCharacter.newInstance(position + 1);
        }

        if (position == 2) {
            return UpbringingFragmentCharacter.newInstance(position + 1);
        }

        if (position == 3) {
            return FactionFragmentCharacter.newInstance(position + 1);
        }

        if (position == 4) {
            return CallingFragmentCharacter.newInstance(position + 1);
        }

        if (position == 5) {
            return OccultismFragmentCharacter.newInstance(position + 1);
        }

        if (position == 6) {
            return CyberneticsFragmentCharacter.newInstance(position + 1);
        }

        if (position == 7) {
            return EquipmentFragmentCharacter.newInstance(position + 1);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }

    public static int getIndexOf(int element) {
        for (int i = 0; i < TAB_TITLES.length; i++) {
            if (TAB_TITLES[i] == element) {
                return i;
            }
        }
        return -1;
    }
}
