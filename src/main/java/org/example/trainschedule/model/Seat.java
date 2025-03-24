package org.example.trainschedule.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "seats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double price;
}