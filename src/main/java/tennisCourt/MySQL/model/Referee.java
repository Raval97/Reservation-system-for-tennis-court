package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.dhiraj072.randomwordgenerator.exceptions.DataMuseException;
import com.github.javafaker.Faker;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name="Referee")
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @OneToOne(cascade =  CascadeType.REMOVE)
    private Matches match;
    String name;
    String surname;
    String nationality;
    int age;
    int numberOfMatch;

    public Referee() throws DataMuseException {
        Faker faker = new Faker();
        this.name = faker.name().firstName();
        this.surname = faker.name().lastName();
        this.nationality = "England";
        this.age = ThreadLocalRandom.current().nextInt(26, 65);
        this.numberOfMatch = ThreadLocalRandom.current().nextInt(5, 100);
    }

    public Referee(String name, String surname, String nationality, int age, int numberOfMatch) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nationality = nationality;
        this.age = age;
        this.numberOfMatch = numberOfMatch;
    }


}
