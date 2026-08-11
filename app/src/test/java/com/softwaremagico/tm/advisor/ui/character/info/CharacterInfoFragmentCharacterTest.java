package com.softwaremagico.tm.advisor.ui.character.info;

import com.softwaremagico.tm.character.Gender;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class CharacterInfoFragmentCharacterTest {

    private CharacterInfoFragmentCharacter fragment;

    @Before
    public void setUp() {
        fragment = new CharacterInfoFragmentCharacter();
    }

    @Test
    public void getSafeElementAt_withNullList_returnsNull() {
        assertNull("Should return null for null list", fragment.getSafeElementAt(null, 0));
    }

    @Test
    public void getSafeElementAt_withNegativePosition_returnsNull() {
        List<Gender> list = new ArrayList<>();
        list.add(Gender.MALE);
        assertNull("Should return null for negative position", fragment.getSafeElementAt(list, -1));
    }

    @Test
    public void getSafeElementAt_withOutOfBoundsPosition_returnsNull() {
        List<Gender> list = new ArrayList<>();
        list.add(Gender.MALE);
        assertNull("Should return null for out of bounds position", fragment.getSafeElementAt(list, 10));
    }

    @Test
    public void getSafeElementAt_withValidPosition_returnsElement() {
        List<Gender> list = new ArrayList<>();
        list.add(Gender.MALE);
        assertEquals("Should return element at valid position", Gender.MALE, fragment.getSafeElementAt(list, 0));
    }

    @Test
    public void getSafeElementAt_withEmptyList_returnsNull() {
        List<Gender> list = new ArrayList<>();
        assertNull("Should return null for empty list", fragment.getSafeElementAt(list, 0));
    }
}
