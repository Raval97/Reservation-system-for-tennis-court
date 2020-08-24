package tennisCourt.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Entity
@Table(name="services")
public class Services {

    @Id
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private LocalTime time;
    private float numberOfHours;
    private float unitCost;
    private Boolean ifRocket;
    private Boolean ifBalls;
    private Boolean ifShoes;
    private float price;
    @ManyToOne
    private Court court;
    @OneToOne(mappedBy = "services", cascade = CascadeType.ALL)
    private ReservationServices reservationService;

    public Services() {
    }

    public Services(Long id, LocalDate date, float numberOfHours, LocalTime time, float unitCost, Boolean ifRocket,
                    Boolean ifBalls, Boolean ifShoes, float price) {
        this.id = id;
        this.date = date;
        this.numberOfHours = numberOfHours;
        this.time = time;
        this.unitCost = unitCost;
        this.ifRocket = ifRocket;
        this.ifBalls = ifBalls;
        this.ifShoes = ifShoes;
        this.price = price;
    }
}
