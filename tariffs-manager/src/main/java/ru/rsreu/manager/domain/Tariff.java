package ru.rsreu.manager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import ru.rsreu.manager.config.UriAttributeConverter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "tariffs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "company_id", "addedNotByUser"})
})
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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

    @ColumnDefault("false")
    private boolean unlimitedTraffic;

    @Min(message = "{valid.min.positive}", value = 0)
    private float amount;

    private String info;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Convert(converter = UriAttributeConverter.class)
    private URI url;

    @ColumnDefault("false")
    private boolean addedNotByUser;

    public Tariff() {

    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return idOptionalEquals((Tariff) o, false);
    }

    public boolean idOptionalEquals(Tariff tariff, boolean withoutId) {

        return countFreeMessages == tariff.countFreeMessages && countFreeMinutes == tariff.countFreeMinutes
            && Float.compare(countGBInternetTraffic, tariff.countGBInternetTraffic) == 0
            && Float.compare(costPerMinute, tariff.costPerMinute) == 0
            && Float.compare(costPerMessage, tariff.costPerMessage) == 0
            && unlimitedTraffic == tariff.unlimitedTraffic
            && Float.compare(amount, tariff.amount) == 0 && addedNotByUser == tariff.addedNotByUser
            && (Objects.equals(id, tariff.id) || withoutId) && Objects.equals(name, tariff.name)
            && Objects.equals(info, tariff.info)
            && Objects.equals(company, tariff.company)
            && Objects.equals(url, tariff.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            countFreeMessages,
            countFreeMinutes,
            countGBInternetTraffic,
            costPerMinute,
            costPerMessage,
            unlimitedTraffic,
            amount,
            info,
            company,
            url,
            addedNotByUser
        );
    }
}
