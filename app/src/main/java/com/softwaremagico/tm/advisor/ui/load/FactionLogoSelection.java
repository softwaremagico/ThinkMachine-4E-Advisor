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

package com.softwaremagico.tm.advisor.ui.load;

import android.content.Context;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.factions.Faction;

import java.util.Locale;

/**
 * Resolves the drawable resource used to represent a faction logo in the character loader.
 */
public final class FactionLogoSelection {

    private FactionLogoSelection() {

    }

    public static int getLogo(Context context, Faction faction) {
        if (faction != null && faction.getId() != null) {
            try {
                final int id = context.getResources().getIdentifier("ic_" + faction.getId().toLowerCase(Locale.getDefault()),
                        "drawable", context.getPackageName());
                if (id > 0) {
                    return id;
                }
            } catch (Exception e) {
                //Logo does not exists.
            }
        }
        return R.drawable.ic_empty;
    }
}
