package ru.rsreu.serov.tariffs.entity;

import javax.persistence.*;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String info;

    @Transient
    @OneToOne(mappedBy = "company")
    private Tariff tariff;

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public Company(Long id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }

    public Company(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public Company() {
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}