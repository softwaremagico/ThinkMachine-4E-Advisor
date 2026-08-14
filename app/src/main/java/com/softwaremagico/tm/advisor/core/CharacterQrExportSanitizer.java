package com.softwaremagico.tm.advisor.core;

import com.softwaremagico.tm.character.CharacterPlayer;

public final class CharacterQrExportSanitizer {
    private CharacterQrExportSanitizer() {
    }

    public static int removeNullPurchasedEquipment(CharacterPlayer character) {
        if (character == null || character.getEquipmentPurchased() == null) {
            return 0;
        }

        int removedElements = 0;
        while (character.getEquipmentPurchased().remove(null)) {
            removedElements++;
        }
        return removedElements;
    }
}
