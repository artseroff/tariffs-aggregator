package ru.rsreu.manager.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class TariffFilter {

    private boolean enabledCountFreeMessages;
    private int minCountFreeMessages;
    private int maxCountFreeMessages;

    private boolean enabledCountFreeMinutes;
    private int minCountFreeMinutes;
    private int maxCountFreeMinutes;
    private boolean enabledInternetTraffic;

    private boolean enabledCountGBInternetTraffic;
    private int minCountGBInternetTraffic;
    private int maxCountGBInternetTraffic;

    private boolean enabledAmount;
    private int minAmount;
    private int maxAmount;

    private boolean current;

    public TariffFilter() {
    }

    private static boolean isValueInRange(int value, int min, int max) {
        return (value >= min && value <= max);
    }

    private static boolean isValueInRange(float value, int min, int max) {
        return (value >= min && value <= max);
    }

    public boolean validRanges() {
        boolean predicate = true;
        if (enabledCountFreeMessages) {
            predicate = predicate && (maxCountFreeMessages > minCountFreeMessages);
        }
        if (enabledCountFreeMinutes) {
            predicate = predicate && (maxCountFreeMinutes > minCountFreeMinutes);
        }
        if (enabledCountGBInternetTraffic) {
            predicate = predicate && (maxCountGBInternetTraffic > minCountGBInternetTraffic);
        }
        if (enabledAmount) {
            predicate = predicate && (maxAmount > minAmount);
        }
        return predicate;
    }

    public boolean applyFilter(Tariff tariff) {
        boolean predicate = true;
        if (enabledCountFreeMessages) {
            predicate = predicate
                && (isValueInRange(tariff.getCountFreeMessages(),
                this.minCountFreeMessages, this.maxCountFreeMessages
            ));
        }
        if (enabledCountFreeMinutes) {
            predicate = predicate
                && (isValueInRange(tariff.getCountFreeMinutes(),
                this.minCountFreeMinutes, this.maxCountFreeMinutes
            ));
        }
        if (enabledCountGBInternetTraffic) {
            predicate = predicate && (isValueInRange(
                tariff.getCountGBInternetTraffic(),
                this.minCountGBInternetTraffic,
                this.maxCountGBInternetTraffic
            ));
        }
        if (enabledAmount) {
            predicate = predicate && (isValueInRange(tariff.getAmount(), this.minAmount, this.maxAmount));
        }
        return predicate;
    }
}
