package tennisCourt.MySQL.model;

import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;
import com.github.dhiraj072.randomwordgenerator.datamuse.DataMuseRequest;
import com.github.dhiraj072.randomwordgenerator.exceptions.DataMuseException;
import com.github.javafaker.Faker;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name="team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<PlayerTeam> players;
    @OneToOne(mappedBy = "team1", cascade = CascadeType.ALL)
    private Matches match1;
    @OneToOne(mappedBy = "team2", cascade = CascadeType.ALL)
    private Matches match2;
    String name;
    String country;
    String nationalLevel;
    String coach;


    public Team() throws DataMuseException {
        Faker faker = new Faker();
        this.name = faker.pokemon().name() +" "+ faker.team().sport();
        this.country = "Poland";
        this.coach  = faker.name().lastName();
        this.nationalLevel = "Ekstraklasa";
    }

    public Team(String name, String country, String nationalLevel, String coach) {
        this.name = name;
        this.country = country;
        this.nationalLevel = nationalLevel;
        this.coach = coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) &&
                Objects.equals(players, team.players) &&
                Objects.equals(match1, team.match1) &&
                Objects.equals(match2, team.match2) &&
                Objects.equals(name, team.name) &&
                Objects.equals(country, team.country) &&
                Objects.equals(nationalLevel, team.nationalLevel) &&
                Objects.equals(coach, team.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, players, match1, match2, name, country, nationalLevel, coach);
    }
}
