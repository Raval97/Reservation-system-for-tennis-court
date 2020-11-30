package tennisCourt.MySQL.model;

import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;
import com.github.dhiraj072.randomwordgenerator.datamuse.DataMuseRequest;
import com.github.dhiraj072.randomwordgenerator.exceptions.DataMuseException;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name="PersonalData")
public class PersonalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private PlayerTeam playerTeam;
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private PlayersMatch playersMatch;
    String surname;
    String name;
    int age;
    String nationality;
    int weight;
    int height;
    String position;
    String formation;

    public PersonalData() {
    }

    public PersonalData(PlayerTeam playerTeam) throws DataMuseException {
        List<String> position = new ArrayList<>(Arrays.asList("ln", "pn", "pp", "lp", "spo", "sp", "spd", "lo", "so", "po", "br"));
        this.surname = RandomWordGenerator.getRandomWord(new DataMuseRequest().topics("Transliteration of personal names"));
        this.name = RandomWordGenerator.getRandomWord(new DataMuseRequest().topics("Transliteration of personal names"));
        this.age= ThreadLocalRandom.current().nextInt(16, 40 + 1);
        this.nationality  = RandomWordGenerator.getRandomWord(new DataMuseRequest().topics("Transliteration of personal names"));
        this.weight = ThreadLocalRandom.current().nextInt(60, 95 + 1);
        this.height  = this.weight + ThreadLocalRandom.current().nextInt(90, 110 + 1);
        this.position = position.get(ThreadLocalRandom.current().nextInt(0, 11));
        this.formation = "a";
        this.playerTeam = playerTeam;
        this.playerTeam.setPlayer(this);
    }

    public PersonalData(String surname, String name, int age, String nationality, int weight,
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
