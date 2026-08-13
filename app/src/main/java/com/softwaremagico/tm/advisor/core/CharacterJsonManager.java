package com.softwaremagico.tm.advisor.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.softwaremagico.tm.ObjectMapperFactory;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidJsonException;

public class CharacterJsonManager {
    private static final ObjectMapper JSON_MAPPER = ObjectMapperFactory.getJsonObjectMapper().copy()
            .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    public static String toJson(CharacterPlayer characterPlayer) {
        try {
            return JSON_MAPPER.writeValueAsString(characterPlayer);
        } catch (JsonProcessingException e) {
            AdvisorLog.errorMessage(CharacterJsonManager.class.getName(), e);
            throw new RuntimeException(e);
        }

    }

    public static CharacterPlayer fromJson(String jsonText) throws InvalidJsonException {
        if (jsonText == null || jsonText.isBlank()) {
            throw new InvalidJsonException("JSON content is empty.");
        }
        try {
            final CharacterPlayer characterPlayer = JSON_MAPPER.readValue(jsonText, CharacterPlayer.class);
            if (characterPlayer == null) {
                throw new InvalidJsonException("JSON content does not contain a character.");
            }
            return characterPlayer;
        } catch (JsonProcessingException | IllegalArgumentException e) {
            throw new InvalidJsonException(e.getMessage());
        }
    }
}
