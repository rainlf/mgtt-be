package com.rainlf.mgttbe.infra.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper defaultMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void writeFile(String file, Object obj) {
        try {
            defaultMapper.writeValue(new File(file), obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readFile(String file, Class<T> clazz) {
        try {
            return defaultMapper.readValue(new File(file), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readFile(String file, TypeReference<T> clazz) {
        try {
            return defaultMapper.readValue(new File(file), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String writeString(Object obj) {
        try {
            return defaultMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readString(String json, Class<T> clazz) {
        try {
            return defaultMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readString(String json, TypeReference<T> clazz) {
        try {
            return defaultMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
