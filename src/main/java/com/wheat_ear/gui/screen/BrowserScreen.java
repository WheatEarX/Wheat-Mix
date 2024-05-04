package com.wheat_ear.gui.screen;

import com.wheat_ear.item.BrowserItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.LinkedHashSet;

public class BrowserScreen extends Screen {
    private static final Text TITLE = Text.translatable("menu.browser");
    private static final Text DONE = Text.translatable("gui.done");
    private static final Text COLLECT = Text.translatable("gui.collect");
    private static final Text HISTORY = Text.translatable("gui.histories");
    private static final Text FAVORITES = Text.translatable("gui.favorites");
    private final BrowserItem browserItem;
    private final TextFieldWidget textField;

    public BrowserScreen(MinecraftClient client, BrowserItem browserItem) {
        super(TITLE);

        this.browserItem = browserItem;

        textField = new TextFieldWidget(client.textRenderer, 200, 20, Text.empty());
    }

    @Override
    public void init() {
        addDrawableChild(textField).setPosition(width / 2 - 100, 80);

        addDrawableChild(ButtonWidget.builder(COLLECT, button -> {
            String str = textField.getText();

            if (!str.isEmpty()) {
                LinkedHashSet<String> favorites = browserItem.getFavorites();
                favorites.add(str);
            }
        }).dimensions(width / 2 - 80, 110, 80, 20).build());

        addDrawableChild(ButtonWidget.builder(DONE, button -> {
            String str = textField.getText();

            if (!str.isEmpty()) {
                openLink(str);

                LinkedHashSet<String> histories = browserItem.getHistories();
                histories.add(textField.getText());
            }
        }).dimensions(width / 2, 110, 80, 20).build());

        addDrawableChild(ButtonWidget.builder(HISTORY, button -> {
            LinkedHashSet<String> histories = browserItem.getHistories();
            if (client != null) {
                client.setScreen(new BrowserHistoriesScreen(client, this, histories));
            }
        }).dimensions(width / 2 - 80, 130, 80, 20).build());

        addDrawableChild(ButtonWidget.builder(FAVORITES, button -> {
            LinkedHashSet<String> favorites = browserItem.getFavorites();
            if (client != null) {
                client.setScreen(new BrowserFavoritesScreen(client, this, favorites));
            }
        }).dimensions(width / 2, 130, 80, 20).build());
    }

    public static void openLink(String str) {
        if (!(str.startsWith("https://") || str.startsWith("http://"))) {
            str = "https://" + str;
        }

        Util.getOperatingSystem().open(str);
    }
}
