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
    private Users users;

    public Client() {
    }

    public Client(String name, String surname, String emailAddress, int phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }
}
