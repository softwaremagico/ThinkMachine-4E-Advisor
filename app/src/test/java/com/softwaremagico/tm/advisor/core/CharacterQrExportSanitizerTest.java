package com.softwaremagico.tm.advisor.core;

import static org.junit.Assert.assertEquals;

import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Test;

public class CharacterQrExportSanitizerTest {

    @Test
    public void removeNullPurchasedEquipment_whenNullsPresent_removesThem() {
        final CharacterPlayer character = new CharacterPlayer();
        character.getEquipmentPurchased().add(null);
        character.getEquipmentPurchased().add(null);

        final int removedElements = CharacterQrExportSanitizer.removeNullPurchasedEquipment(character);

        assertEquals(1, removedElements);
        assertEquals(0, character.getEquipmentPurchased().size());
    }

    @Test
    public void removeNullPurchasedEquipment_whenNoNullsPresent_returnsZero() {
        final CharacterPlayer character = new CharacterPlayer();

        final int removedElements = CharacterQrExportSanitizer.removeNullPurchasedEquipment(character);

        assertEquals(0, removedElements);
    }
}
