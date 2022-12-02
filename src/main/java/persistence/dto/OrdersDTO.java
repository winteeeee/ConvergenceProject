package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrdersDTO extends DTO {
    private Long menu_id;           // FK
    private Long total_orders_id;   // FK
    private String details;
    private Integer price;


    public OrdersDTO() {}

    public OrdersDTO(String details, Integer price, Long menu_id, Long total_orders_id) {
        this.details = details;
        this.price = price;
        this.menu_id = menu_id;
        this.total_orders_id = total_orders_id;
    }

    public OrdersDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
