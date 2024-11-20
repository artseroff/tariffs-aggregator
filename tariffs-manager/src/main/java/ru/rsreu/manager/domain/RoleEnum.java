package ru.rsreu.manager.domain;

import java.util.Arrays;

public enum RoleEnum {
    ADMINISTRATOR("Администратор", "admin"),
    EDITOR("Редактор каталога", "editor");

    private final String name;

    private final String mainPage;

    RoleEnum(String name, String mainPage) {
        this.name = name;
        this.mainPage = mainPage;
    }

    public String getName() {
        return name;
    }

    public String getMainPage() {
        return mainPage;
    }

    public static RoleEnum findRoleByName(String parameterName) throws IllegalArgumentException {
        return Arrays.stream(RoleEnum.values())
            .filter(v -> v.getName().equals(parameterName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown role"));
    }
}
