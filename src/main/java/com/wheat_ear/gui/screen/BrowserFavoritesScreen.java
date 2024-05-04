package com.wheat_ear.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

@SuppressWarnings({"StackOverflowIssue", "DataFlowIssue"})
public class BrowserFavoritesScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.favorites");
    private final ArrayList<String> favorites;
    private final Screen parent;

    public BrowserFavoritesScreen(MinecraftClient client, Screen parent, ArrayList<String> favorites) {
        super(TITLE);

        this.favorites = favorites;
        this.parent = parent;
        this.client = client;
    }

    @Override
    public void init() {
        TextWidget titleWidget = new TextWidget(title, textRenderer);
        titleWidget.setPosition(width / 2, 20);
        addDrawableChild(titleWidget);

        FavoritesDeletableTextWidget favoritesDeletableTextWidget;

        for (int i = 0; i < favorites.size(); ++i) {
            favoritesDeletableTextWidget = new FavoritesDeletableTextWidget(Text.literal(favorites.get(i)), client.textRenderer, i);
            favoritesDeletableTextWidget.alignCenter();
            favoritesDeletableTextWidget.setPosition(width / 2 - favoritesDeletableTextWidget.getWidth() / 2, i * 9 + 30);
            if (i % 2 == 1) {
                favoritesDeletableTextWidget.setTextColor(7);
            }

            addDrawableChild(favoritesDeletableTextWidget);
        }
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private class FavoritesDeletableTextWidget extends ClickableLinkTextWidget {
        private final int index;
        public FavoritesDeletableTextWidget(Text message, TextRenderer textRenderer, int index) {
            super(message, textRenderer);

            this.index = index;
        }

        @Override
        public void rightButtonAction() {
            favorites.remove(index);
            client.setScreen(new BrowserFavoritesScreen(client, parent, favorites));
        }
    }
}
