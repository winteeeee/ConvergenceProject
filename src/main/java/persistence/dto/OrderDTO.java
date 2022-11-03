package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Status;

@Getter
@Setter
public class OrderDTO {
    private long id;
    private String option;
    private int price;
    private Status status;
    private String comment;
    private long total_order_id;    // FK
    private long store_id;          // FK

    public OrderDTO(long id, String option, int price, Status status, String comment, long total_order_id, long store_id) {
        this.id = id;
        this.option = option;
        this.price = price;
        this.status = status;
        this.comment = comment;
        this.total_order_id = total_order_id;
        this.store_id = store_id;
    }
}
