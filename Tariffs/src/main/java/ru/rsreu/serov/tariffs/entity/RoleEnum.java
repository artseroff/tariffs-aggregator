package ru.rsreu.serov.tariffs.entity;

import java.util.Arrays;

public enum RoleEnum {
    ADMINISTRATOR("Администратор"),
    EDITOR("Редактор каталога");

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RoleEnum findRoleByName(String parameterName) throws IllegalArgumentException {
        return Arrays.stream(RoleEnum.values()).filter(v -> v.getName().equals(parameterName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown user"));
    }
}
