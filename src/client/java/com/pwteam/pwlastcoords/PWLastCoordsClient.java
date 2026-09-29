package com.pwteam.pwlastcoords;

import net.fabricmc.api.ClientModInitializer;

public class PWLastCoordsClient implements ClientModInitializer {
    public static boolean enabled;

    public static PWLastCoordsConfig config;

    public static int lastX;
    public static int lastY;
    public static int lastZ;
    public static boolean hasCoordinates;

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