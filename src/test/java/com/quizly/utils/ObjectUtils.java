package com.quizly.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectUtils {

    public static String convertObjectToJsonBytes(final Object object) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        final ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();

        return objectWriter.writeValueAsString(object);
    }
}
