package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.enums.OrdersStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class TotalOrdersDTO extends DTO {
    private Long id;
    private OrdersStatus status;
    private LocalDateTime regdate;
    private Integer price;
    private Long store_id;          // FK
    private Long user_pk;           // FK

    public TotalOrdersDTO() {  }

    public TotalOrdersDTO(Long id, OrdersStatus status, LocalDateTime regdate, Integer price, Long store_id, Long user_pk) {
        this.id = id;
        this.status = status;
        this.regdate = regdate;
        this.price = price;
        this.store_id = store_id;
        this.user_pk = user_pk;
    }

    public void setStatus(String status) {
        this.status = OrdersStatus.of(status);
    }
    public void setStatus(OrdersStatus status) {
        this.status = status;
    }
    public String getStatus() {
        return status.getCode();
    }

    public OrdersStatus getStatusEnum() {
        return status;
    }
}
