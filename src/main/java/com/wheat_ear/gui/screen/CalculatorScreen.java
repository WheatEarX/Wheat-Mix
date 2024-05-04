package com.wheat_ear.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class CalculatorScreen extends Screen {
    private static final Text TITLE = Text.translatable("menu.calculator");
    private static final int BUTTON_WIDTH = 25;
    private static final int[] NUMBER_ORDER = {7, 8, 9, 4, 5, 6, 1, 2, 3, 0};
    private static final ButtonType[] BUTTON_ORDER = {
            ButtonType.PI, ButtonType.E, ButtonType.REVERSE, ButtonType.ABS, ButtonType.MOD,
            ButtonType.EXP, ButtonType.POWER, ButtonType.POWER2, ButtonType.POWER3, ButtonType.BACKSPACE,
            ButtonType.SIN, ButtonType.SQUARE_ROOT, ButtonType.CUBE_ROOT, ButtonType.ALL_CLEAR, ButtonType.DIVIDE,
            ButtonType.COS, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.MULTIPLY,
            ButtonType.TAN, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.SUBTRACT,
            ButtonType.LOG, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.ADD,
            ButtonType.LOG10, ButtonType.NEGATE, ButtonType.NUMBER, ButtonType.POINT, ButtonType.EQUALS
    };
    private static TextFieldWidget textField;
    private static BigDecimal number1, number2;
    private static Operator operator;
    private static final GridWidget gridWidget = new GridWidget();
    private static InputMode inputMode = InputMode.FIRST;
    private static int operateCount = 0;

    public CalculatorScreen(MinecraftClient client) {
        super(TITLE);

        this.client = client;
        textRenderer = this.client.textRenderer;

        textField = new TextFieldWidget(textRenderer, 400, 300, 125, 15, Text.empty());

        addDrawableChild(textField);
    }

    @Override
    protected void init() {
        textField.active = false;
        textField.visible = true;

        GridWidget.Adder adder = gridWidget.createAdder(5);

        adder.add(textField, 5);
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
                        button -> textField.write(number)).width(BUTTON_WIDTH).build());
                ++numberIndex;
            }
            else {
                adder.add(buttonType.getButton());
            }
        }
    }

    private static void pressButton(ButtonType type) {
        type.getButton().onPress();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Objects.requireNonNull(client);
        // Objects.requireNonNull(Objects.requireNonNull(client).player).sendMessage(Text.literal(Integer.toString(keyCode)));

        super.keyPressed(keyCode, scanCode, modifiers);

        if (keyCode >= '0' && keyCode <= '9') {   // Numbers
            String number = Integer.toString(keyCode - 48);
            ButtonWidget.builder(Text.of(number),
                    button -> textField.write(number)).build().onPress();
        }
        switch (keyCode) {
            case '=' -> {   // + and =
                if (Screen.hasShiftDown()) {
                    pressButton(ButtonType.ADD);
                }
                else {
                    pressButton(ButtonType.EQUALS);
                }
            }
            case '-' -> pressButton(ButtonType.SUBTRACT);   // -
            case '8' -> {    // ×
                if (Screen.hasShiftDown()) {
                    pressButton(ButtonType.MULTIPLY);
                }
            }
            case '/' -> pressButton(ButtonType.DIVIDE);   // ÷
            case 'C' -> {
                if (Screen.hasControlDown()) {
                    client.keyboard.setClipboard(textField.getText());  // Copy
                }
                else {
                    pressButton(ButtonType.ALL_CLEAR);  // AC
                }
            }
            case 'P' -> {
                if (Screen.hasControlDown()) {
                    textField.setText(client.keyboard.getClipboard());  // Paste
                }
            }
            case 'X' -> {
                if (Screen.hasControlDown()) {
                    client.keyboard.setClipboard(textField.getText());  // Cut
                    textField.setText("");
                }
            }
            case '.' -> pressButton(ButtonType.POINT);   // .
            case 261 -> pressButton(ButtonType.ALL_CLEAR);   // AC
            case 259 -> pressButton(ButtonType.BACKSPACE);  // <-
        }
        return true;
    }

    private static void setNumber() {
        String s = textField.getText();
        if (inputMode == InputMode.FIRST) {
            if (s.isEmpty()) {
                number1 = new BigDecimal(0);
            }
            else {
                number1 = new BigDecimal(s);
            }
        }
        else if (inputMode == InputMode.SECOND) {
            if (s.isEmpty()) {
                number2 = new BigDecimal(0);
            }
            else {
                number2 = new BigDecimal(s);
            }
        }
    }

    private static void syncNumberText() {
        String number;
        if (number1 != null) {
            number = number1.toString();
        }
        else if (number2 != null) {
            number = number2.toString();
        }
        else {
            number = "";
        }
        textField.setText(number);
    }

    private static void setNumberAndClear() {
        setNumber();
        textField.setText("");
    }

    private static void normalOperate() {
        ++operateCount;
        try {
            if (number1 != null && number2 != null && operateCount > 1) {
                number1 = operator.method.get();
            }
        }
        catch (NumberFormatException | ArithmeticException e) {
            textField.setText("Wrong!");
        }
    }

    private static void operate() {
        operateCount = 0;
        try {
            setNumberAndClear();
            if (number1 != null && number2 != null) {
                textField.setText(operator.method.get().toString());
                number1 = new BigDecimal(textField.getText());
                inputMode = InputMode.FIRST;
            }
        }
        catch (NumberFormatException | ArithmeticException e) {
            textField.setText("Wrong!");
        }
    }

    private static void allClear() {
        number1 = number2 = null;
        syncNumberText();
    }

    enum ButtonType {
        NUMBER,
        BACKSPACE("←", button -> textField.eraseCharacters(-1)),
        ADD("+", Operator.ADD),
        SUBTRACT("-", Operator.SUBTRACT),
        MULTIPLY("*", Operator.MULTIPLY),
        DIVIDE("/", Operator.DIVIDE),
        MOD("%", Operator.MOD),
        ALL_CLEAR("AC", button -> allClear()),
        EQUALS("=", button -> operate()),
        POINT(".", button -> textField.write(".")),
        NEGATE("+/-", Operator.NEGATE),
        SQUARE_ROOT("√", Operator.SQUARE_ROOT),
        CUBE_ROOT("³√", Operator.CUBE_ROOT),
        POWER("xʸ", Operator.POWER),
        POWER2("x²", Operator.POWER, new BigDecimal(2)),
        POWER3("x³", Operator.POWER, new BigDecimal(3)),
        ABS("|x|", Operator.ABS),
        REVERSE("1/x", Operator.REVERSE),
        PI("π", new BigDecimal("3.1415926535897")),
        E("e", new BigDecimal("2.7182818284590")),
        LOG10("log₁₀", Operator.LOG10),
        LOG("log", Operator.LOG),
        SIN("sin", Operator.SIN),
        COS("cos", Operator.COS),
        TAN("tan", Operator.TAN),
        EXP("exp", button -> textField.write("e"));

        private String string;
        private ButtonWidget button;
        private ButtonWidget.PressAction action;

        ButtonType() {
        }

        ButtonType(String string, ButtonWidget.PressAction action) {
            this.string = string;
            this.action = action;
            this.button = getInnerButton();
        }

        ButtonType(String string, Operator operator) {
            this.string = string;
            if (operator.isTwoArgs) {
                this.action = button -> {
                    setNumberAndClear();
                    normalOperate();
                    CalculatorScreen.operator = operator;
                    inputMode = InputMode.SECOND;
                    button.setFocused(false);
                };
            }
            else {
                this.action = button -> {
                    setNumber();
                    number1 = operator.method.get();
                    syncNumberText();
                    button.setFocused(false);
                };
            }
            this.button = getInnerButton();
        }

        ButtonType(String string, Operator operator, BigDecimal arg2) {
            this.string = string;
            if (operator.isTwoArgs) {
                this.action = button -> {
                    setNumber();
                    number2 = arg2;
                    number1 = operator.method.get();
                    number2 = null;
                    syncNumberText();
                    button.setFocused(false);
                };
            }
            this.button = getInnerButton();
        }

        ButtonType(String string, BigDecimal value) {
            this.string = string;
            this.action = button -> textField.setText(value.toString());
            this.button = getInnerButton();
        }

        public ButtonWidget getInnerButton() {
            return ButtonWidget.builder(Text.of(string), action).width(BUTTON_WIDTH).build();
        }

        public ButtonWidget getButton() {
            return button;
        }
    }

    enum Operator {
        ADD(() -> number1.add(number2), true),
        SUBTRACT(() -> number1.subtract(number2), true),
        MULTIPLY(() -> number1.multiply(number2), true),
        DIVIDE(() -> number1.divide(number2, 7, RoundingMode.HALF_EVEN), true),
        POWER(() -> number1.pow(number2.intValue()), true),
        MOD(() -> number1.remainder(number2), true),
        NEGATE(() -> number1.negate()),
        SQUARE_ROOT(() -> number1.sqrt(MathContext.DECIMAL64)),
        CUBE_ROOT(() -> getValueFromDouble(Math.cbrt(number1.doubleValue()))),
        ABS(() -> number1.abs()),
        REVERSE(() -> new BigDecimal(1).divide(number1, 7, RoundingMode.HALF_EVEN)),
        LOG10(() -> getValueFromDouble(Math.log10(number1.doubleValue()))),
        LOG(() -> getValueFromDouble(Math.log(number1.doubleValue()))),
        SIN(() -> getValueFromDouble(Math.sin(number1.doubleValue()))),
        COS(() -> getValueFromDouble(Math.cos(number1.doubleValue()))),
        TAN(() -> getValueFromDouble(Math.tan(number1.doubleValue())));

        public final OperateAction<BigDecimal> method;
        public final boolean isTwoArgs;
        static BigDecimal getValueFromDouble(double result) {
            return BigDecimal.valueOf(result);
        }

        Operator(OperateAction<BigDecimal> method) {
            this.method = method;
            this.isTwoArgs = false;
        }

        Operator(OperateAction<BigDecimal> method, boolean isTwoArgs) {
            this.method = method;
            this.isTwoArgs = isTwoArgs;
        }
    }

    enum InputMode {
        FIRST,
        SECOND
    }

    interface OperateAction<T> {
        T get() throws NumberFormatException, ArithmeticException;
    }
}
