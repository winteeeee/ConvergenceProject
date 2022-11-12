package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.OrderStatus;

import java.time.LocalDateTime;


@Getter
@Setter
public class OrderDTO extends DTO {
    private Long id;
    private OrderStatus status;
    private LocalDateTime regdate;
    private String option;
    private Integer price;
    private String comment;
    private Long menu_id;           // FK
    private Long total_order_id;    // FK
    private Long store_id;          // FK

    public OrderDTO() {}

    public OrderDTO(Long id, Integer status, LocalDateTime regdate, String option, Integer price, String comment, Long menu_id, Long total_order_id, Long store_id) {
        this.id = id;
        this.status = OrderStatus.of(status);
        this.regdate = regdate;
        this.option = option;
        this.price = price;
        this.comment = comment;
        this.menu_id = menu_id;
        this.total_order_id = total_order_id;
        this.store_id = store_id;
    }

    public OrderDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }

    public void setStatus(Integer status) {
        this.status = OrderStatus.of(status);
    }

    public Integer getStatus() {
        return status.getCode();
    }
}
