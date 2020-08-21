package tennisCourt.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="price_list")
public class PriceList {
    @Id
    private Long id;
    private String type;
    private String time;
    private Float price;

    public PriceList() {
    }

    public PriceList(Long id, String type, String time, Float price) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.price = price;
    }
}
