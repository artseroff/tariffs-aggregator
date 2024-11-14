package ru.rsreu.megafon.service.sender.dto;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.rsreu.megafon.dto.Tariff;

public record TariffsData(@NotNull String company, @NotNull List<Tariff> tariffs) {
}
