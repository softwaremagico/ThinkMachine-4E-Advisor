package com.softwaremagico.tm.advisor.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softwaremagico.tm.ObjectMapperFactory;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidJsonException;

public class CharacterJsonManager {

    public static String toJson(CharacterPlayer characterPlayer) {
        try {
            return ObjectMapperFactory.getJsonObjectMapper().writeValueAsString(characterPlayer);
        } catch (JsonProcessingException e) {
            AdvisorLog.errorMessage(CharacterJsonManager.class.getName(), e);
            throw new RuntimeException(e);
        }

    }

    public static CharacterPlayer fromJson(String jsonText) throws InvalidJsonException {
        try {
            return ObjectMapperFactory.getJsonObjectMapper().readValue(jsonText, CharacterPlayer.class);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException(e.getMessage());
        }
    }
}
