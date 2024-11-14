package ru.rsreu.megafon.dto;

import java.net.URI;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

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
