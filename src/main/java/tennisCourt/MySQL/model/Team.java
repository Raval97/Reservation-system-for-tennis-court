package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<PlayerTeam> players;
    @JsonBackReference
    @OneToMany(mappedBy = "team1", cascade = CascadeType.ALL)
    private Set<Matches> match1;
    @JsonBackReference
    @OneToMany(mappedBy = "team2", cascade = CascadeType.ALL)
    private Set<Matches> match2;
    String name;
    String country;
    String nationalLevel;
    String coach;


    public Team() throws DataMuseException {
        Faker faker = new Faker();
        this.name = faker.pokemon().name() +" "+ faker.pokemon().name();
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

}
