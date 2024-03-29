package com.wheat_ear.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorScreen extends Screen {
    private static final Text TITLE = Text.translatable("menu.calculator");
    private static final int[] NUMBER_ORDER = {7, 8, 9, 4, 5, 6, 1, 2, 3, 0};
    private static final ButtonType[] BUTTON_ORDER = {
            ButtonType.SQUARE_ROOT, ButtonType.ALL_CLEAR, ButtonType.BACKSPACE, ButtonType.DIVIDE,
            ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.MULTIPLY,
            ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.SUBTRACT,
            ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.ADD,
            ButtonType.NEGATIVE, ButtonType.NUMBER, ButtonType.POINT, ButtonType.EQUALS
    };
    private static TextFieldWidget textField;
    private static BigDecimal number1, number2;
    private static Operator operator;
    private static InputMode inputMode = InputMode.FIRST;

    public CalculatorScreen(MinecraftClient client) {
        super(TITLE);

        this.client = client;
        textRenderer = this.client.textRenderer;

        textField = new TextFieldWidget(textRenderer, 400, 300, 130, 15, Text.empty());

        addDrawableChild(textField);
    }

    @Override
    protected void init() {
        textField.active = false;
        textField.visible = true;

        GridWidget gridWidget = new GridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(3);

        adder.add(textField, 4);
        addNumbers(adder);

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    private void addNumbers(GridWidget.Adder adder) {
        int numberIndex = 0;
        for (ButtonType buttonType: BUTTON_ORDER) {
            if (buttonType == ButtonType.NUMBER) {
                String number = Integer.toString(NUMBER_ORDER[numberIndex]);
                adder.add(ButtonWidget.builder(Text.of(number),
                        button -> textField.write(number)).width(30).build());
                ++numberIndex;
            }
            else {
                adder.add(buttonType.getButton());
            }
        }
    }

    private static void setNumber() {
        String s = textField.getText();
        if (inputMode == InputMode.FIRST) {
            number1 = new BigDecimal(s);
        }
        else if (inputMode == InputMode.SECOND) {
            number2 = new BigDecimal(s);
        }
        textField.setText("");
    }

    enum ButtonType {
        NUMBER,
        BACKSPACE,
        ADD("+", button -> {
            setNumber();
            operator = Operator.ADD;
            inputMode = InputMode.SECOND;
        }),
        SUBTRACT("-", button -> {
            setNumber();
            operator = Operator.SUBTRACT;
            inputMode = InputMode.SECOND;
        }),
        MULTIPLY("*", button -> {
            setNumber();
            operator = Operator.MULTIPLY;
            inputMode = InputMode.SECOND;
        }),
        DIVIDE("/", button -> {
            setNumber();
            operator = Operator.DIVIDE;
            inputMode = InputMode.SECOND;
        }),
        ALL_CLEAR,
        EQUALS("=", button -> {
            setNumber();
            if (number2 != null) {
                textField.setText(operator.method.get().toString());
                number1 = new BigDecimal(textField.getText());
                inputMode = InputMode.FIRST;
            }
        }),
        POINT,
        NEGATIVE,
        SQUARE_ROOT;

        private String string;
        private ButtonWidget.PressAction action;

        ButtonType() {}
        ButtonType(String string, ButtonWidget.PressAction action) {
            this.string = string;
            this.action = action;
        }

        public ButtonWidget getButton() {
            return ButtonWidget.builder(Text.of(string), action).width(30).build();
        }
    }

    enum Operator {
        ADD(() -> number1.add(number2)),
        SUBTRACT(() -> number1.subtract(number2)),
        MULTIPLY(() -> number1.multiply(number2)),
        DIVIDE(() -> number1.divide(number2, 7, RoundingMode.HALF_EVEN));

        public final OperateAction<BigDecimal> method;

        Operator(OperateAction<BigDecimal> method) {
            this.method = method;
        }
    }

    enum InputMode {
        FIRST,
        SECOND
    }

    interface OperateAction<T> {
        T get();
    }
}
