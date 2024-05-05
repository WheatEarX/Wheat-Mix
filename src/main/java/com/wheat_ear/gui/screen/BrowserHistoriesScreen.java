package com.wheat_ear.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.LinkedHashSet;

@SuppressWarnings({"StackOverflowIssue", "DataFlowIssue"})
public class BrowserHistoriesScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.histories");
    private final LinkedHashSet<String> histories;
    private final Screen parent;

    public BrowserHistoriesScreen(MinecraftClient client, Screen parent, LinkedHashSet<String> histories) {
        super(TITLE);

        this.histories = histories;
        this.parent = parent;
        this.client = client;
    }

    @Override
    public void init() {
        TextWidget titleWidget = new TextWidget(title, textRenderer);
        titleWidget.setPosition(width / 2 - titleWidget.getWidth() / 2, 10);
        addDrawableChild(titleWidget);

        HistoriesDeletableTextWidget historiesDeletableTextWidget;

        int i = 0;
        for (String str: histories) {
            historiesDeletableTextWidget = new HistoriesDeletableTextWidget(Text.literal(str), client.textRenderer);
            historiesDeletableTextWidget.alignCenter();
            historiesDeletableTextWidget.setPosition(width / 2 - historiesDeletableTextWidget.getWidth() / 2, i * 12 + 40);
            if (i % 2 == 1) {
                historiesDeletableTextWidget.setTextColor(0x909090);
            }

            addDrawableChild(historiesDeletableTextWidget);
            ++i;
        }
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private class HistoriesDeletableTextWidget extends ClickableLinkTextWidget {
        public HistoriesDeletableTextWidget(Text message, TextRenderer textRenderer) {
            super(message, textRenderer);
        }

        @Override
        public void rightButtonClick() {
            histories.remove(getMessage().getString());
            client.setScreen(new BrowserHistoriesScreen(client, parent, histories));
        }
    }
}
