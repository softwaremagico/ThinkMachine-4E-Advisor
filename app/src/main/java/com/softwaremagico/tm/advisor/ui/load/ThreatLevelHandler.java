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

import androidx.core.content.ContextCompat;

import com.softwaremagico.tm.advisor.R;

public class ThreatLevelHandler {
    private static final String HEXADECIMAL_FORMAT = "#%06x";
    private final int threatLevel;

    public ThreatLevelHandler(int threatLevel) {
        this.threatLevel = threatLevel;
    }

    public String getThreatLevel() {
        if (threatLevel < 40) {
            return "☮☮☮☮";
        } else if (threatLevel < 60) {
            return "☮☮☮";
        } else if (threatLevel < 80) {
            return "☮☮";
        } else if (threatLevel < 100) {
            return "☮";
        } else if (threatLevel < 120) {
            return "☠";
        } else if (threatLevel < 150) {
            return "☠☠";
        } else if (threatLevel < 175) {
            return "☠☠☠";
        } else if (threatLevel < 200) {
            return "☠☠☠☠";
        } else if (threatLevel < 250) {
            return "☠☠☠☠☠";
        }
        return "☠☠☠☠☠☠";
    }

    public String getColor(Context context) {
        if (threatLevel < 100) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatPeace) & 0xffffff);
        } else if (threatLevel < 125) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatLow) & 0xffffff);
        } else if (threatLevel < 150) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatMedium) & 0xffffff);
        } else if (threatLevel < 200) {
            return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatHigh) & 0xffffff);
        }
        return String.format(HEXADECIMAL_FORMAT, ContextCompat.getColor(context, R.color.threatExtreme) & 0xffffff);
    }
}
