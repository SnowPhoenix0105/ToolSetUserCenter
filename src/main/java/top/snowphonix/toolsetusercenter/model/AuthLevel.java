package top.snowphonix.toolsetusercenter.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum AuthLevel {
    ADMIN(2, "admin"),
    USER(1, "user"),
    PASSERBY(0, "passerby");

    private final int num;
    private final String name;
    private static final Map<Integer, AuthLevel> intMap = Arrays.stream(AuthLevel.values())
            .collect(Collectors.toMap(AuthLevel::toNum, auth -> auth));

    AuthLevel(int num, String name) {
        this.num = num;
        this.name = name;
    }

    public int toNum() {
        return num;
    }

    public static AuthLevel ofNum(int num) {
        return intMap.get(num);
    }

    @Override
    public String toString() {
        return name;
    }
}
