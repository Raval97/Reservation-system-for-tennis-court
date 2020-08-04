package tennisCourt.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="client")
public class Client {

    @Id
    private Long id;
    private String name;
    private String surname;
    private String emailAddress;
    private int phoneNumber;
    @OneToOne
    @MapsId
    private User user;

    public Client(Long id, String name, String surname, String emailAddress, int phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }
}
