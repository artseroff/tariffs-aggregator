package ru.rsreu.serov.tariffs.entity;

public class TariffFilter {


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

    /*private boolean unlimitedTraffic;
    private boolean noAmount;*/

    private boolean enabledAmount;
    private int minAmount;
    private int maxAmount;

    private boolean current;
    /*private Company company;*/


    public TariffFilter() {
    }


    private boolean isValueInRange(int value, int min, int max) {
        return (value >= min && value <= max);
    }

    private boolean isValueInRange(float value, int min, int max) {
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
        // интернет
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
            predicate = predicate && (isValueInRange(tariff.getCountFreeMessages(), this.minCountFreeMessages, this.maxCountFreeMessages));
        }
        if (enabledCountFreeMinutes) {
            predicate = predicate && (isValueInRange(tariff.getCountFreeMinutes(), this.minCountFreeMinutes, this.maxCountFreeMinutes));
        }
        // интернет
        if (enabledCountGBInternetTraffic) {
            predicate = predicate && (isValueInRange(tariff.getCountGBInternetTraffic(), this.minCountGBInternetTraffic, this.maxCountGBInternetTraffic));
        }
        if (enabledAmount) {
            predicate = predicate && (isValueInRange(tariff.getAmount(), this.minAmount, this.maxAmount));
        }
        return predicate;
    }

    public boolean isEnabledCountFreeMessages() {
        return enabledCountFreeMessages;
    }

    public void setEnabledCountFreeMessages(boolean enabledCountFreeMessages) {
        this.enabledCountFreeMessages = enabledCountFreeMessages;
    }

    public int getMinCountFreeMessages() {
        return minCountFreeMessages;
    }

    public void setMinCountFreeMessages(int minCountFreeMessages) {
        this.minCountFreeMessages = minCountFreeMessages;
    }

    public int getMaxCountFreeMessages() {
        return maxCountFreeMessages;
    }

    public void setMaxCountFreeMessages(int maxCountFreeMessages) {
        this.maxCountFreeMessages = maxCountFreeMessages;
    }

    public boolean isEnabledCountFreeMinutes() {
        return enabledCountFreeMinutes;
    }

    public void setEnabledCountFreeMinutes(boolean enabledCountFreeMinutes) {
        this.enabledCountFreeMinutes = enabledCountFreeMinutes;
    }

    public int getMinCountFreeMinutes() {
        return minCountFreeMinutes;
    }

    public void setMinCountFreeMinutes(int minCountFreeMinutes) {
        this.minCountFreeMinutes = minCountFreeMinutes;
    }

    public int getMaxCountFreeMinutes() {
        return maxCountFreeMinutes;
    }

    public void setMaxCountFreeMinutes(int maxCountFreeMinutes) {
        this.maxCountFreeMinutes = maxCountFreeMinutes;
    }

    public boolean isEnabledInternetTraffic() {
        return enabledInternetTraffic;
    }

    public void setEnabledInternetTraffic(boolean enabledInternetTraffic) {
        this.enabledInternetTraffic = enabledInternetTraffic;
    }

    public boolean isEnabledCountGBInternetTraffic() {
        return enabledCountGBInternetTraffic;
    }

    public void setEnabledCountGBInternetTraffic(boolean enabledCountGBInternetTraffic) {
        this.enabledCountGBInternetTraffic = enabledCountGBInternetTraffic;
    }

    public int getMinCountGBInternetTraffic() {
        return minCountGBInternetTraffic;
    }

    public void setMinCountGBInternetTraffic(int minCountGBInternetTraffic) {
        this.minCountGBInternetTraffic = minCountGBInternetTraffic;
    }

    public int getMaxCountGBInternetTraffic() {
        return maxCountGBInternetTraffic;
    }

    public void setMaxCountGBInternetTraffic(int maxCountGBInternetTraffic) {
        this.maxCountGBInternetTraffic = maxCountGBInternetTraffic;
    }

    public boolean isEnabledAmount() {
        return enabledAmount;
    }

    public void setEnabledAmount(boolean enabledAmount) {
        this.enabledAmount = enabledAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
