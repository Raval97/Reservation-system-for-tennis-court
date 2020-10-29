package tennisCourt.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private User user;
    private LocalDate dateOfJoining;
    private Boolean ifActive;
    private LocalDate dateOfEndActive;

    public ClubAssociation() {
    }

    public ClubAssociation(LocalDate dateOfJoining, Boolean ifActive, User user) {
        this.dateOfJoining = dateOfJoining;
        this.ifActive = ifActive;
        this.user= user;
    }

}
