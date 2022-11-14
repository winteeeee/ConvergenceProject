package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TotalOrdersDTO extends DTO {
    private Long id;
    private LocalDateTime regdate;
    private String address;
    private Integer total_price;
    private Long user_pk;     // FK

    public TotalOrdersDTO() {  }

    public TotalOrdersDTO(Long id, LocalDateTime regdate, String address, Integer total_price, Long user_pk) {
        this.id = id;
        this.regdate = regdate;
        this.address = address;
        this.total_price = total_price;
        this.user_pk = user_pk;
    }

    public TotalOrdersDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
