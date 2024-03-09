package com.wheat_ear.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.wheat_ear.others.NotepadConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class NotepadItem extends Item {

    public static MutableText READ_FAIL = Text.translatable("text.wheat-mix.notepad.read.fail");
    public static MutableText READ_SUCCESS = Text.translatable("text.wheat-mix.notepad.read.success");
    public static MutableText WRITE_FAIL = Text.translatable("text.wheat-mix.notepad.write.fail");
    public static MutableText WRITE_SUCCESS = Text.translatable("text.wheat-mix.notepad.write.success");
    public static MutableText APPEND_FAIL = Text.translatable("text.wheat-mix.notepad.append.fail");
    public static MutableText APPEND_SUCCESS = Text.translatable("text.wheat-mix.notepad.append.success");
    public static MutableText CLEAR_FAIL = Text.translatable("text.wheat-mix.notepad.clear.fail");
    public static MutableText CLEAR_SUCCESS = Text.translatable("text.wheat-mix.notepad.clear.success");
    public static NotepadState[] STATE_ORDER = new NotepadState[]{NotepadState.READ, NotepadState.WRITE, NotepadState.APPEND, NotepadState.CLEAR};

    public NotepadState state = NotepadState.READ;

    public NotepadItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack offHandStack = user.getOffHandStack();
        ItemStack mainHandStack = user.getMainHandStack();
        if (offHandStack.isOf(Items.WRITABLE_BOOK) || offHandStack.isOf(Items.WRITTEN_BOOK)) {
            NbtCompound compound = offHandStack.getNbt();

            if (compound != null) {
                switch (state) {
                    case READ -> {
                        return read(user, offHandStack, mainHandStack);
                    }
                    case WRITE -> {
                        return write(user, offHandStack, mainHandStack);
                    }
                    case APPEND -> {
                        return append(user, offHandStack, mainHandStack);
                    }
                    case CLEAR -> {
                        return clear(user, mainHandStack);
                    }
                }
            }
        }
        else if (state.equals(NotepadState.CLEAR)) {
            return clear(user, offHandStack);
        }

        return TypedActionResult.pass(offHandStack);
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public static TypedActionResult<ItemStack> read(PlayerEntity user, ItemStack offHandStack, ItemStack mainHandStack) {
        String line;
        String result;
        String writerString = "";
        StringBuilder builder = new StringBuilder();

        File file;
        if (NotepadConfig.file != null) {
            file = NotepadConfig.file;
        }
        else {
            return TypedActionResult.pass(mainHandStack);
        }

        FileReader reader;
        BufferedReader bufferedReader;

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);

            line = bufferedReader.readLine();

            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }

            result = builder.toString();
            for (String s: result.split("\n")) {
                writerString += "[\"%s\"],".formatted(s);
            }
            writerString = writerString.substring(0, writerString.length() - 1);
            writerString = "{\"content\": %s}".formatted(writerString);
            try {
                offHandStack.setSubNbt("pages", NbtHelper.fromNbtProviderString(writerString).get("content"));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            bufferedReader.close();
            reader.close();

        } catch (IOException e) {
            user.sendMessage(READ_FAIL);
            throw new RuntimeException(e);
        }
        user.sendMessage(READ_SUCCESS);
        return TypedActionResult.success(mainHandStack);
    }

    public static TypedActionResult<ItemStack> write(PlayerEntity user, ItemStack offHandStack, ItemStack mainHandStack) {
        NbtCompound compound = offHandStack.getNbt();
        String content;

        StringBuilder builder = new StringBuilder();
        NbtList list = Objects.requireNonNull(compound).getList("pages", NbtElement.STRING_TYPE);
        for (NbtElement element: list) {
            builder.append(element.asString()).append("\n");
        }
        content = builder.toString();
        File file;
        if (NotepadConfig.file != null) {
            file = NotepadConfig.file;
        }
        else {
            return TypedActionResult.pass(mainHandStack);
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
            user.sendMessage(WRITE_FAIL);
            throw new RuntimeException(e);
        }
        user.sendMessage(WRITE_SUCCESS);
        return TypedActionResult.success(mainHandStack);
    }

    public static TypedActionResult<ItemStack> append(PlayerEntity user, ItemStack offHandStack, ItemStack mainHandStack) {
        File file;
        if (NotepadConfig.file != null) {
            file = NotepadConfig.file;
        }
        else {
            return TypedActionResult.pass(mainHandStack);
        }

        FileReader reader;
        BufferedReader bufferedReader;

        FileWriter writer;
        BufferedWriter bufferedWriter;

        String line;
        String result;
        StringBuilder builder = new StringBuilder();

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);

            line = bufferedReader.readLine();

            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            reader.close();

        } catch (IOException e) {
            user.sendMessage(READ_FAIL);
            throw new RuntimeException(e);
        }

        result = builder.toString();



        NbtCompound compound = offHandStack.getNbt();

        builder = new StringBuilder();
        NbtList list = Objects.requireNonNull(compound).getList("pages", NbtElement.STRING_TYPE);
        for (NbtElement element : list) {
            builder.append(element.asString()).append("\n");
        }
        result += builder.toString();

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(result);

            bufferedWriter.close();
            writer.close();

        } catch (IOException e) {
            user.sendMessage(APPEND_FAIL);
            throw new RuntimeException(e);
        }
        user.sendMessage(APPEND_SUCCESS);
        return TypedActionResult.success(mainHandStack);
    }

    public static TypedActionResult<ItemStack> clear(PlayerEntity user, ItemStack stack) {
        File file;
        if (NotepadConfig.file != null) {
            file = NotepadConfig.file;
        }
        else {
            return TypedActionResult.pass(stack);
        }

        FileWriter writer;
        BufferedWriter bufferedWriter;

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("");

            bufferedWriter.close();
            writer.close();

        } catch (IOException e) {
            user.sendMessage(CLEAR_FAIL);
            throw new RuntimeException(e);
        }
        user.sendMessage(CLEAR_SUCCESS);
        return TypedActionResult.success(stack);
    }

    public static void changeMode(ItemStack stack) {
        if (stack.getItem() instanceof NotepadItem notepadItem) {
            notepadItem.state = Arrays.stream(STATE_ORDER).filter((notepadState -> {
                int id = notepadItem.state.getId();
                if (id >= 3) return notepadState.id == 0;
                else return notepadState.id == id + 1;
            })).toList().get(0);
        }
    }

    public enum NotepadState {

        READ(Text.translatable("text.wheat-mix.notepad.read"), 0),
        WRITE(Text.translatable("text.wheat-mix.notepad.write"), 1),
        APPEND(Text.translatable("text.wheat-mix.notepad.append"), 2),
        CLEAR(Text.translatable("text.wheat-mix.notepad.clear"), 3);

        private final MutableText text;
        private final int id;

        NotepadState(MutableText text, int id) {
            this.text = text;
            this.id = id;
        }

        public MutableText getText() {
            return text;
        }

        public int getId() {
            return id;
        }
    }
}
