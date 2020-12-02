package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.dhiraj072.randomwordgenerator.exceptions.DataMuseException;
import com.github.javafaker.Faker;
import lombok.Data;

import javax.persistence.*;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "Accurate")
public class Accurate {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    private PlayersMatch playersMatch;
    float shotsAccurate;
    float passesAccurate;

    public Accurate() throws DataMuseException {
        this.shotsAccurate = 1;
        this.passesAccurate = 1;
    }

    public Accurate(float shotsAccurate, float passesAccurate) {
        this.shotsAccurate = shotsAccurate;
        this.passesAccurate = passesAccurate;
    }


}
