package tennisCourt.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name="club_association")
public class ClubAssociation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade =  CascadeType.REMOVE)
    private Users users;
    private LocalDate dateOfJoining;
    private Boolean ifActive;
    private LocalDate dateOfEndActive;

    public ClubAssociation() {
    }

    public ClubAssociation(LocalDate dateOfJoining, Boolean ifActive, Users user) {
        this.dateOfJoining = dateOfJoining;
        this.ifActive = ifActive;
        this.users= user;
    }

}
