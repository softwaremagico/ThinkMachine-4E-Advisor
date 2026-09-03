package com.softwaremagico.tm.advisor.ui.character.level;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevelFragmentCharacterTest {

    @Test
    public void getSelectedCallingId_explicitValue_returnsExplicitCalling() {
        // Arrange
        final String explicitCallingId = "soldier";
        final String inheritedCallingId = "mystic";

        // Act
        final String selectedCallingId = LevelFragmentCharacter.getSelectedCallingId(explicitCallingId, inheritedCallingId);

        // Assert
        assertEquals(explicitCallingId, selectedCallingId);
    }

    @Test
    public void getSelectedCallingId_missingExplicitValue_returnsInheritedCalling() {
        // Arrange
        final String explicitCallingId = null;
        final String inheritedCallingId = "mystic";

        // Act
        final String selectedCallingId = LevelFragmentCharacter.getSelectedCallingId(explicitCallingId, inheritedCallingId);

        // Assert
        assertEquals(inheritedCallingId, selectedCallingId);
    }
}

