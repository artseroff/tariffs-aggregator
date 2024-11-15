package ru.rsreu.manager.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record TariffDto(
    @NotNull String name,
    int countFreeMessages,
    int countFreeMinutes,
    float countGBInternetTraffic,
    float costPerMinute,
    float costPerMessage,
    boolean unlimitedTraffic,
    float amount,
    @NotNull URI url) {
}
