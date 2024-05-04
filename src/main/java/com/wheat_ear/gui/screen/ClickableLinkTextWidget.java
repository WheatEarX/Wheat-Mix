package com.wheat_ear.gui.screen;

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
        if (!super.mouseClicked(mouseX, mouseY, button) && button == 1) {
            rightButtonAction();
        }
        return true;
    }

    public void rightButtonAction() {

    }
}
