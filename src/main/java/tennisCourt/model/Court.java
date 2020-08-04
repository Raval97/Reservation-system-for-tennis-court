package tennisCourt.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="court")
public class Court {

    @Id
    private Long id;
    private String name;
    private String type;
    private Boolean status;
    private String imagesAddress;
    @OneToOne(mappedBy = "court", cascade = CascadeType.ALL)
    private Services services;
}
