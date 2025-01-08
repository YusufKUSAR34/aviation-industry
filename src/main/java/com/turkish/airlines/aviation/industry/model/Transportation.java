package com.turkish.airlines.aviation.industry.model;

import com.turkish.airlines.aviation.industry.enums.TransportationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "transportations")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted = false")
public class Transportation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id", nullable = false)
    @ToString.Exclude
    private Location originLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    @ToString.Exclude
    private Location destinationLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportationType transportationType;

    private Double duration;
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transportation that = (Transportation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}