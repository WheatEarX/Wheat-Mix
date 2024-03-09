package com.wheat_ear.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.wheat_ear.others.NotepadConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.io.File;
import java.io.IOException;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class NotepadCommand {
    private static final MutableText FEEDBACK = Text.translatable("commands.notepad.config");

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            literal("notepad")
                .then(literal("config")
                    .then(argument("path", StringArgumentType.greedyString())
                        .executes(NotepadCommand::executeConfig)
                    )
                )
            )
        );
    }

    public static int executeConfig(CommandContext<ServerCommandSource> context) {
        String filename = StringArgumentType.getString(context, "path");
        File file = new File(filename);

        if (!file.exists()) {
            try {
                boolean succeed = file.createNewFile();
                if (!succeed) {
                    throw new RuntimeException("");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        NotepadConfig.file = file;
        context.getSource().sendFeedback(() -> FEEDBACK, false);

        return 1;
    }
}
