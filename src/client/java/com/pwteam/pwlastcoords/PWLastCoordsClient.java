package com.pwteam.pwlastcoords;

import net.fabricmc.api.ClientModInitializer;

public class PWLastCoordsClient implements ClientModInitializer {
    public static boolean enabled;

    public static PWLastCoordsConfig config;

    @Override
    public void onInitializeClient() {
        config = PWLastCoordsConfig.load();
        enabled = config.enabled;
    }

    public static void saveConfig() {
        if (config != null) {
            config.enabled = enabled;
            config.save();
        }
    }
}