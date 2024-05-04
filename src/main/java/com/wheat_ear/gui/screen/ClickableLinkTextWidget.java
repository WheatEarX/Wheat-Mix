package com.wheat_ear.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class ClickableLinkTextWidget extends TextWidget {

    public ClickableLinkTextWidget(Text message, TextRenderer textRenderer) {
        super(message, textRenderer);

        active = true;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        BrowserScreen.openLink(getMessage().getString());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (clicked(mouseX, mouseY)) {
            if (button == 0) {
                playDownSound(MinecraftClient.getInstance().getSoundManager());
                onClick(mouseX, mouseY);
                return true;
            }
            else if (button == 1) {
                playDownSound(MinecraftClient.getInstance().getSoundManager());
                rightButtonClick();
                return true;
            }
        }
        return false;
    }

    public void rightButtonClick() {

    }
}
