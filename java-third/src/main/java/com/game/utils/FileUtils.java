package com.game.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileUtils {
    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = FileUtils.class.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new Exception("Resource not found: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                result = reader.lines().collect(Collectors.joining("\n"));
            }
        }
        return result;
    }
}