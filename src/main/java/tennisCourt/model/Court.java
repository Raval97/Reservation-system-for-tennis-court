package tennisCourt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="court")
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private Boolean status;
    @JsonBackReference
    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL)
    private Set<Services> services;

    public Court(Long id, String name, String type, Boolean status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public Court() {
        this.services = new HashSet<Services>();
    }

    public void addServices(Services services){
        this.services.add(services);
    }
}
