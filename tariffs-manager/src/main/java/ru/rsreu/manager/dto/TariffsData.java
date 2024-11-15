package ru.rsreu.manager.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record TariffsData(@NotNull String company, @NotNull List<TariffDto> tariffs) {
}
