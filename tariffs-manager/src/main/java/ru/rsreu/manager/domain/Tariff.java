package ru.rsreu.manager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tariffs")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "{valid.tariff.name.empty}")
    private String name;

    @Min(message = "{valid.min.positive}", value = 0)
    private int countFreeMessages;

    @Min(message = "{valid.min.positive}", value = 0)
    private int countFreeMinutes;

    @Min(message = "{valid.min.positive}", value = 0)
    @Column(name = "count_gb_internet_traffic")
    private float countGBInternetTraffic;

    @Min(message = "{valid.min.positive}", value = 0)
    private float costPerMinute;

    @Min(message = "{valid.min.positive}", value = 0)
    private float costPerMessage;

    private boolean unlimitedTraffic;

    @Min(message = "{valid.min.positive}", value = 0)
    private float amount;

    private String info;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Tariff() {

    }
}
