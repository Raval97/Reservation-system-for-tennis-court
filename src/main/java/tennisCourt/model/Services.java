package tennisCourt.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name="services")
public class Services {

    @Id
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private float hours;
    private float time;
    private float count;
    private Boolean ifRocket;
    private Boolean ifBalls;
    private Boolean ifShoes;
    private float price;
    @OneToOne
    @MapsId
    private Court court;
    @OneToOne(mappedBy = "services", cascade = CascadeType.ALL)
    private ReservationServices reservationService;

    public Services(Long id, LocalDate date, float hours, float time, float count, Boolean ifRocket,
                    Boolean ifBalls, Boolean ifShoes, float price) {
        this.id = id;
        this.date = date;
        this.hours = hours;
        this.time = time;
        this.count = count;
        this.ifRocket = ifRocket;
        this.ifBalls = ifBalls;
        this.ifShoes = ifShoes;
        this.price = price;
    }
}
