package com.softwaremagico.tm.advisor.persistence;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class CharacterHandlerTest {

    @Test
    public void mapEntitiesByCharacter_onlyInvalidCharacters_returnsEmptyMap() {
        final CharacterEntity invalidEntity = new CharacterEntity();
        invalidEntity.setJson("{ invalid json }");
        invalidEntity.setName("broken");

        final Map<?, CharacterEntity> mappedEntities = CharacterHandler.mapEntitiesByCharacter(Arrays.asList(invalidEntity, null));

        assertTrue(mappedEntities.isEmpty());
    }

    @Test
    public void mapEntitiesByCharacter_withNullList_returnsEmptyMap() {
        final Map<?, CharacterEntity> mappedEntities = CharacterHandler.mapEntitiesByCharacter(null);

        assertTrue(mappedEntities.isEmpty());
    }
}
