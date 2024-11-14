package ru.rsreu.megafon.service.sender;

import ru.rsreu.megafon.service.sender.dto.TariffsData;

public interface SenderService {
    void send(TariffsData tariffsData);
}
