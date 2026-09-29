package com.pwteam.pwlastcoords;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class PWLastCoordsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "pwlastcoords.json");

    public boolean enabled = true;

    public static PWLastCoordsConfig load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    PWLastCoordsConfig config = GSON.fromJson(reader, PWLastCoordsConfig.class);
                    if (config != null) {
                        return config;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return new PWLastCoordsConfig();
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception ignored) {
        }
    }
}