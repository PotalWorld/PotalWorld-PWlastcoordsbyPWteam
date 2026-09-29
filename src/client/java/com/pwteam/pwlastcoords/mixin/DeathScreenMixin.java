package com.pwteam.pwlastcoords.mixin;

import com.pwteam.pwlastcoords.PWLastCoordsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    protected DeathScreenMixin(Component title) {
        super(title);
    }

    private String pwlastcoords$coordinates;
    private boolean pwlastcoords$copied;
    private long pwlastcoords$copyTime;

    @Inject(method = "init", at = @At("TAIL"))
    private void pwlastcoords$init(CallbackInfo ci) {
        if (!PWLastCoordsClient.enabled) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        int x = minecraft.player.getBlockX();
        int y = minecraft.player.getBlockY();
        int z = minecraft.player.getBlockZ();

        pwlastcoords$coordinates =
            "X: " + x +
            " Y: " + y +
            " Z: " + z;

        pwlastcoords$copied = false;
        pwlastcoords$copyTime = 0L;
    }

    private int pwlastcoords$getCoordinatesY() {
        return 115;
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void pwlastcoords$render(
        GuiGraphicsExtractor graphics,
        int mouseX,
        int mouseY,
        float partialTick,
        CallbackInfo ci
    ) {
        if (!PWLastCoordsClient.enabled
            || pwlastcoords$coordinates == null) {
            return;
        }

        if (pwlastcoords$copied
            && System.currentTimeMillis() - pwlastcoords$copyTime >= 2000L) {
            pwlastcoords$copied = false;
        }

        int coordinatesY = pwlastcoords$getCoordinatesY();

        if (pwlastcoords$copied) {
            String copiedText = Component
                .translatable("pwlastcoords.coordinates_copied")
                .getString();

            int copiedWidth = font.width(copiedText);

            graphics.text(
                font,
                copiedText,
                (width - copiedWidth) / 2,
                coordinatesY,
                0xFF55FF55,
                true
            );

            return;
        }

        String label = Component
            .translatable("pwlastcoords.coordinates")
            .getString();

        String coordinates = pwlastcoords$coordinates;

        int labelWidth = font.width(label);
        int spaceWidth = font.width(" ");
        int coordinatesWidth = font.width(coordinates);

        int totalWidth = labelWidth + spaceWidth + coordinatesWidth;
        int startX = (width - totalWidth) / 2;
        int coordinatesX = startX + labelWidth + spaceWidth;

        graphics.text(
            font,
            label,
            startX,
            coordinatesY,
            0xFFFFFFFF,
            true
        );

        boolean hovered =
            mouseX >= coordinatesX
            && mouseX <= coordinatesX + coordinatesWidth
            && mouseY >= coordinatesY
            && mouseY <= coordinatesY + font.lineHeight;

        if (hovered) {
            graphics.fill(
                coordinatesX - 2,
                coordinatesY - 2,
                coordinatesX + coordinatesWidth + 2,
                coordinatesY + font.lineHeight + 2,
                0x3322FF22
            );
        }

        graphics.text(
            font,
            coordinates,
            coordinatesX,
            coordinatesY,
            0xFF55FF55,
            true
        );
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void pwlastcoords$mouseClicked(
        MouseButtonEvent event,
        boolean doubleClick,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (!PWLastCoordsClient.enabled
            || pwlastcoords$coordinates == null
            || pwlastcoords$copied) {
            return;
        }

        int coordinatesY = pwlastcoords$getCoordinatesY();

        String label = Component
            .translatable("pwlastcoords.coordinates")
            .getString();

        int labelWidth = font.width(label);
        int spaceWidth = font.width(" ");
        int coordinatesWidth = font.width(pwlastcoords$coordinates);

        int totalWidth = labelWidth + spaceWidth + coordinatesWidth;
        int startX = (width - totalWidth) / 2;
        int coordinatesX = startX + labelWidth + spaceWidth;

        double mouseX = event.x();
        double mouseY = event.y();

        if (mouseX >= coordinatesX
            && mouseX <= coordinatesX + coordinatesWidth
            && mouseY >= coordinatesY
            && mouseY <= coordinatesY + font.lineHeight
            && event.button() == 0) {

            Minecraft.getInstance().keyboardHandler.setClipboard(
                pwlastcoords$coordinates
            );

            pwlastcoords$copied = true;
            pwlastcoords$copyTime = System.currentTimeMillis();

            cir.setReturnValue(true);
        }
    }
}