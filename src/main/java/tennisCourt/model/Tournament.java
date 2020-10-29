package tennisCourt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int maxCountOFParticipant;
    private int countOFRegisteredParticipant;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfStarted;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfEnded;
    private float entryFee;
    @JsonBackReference
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<UserTournament> userTournaments = null;
    @JsonBackReference
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<UserTournamentApplication> userTournamentApplications = null;

    public Tournament() {
    }

    public Tournament(String title, int maxCountOFParticipant, int countOFRegisteredParticipant,
                      LocalDate dateOfStarted, LocalDate dateOfEnded, float entryFee) {
        this.title = title;
        this.maxCountOFParticipant = maxCountOFParticipant;
        this.countOFRegisteredParticipant = countOFRegisteredParticipant;
        this.dateOfStarted = dateOfStarted;
        this.dateOfEnded = dateOfEnded;
        this.entryFee = entryFee;
    }

}
