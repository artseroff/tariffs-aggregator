package ru.rsreu.serov.tariffs.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tariffs")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    private int countFreeMessages;

    @Column(nullable = false)
    @Min(message = "Число > 1", value = 1)
    @Max(message = "Число минут должно быть от 1 до 200", value = 300)
    private int countFreeMinutes;

    @Min(message = "Число > 1", value = 1)
    @Max(message = "Число минут должно быть от 1 до 10", value = 5)
    private float costPerMinute;

    private float costPerMessage;

    @Column(name = "count_gb_internet_traffic")
    private float countGBInternetTraffic;

    private boolean isUnlimitedTraffic;

    private float amount;

    private String info;

    @OneToOne()
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    public Tariff(Long id,
                  String name,
                  int countFreeMessages,
                  int countFreeMinutes,
                  float costPerMinute,
                  float costPerMessage,
                  float countGBInternetTraffic,
                  boolean isUnlimitedTraffic,
                  float amount,
                  String info,
                  Company company) {
        this.id = id;
        this.name = name;
        this.countFreeMessages = countFreeMessages;
        this.countFreeMinutes = countFreeMinutes;
        this.costPerMinute = costPerMinute;
        this.costPerMessage = costPerMessage;
        this.countGBInternetTraffic = countGBInternetTraffic;
        this.isUnlimitedTraffic = isUnlimitedTraffic;
        this.amount = amount;
        this.info = info;
        this.company = company;
    }


    public Tariff(String name,
                  int countFreeMessages,
                  int countFreeMinutes,
                  float costPerMinute,
                  float costPerMessage,
                  float countGBInternetTraffic,
                  boolean isUnlimitedTraffic,
                  float amount,
                  String info,
                  Company company) {

        this.name = name;
        this.countFreeMessages = countFreeMessages;
        this.countFreeMinutes = countFreeMinutes;
        this.costPerMinute = costPerMinute;
        this.costPerMessage = costPerMessage;
        this.countGBInternetTraffic = countGBInternetTraffic;
        this.isUnlimitedTraffic = isUnlimitedTraffic;
        this.amount = amount;
        this.info = info;
        this.company = company;
    }

    public Tariff() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountFreeMessages() {
        return countFreeMessages;
    }

    public void setCountFreeMessages(int countFreeMessages) {
        this.countFreeMessages = countFreeMessages;
    }

    public int getCountFreeMinutes() {
        return countFreeMinutes;
    }

    public void setCountFreeMinutes(int countFreeMinutes) {
        this.countFreeMinutes = countFreeMinutes;
    }

    public float getCostPerMinute() {
        return costPerMinute;
    }

    public void setCostPerMinute(float costPerMinute) {
        this.costPerMinute = costPerMinute;
    }

    public float getCostPerMessage() {
        return costPerMessage;
    }

    public void setCostPerMessage(float costPerMessage) {
        this.costPerMessage = costPerMessage;
    }

    public float getCountGBInternetTraffic() {
        return countGBInternetTraffic;
    }

    public void setCountGBInternetTraffic(float countGBInternetTraffic) {
        this.countGBInternetTraffic = countGBInternetTraffic;
    }

    public boolean isUnlimitedTraffic() {
        return isUnlimitedTraffic;
    }

    public void setUnlimitedTraffic(boolean unlimitedTraffic) {
        isUnlimitedTraffic = unlimitedTraffic;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}