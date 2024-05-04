package com.wheat_ear.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.LinkedHashSet;

@SuppressWarnings({"StackOverflowIssue", "DataFlowIssue"})
public class BrowserFavoritesScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.favorites");
    private final LinkedHashSet<String> favorites;
    private final Screen parent;

    public BrowserFavoritesScreen(MinecraftClient client, Screen parent, LinkedHashSet<String> favorites) {
        super(TITLE);

        this.favorites = favorites;
        this.parent = parent;
        this.client = client;
    }

    @Override
    public void init() {
        TextWidget titleWidget = new TextWidget(title, textRenderer);
        titleWidget.setPosition(width / 2 - titleWidget.getWidth() / 2, 10);
        addDrawableChild(titleWidget);

        FavoritesDeletableTextWidget favoritesDeletableTextWidget;

        int i = 0;
        for (String str: favorites) {
            favoritesDeletableTextWidget = new FavoritesDeletableTextWidget(Text.literal(str), client.textRenderer);
            favoritesDeletableTextWidget.alignCenter();
            favoritesDeletableTextWidget.setPosition(width / 2 - favoritesDeletableTextWidget.getWidth() / 2, i * 12 + 30);
            if (i % 2 == 1) {
                favoritesDeletableTextWidget.setTextColor(0x909090);
            }

            addDrawableChild(favoritesDeletableTextWidget);
            System.out.println(i * 12 + 30);
            ++i;
        }
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private class FavoritesDeletableTextWidget extends ClickableLinkTextWidget {
        public FavoritesDeletableTextWidget(Text message, TextRenderer textRenderer) {
            super(message, textRenderer);
        }

        @Override
        public void rightButtonClick() {
            favorites.remove(getMessage().getString());
            client.setScreen(new BrowserFavoritesScreen(client, parent, favorites));
        }
    }
}
