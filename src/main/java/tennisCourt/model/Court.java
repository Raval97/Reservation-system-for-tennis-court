package tennisCourt.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name="court")
public class Court {

    @Id
    private Long id;
    private String name;
    private String type;
    private Boolean status;
    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL)
    private Set<Services> services;

    public Court(Long id, String name, String type, Boolean status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public Court() {
    }
}
