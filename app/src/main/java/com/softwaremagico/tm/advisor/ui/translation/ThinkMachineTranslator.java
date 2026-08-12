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

package com.softwaremagico.tm.advisor.ui.translation;

import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.txt.Text;
import com.softwaremagico.tm.txt.TextFactory;

public final class ThinkMachineTranslator {

    private ThinkMachineTranslator() {

    }


    public static String getTranslatedText(String tag) {
        if (tag == null || tag.isEmpty()) {
            return "";
        }
        try {
            final Text text = TextFactory.getInstance().getElement(tag);
            if (text == null) {
                return tag;
            }
            final String translated = text.getNameRepresentation();
            return translated == null ? tag : translated;
        } catch (InvalidXmlElementException e) {
            return tag;
        }
    }


    public static String getDescriptionText(String tag) {
        if (tag == null || tag.isEmpty()) {
            return "";
        }
        try {
            final Text text = TextFactory.getInstance().getElement(tag);
            if (text == null) {
                return "";
            }
            final String description = text.getDescriptionRepresentation();
            return description == null ? "" : description;
        } catch (InvalidXmlElementException e) {
            return "";
        }
    }

}
