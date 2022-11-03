package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalOrderDTO {
    private long id;
    private String address;
    private int total_price;
    private String user_id;     // FK

    public TotalOrderDTO(long id, String address, int total_price, String user_id) {
        this.id = id;
        this.address = address;
        this.total_price = total_price;
        this.user_id = user_id;
    }
}
