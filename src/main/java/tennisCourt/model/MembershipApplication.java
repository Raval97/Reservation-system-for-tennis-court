package tennisCourt.model;

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
    private Users users;
    private LocalDate dateOfApplication;
    private String decision;

    public MembershipApplication() {
    }

    public MembershipApplication(Users user, LocalDate dateOfApplication, String decision) {
        this.users = user;
        this.dateOfApplication = dateOfApplication;
        this.decision = decision;
    }

}
