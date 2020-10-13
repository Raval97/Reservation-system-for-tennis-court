package tennisCourt.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name="payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfPaying;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate finalPaymentDate;
    private Float price;
    private String statusPaying;
    @ManyToOne
    @JoinColumn
    private User user;

    public Payment() {
    }

    public Payment(String title, LocalDate dateOfPaying, LocalDate finalPaymentDate,
                   Float price, String statusPaying, User user) {
        this.title = title;
        this.dateOfPaying = dateOfPaying;
        this.finalPaymentDate = finalPaymentDate;
        this.price = price;
        this.statusPaying = statusPaying;
        this.user = user;
    }

    public Payment(String title, LocalDate dateOfPaying, Float price, String statusPaying, User user) {
        this.title = title;
        this.dateOfPaying = dateOfPaying;
        this.price = price;
        this.statusPaying = statusPaying;
        this.user = user;
    }
}
