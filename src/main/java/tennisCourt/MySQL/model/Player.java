package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.dhiraj072.randomwordgenerator.exceptions.DataMuseException;
import com.github.javafaker.Faker;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name="Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private PlayerTeam playerTeam;
    @JsonBackReference
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<PlayerMatchPerformance> playersMatch;
    String surname;
    String name;
    int age;
    String nationality;
    int weight;
    int height;
    String position;
    String formation;

    public Player() {
    }

    public Player(PlayerTeam playerTeam) throws DataMuseException {
        Faker faker = new Faker();
        List<String> position = new ArrayList<>(Arrays.asList("ln", "pn", "pp", "lp", "spo", "sp", "spd", "lo", "so", "po", "br"));
        List<String> country = new ArrayList<>(Arrays.asList("Polska", "Niemcy", "Hiszpania", "Włochy", "Francja",
                "Agentyna", "Anglia", "Belgia", "Brazylia", "Urugwaj", "Japonia","Rosja", "Portugalia" ));
        this.surname = faker.name().lastName();
        this.name = faker.name().firstName();
        this.age= ThreadLocalRandom.current().nextInt(16, 41);
        this.nationality  = country.get(ThreadLocalRandom.current().nextInt(0, country.size()));
        this.weight = ThreadLocalRandom.current().nextInt(60, 96);
        this.height  = this.weight + ThreadLocalRandom.current().nextInt(90, 111);
        this.position = position.get(ThreadLocalRandom.current().nextInt(0, 11));
        this.formation = "a";
        this.playerTeam = playerTeam;
        this.playerTeam.setPlayer(this);
    }

    public Player(String surname, String name, int age, String nationality, int weight,
                  int height, String position, String formation, PlayerTeam playerTeam) {
        this.surname = surname;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.weight = weight;
        this.height = height;
        this.position = position;
        this.formation = formation;
        this.playerTeam = playerTeam;
        this.playerTeam.setPlayer(this);
    }
}
