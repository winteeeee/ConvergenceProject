package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TotalOrderDTO extends DTO {
    private long id;
    private LocalDateTime regdate;
    private String address;
    private int total_price;
    private String user_id;     // FK

    public TotalOrderDTO(long id, LocalDateTime regdate, String address, int total_price, String user_id) {
        this.id = id;
        this.regdate = regdate;
        this.address = address;
        this.total_price = total_price;
        this.user_id = user_id;
    }
}
