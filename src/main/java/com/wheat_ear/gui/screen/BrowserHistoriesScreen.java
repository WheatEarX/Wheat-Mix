package com.wheat_ear.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

@SuppressWarnings({"StackOverflowIssue", "DataFlowIssue"})
public class BrowserHistoriesScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.histories");
    private final ArrayList<String> histories;
    private final Screen parent;

    public BrowserHistoriesScreen(MinecraftClient client, Screen parent, ArrayList<String> histories) {
        super(TITLE);

        this.histories = histories;
        this.parent = parent;
        this.client = client;
    }

    @Override
    public void init() {
        TextWidget titleWidget = new TextWidget(title, textRenderer);
        titleWidget.setPosition(width / 2, 20);
        addDrawableChild(titleWidget);

        HistoriesDeletableTextWidget historiesDeletableTextWidget;

        for (int i = 0; i < histories.size(); ++i) {
            historiesDeletableTextWidget = new HistoriesDeletableTextWidget(Text.literal(histories.get(i)), client.textRenderer, i);
            historiesDeletableTextWidget.alignCenter();
            historiesDeletableTextWidget.setPosition(width / 2 - historiesDeletableTextWidget.getWidth() / 2, i * 9 + 40);
            if (i % 2 == 1) {
                historiesDeletableTextWidget.setTextColor(7);
            }

            addDrawableChild(historiesDeletableTextWidget);
        }
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private class HistoriesDeletableTextWidget extends ClickableLinkTextWidget {
        private final int index;
        public HistoriesDeletableTextWidget(Text message, TextRenderer textRenderer, int index) {
            super(message, textRenderer);

            this.index = index;
        }

        @Override
        public void rightButtonAction() {
            histories.remove(index);
            client.setScreen(new BrowserFavoritesScreen(client, parent, histories));
        }
    }
}
