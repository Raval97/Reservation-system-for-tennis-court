package tennisCourt.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name="membership_application")
public class MembershipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private User user;
    private LocalDate dateOfApplication;
    private String decision;

    public MembershipApplication() {
    }

    public MembershipApplication(User user, LocalDate dateOfApplication, String decision) {
        this.user = user;
        this.dateOfApplication = dateOfApplication;
        this.decision = decision;
    }

}
