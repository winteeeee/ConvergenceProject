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
    private Long store_id;           // FK
    private Long user_pk;   // FK

    public TotalOrdersDTO(Long id, String status, LocalDateTime regdate, Long store_id, Long user_pk) {
        this.id = id;
        this.status = OrdersStatus.of(status);
        this.regdate = regdate;
        this.store_id = store_id;
        this.user_pk = user_pk;
    }

    public TotalOrdersDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }

    public void setStatus(String status) {
        this.status = OrdersStatus.of(status);
    }

    public String getStatus() {
        return status.getCode();
    }

    public OrdersStatus getStatusEnum() {
        return status;
    }
}
