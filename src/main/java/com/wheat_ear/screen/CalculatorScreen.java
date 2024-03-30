package com.wheat_ear.screen;

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

public class CalculatorScreen extends Screen {
    private static final Text TITLE = Text.translatable("menu.calculator");
    private static final int[] NUMBER_ORDER = {7, 8, 9, 4, 5, 6, 1, 2, 3, 0};
    private static final ButtonType[] BUTTON_ORDER = {
            ButtonType.POWER, ButtonType.POWER2, ButtonType.POWER3, ButtonType.BACKSPACE,
            ButtonType.SQUARE_ROOT, ButtonType.ABS, ButtonType.ALL_CLEAR, ButtonType.DIVIDE,
            ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.MULTIPLY,
            ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.SUBTRACT,
            ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.NUMBER, ButtonType.ADD,
            ButtonType.NEGATE, ButtonType.NUMBER, ButtonType.POINT, ButtonType.EQUALS
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
        GridWidget.Adder adder = gridWidget.createAdder(4);

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

    enum ButtonType {
        NUMBER,
        BACKSPACE("←", button -> textField.eraseCharacters(-1)),
        ADD("+", Operator.ADD),
        SUBTRACT("-", Operator.SUBTRACT),
        MULTIPLY("*", Operator.MULTIPLY),
        DIVIDE("/", Operator.DIVIDE),
        ALL_CLEAR("C", button -> {
            number1 = number2 = null;
            syncNumberText();
        }),
        EQUALS("=", button -> {
            setNumberAndClear();
            if (number2 != null) {
                textField.setText(operator.method.get().toString());
                number1 = new BigDecimal(textField.getText());
                inputMode = InputMode.FIRST;
            }
        }),
        POINT(".", button -> textField.write(".")),
        NEGATE("⁺/₋", Operator.NEGATE),
        SQUARE_ROOT("√", Operator.SQUARE_ROOT),
        POWER("xʸ", Operator.POWER),
        POWER2("x²", Operator.POWER, new BigDecimal(2)),
        POWER3("x³", Operator.POWER, new BigDecimal(3)),
        ABS("|x|", Operator.ABS),
        REVERSE;

        private String string;
        private ButtonWidget.PressAction action;

        ButtonType() {

        }

        ButtonType(String string, ButtonWidget.PressAction action) {
            this.string = string;
            this.action = action;
        }

        ButtonType(String string, Operator operator) {
            this.string = string;
            if (operator.isTwoArgs) {
                this.action = button -> {
                    setNumberAndClear();
                    CalculatorScreen.operator = operator;
                    inputMode = InputMode.SECOND;
                };
            }
            else {
                this.action = button -> {
                    setNumber();
                    number1 = operator.method.get();
                    syncNumberText();
                };
            }
        }

        ButtonType(String string, Operator operator, BigDecimal arg2) {
            this.string = string;
            if (!operator.isTwoArgs) {
                this.action = button -> {
                    setNumber();
                    number2 = arg2;
                    number1 = operator.method.get();
                    number2 = null;
                    syncNumberText();
                };
            }
        }

        public ButtonWidget getButton() {
            return ButtonWidget.builder(Text.of(string), action).width(30).build();
        }
    }

    enum Operator {
        ADD(() -> number1.add(number2), true),
        SUBTRACT(() -> number1.subtract(number2), true),
        MULTIPLY(() -> number1.multiply(number2), true),
        DIVIDE(() -> number1.divide(number2, 7, RoundingMode.HALF_EVEN), true),
        POWER(() -> number1.pow(number2.intValue()), true),
        NEGATE(() -> number1.negate()),
        SQUARE_ROOT(() -> number1.sqrt(MathContext.DECIMAL64)),
        ABS(() -> number1.abs());

        public final OperateAction<BigDecimal> method;
        public final boolean isTwoArgs;

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
        T get();
    }
}
