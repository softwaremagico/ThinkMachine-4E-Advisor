package com.softwaremagico.tm.advisor.ui.character.calling;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class CallingFragmentCharacterTest {

    @Test
    public void isCallingSelectionDisabled_returnsFalse() {
        // Arrange
        // Act
        final boolean disabled = CallingFragmentCharacter.isCallingSelectionDisabled();

        // Assert
        assertFalse(disabled);
    }
}


