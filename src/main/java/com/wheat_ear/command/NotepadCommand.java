package com.wheat_ear.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.wheat_ear.others.NotepadConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class NotepadCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            literal("notepad")
                .then(literal("config")
                    .then(argument("path", StringArgumentType.greedyString())
                        .executes(NotepadCommand::executeConfig)
                    )
                )
                .then(literal("read")
                ).executes(NotepadCommand::executeRead)
                .then(literal("write")
                    .then(argument("content", StringArgumentType.greedyString())
                        .executes(NotepadCommand::executeWrite)
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

        return 1;
    }

    public static int executeRead(CommandContext<ServerCommandSource> context) {
        String line;

        File file;
        if (NotepadConfig.file != null) {
            file = NotepadConfig.file;
        }
        else {
            return 0;
        }

        FileReader reader;
        BufferedReader bufferedReader;

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);

            line = bufferedReader.readLine();

            while (line != null) {
                String finalLine = line;
                context.getSource().sendFeedback(() -> Text.of(finalLine), false);
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            reader.close();

        } catch (IOException e) {
            context.getSource().sendFeedback(() -> null, false);
            throw new RuntimeException(e);
        }

        return 1;
    }

    public static int executeWrite(CommandContext<ServerCommandSource> context) {
        String content = StringArgumentType.getString(context, "content");

        File file;
        if (NotepadConfig.file != null) {
            file = NotepadConfig.file;
        }
        else {
            return 0;
        }

        FileWriter writer;
        BufferedWriter bufferedWriter;

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);

            bufferedWriter.close();
            writer.close();

        } catch (IOException e) {
            context.getSource().sendFeedback(() -> null, false);
            throw new RuntimeException(e);
        }
        context.getSource().sendFeedback(() -> null, false);

        return 1;
    }
}
