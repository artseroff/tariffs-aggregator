package ru.rsreu.megafon.dto;

import java.net.URI;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Tariff(
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
