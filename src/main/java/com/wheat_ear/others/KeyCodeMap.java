package com.wheat_ear.others;

import java.util.HashMap;

public final class KeyCodeMap {
    private KeyCodeMap() {
    }

    public static HashMap<Character, Integer> CODE_MAP = new HashMap<>();

    static {
        for (int n = 65; n <= 90; ++n) {
            CODE_MAP.put((char) n, n);
        }
    }
}
