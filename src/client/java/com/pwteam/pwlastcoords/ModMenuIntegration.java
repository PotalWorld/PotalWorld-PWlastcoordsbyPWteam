package com.pwteam.pwlastcoords;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PWLastCoordsConfigScreen::new;
    }

    private static class PWLastCoordsConfigScreen extends Screen {
        private final Screen parent;

        protected PWLastCoordsConfigScreen(Screen parent) {
            super(Component.literal("PWLastCoords"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            addRenderableWidget(
                Button.builder(
                    Component.literal(
                        "PWLastCoords: " +
                        (PWLastCoordsClient.enabled ? "Включено" : "Выключено")
                    ),
                    button -> {
                        PWLastCoordsClient.enabled = !PWLastCoordsClient.enabled;
                        PWLastCoordsClient.saveConfig();

                        button.setMessage(
                            Component.literal(
                                "PWLastCoords: " +
                                (PWLastCoordsClient.enabled ? "Включено" : "Выключено")
                            )
                        );
                    }
                ).bounds(width / 2 - 100, height / 2 - 10, 200, 20).build()
            );

            addRenderableWidget(
                Button.builder(
                    Component.literal("Готово"),
                    button -> minecraft.setScreen(parent)
                ).bounds(width / 2 - 100, height / 2 + 20, 200, 20).build()
            );
        }

        @Override
        public void onClose() {
            minecraft.setScreen(parent);
        }
    }
}