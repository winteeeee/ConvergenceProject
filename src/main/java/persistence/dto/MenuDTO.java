package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.enums.OrdersStatus;
import persistence.enums.RegistStatus;

@Getter
@Setter
@Builder
public class MenuDTO extends DTO {
    private Long id;
    private RegistStatus status;
    private String name;
    private Integer price;
    private Integer stock;
    private Long classification_id;          // FK

    public MenuDTO() {  }

    public MenuDTO(Long id, RegistStatus status, String name, Integer price, Integer stock, Long Classification_id) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.classification_id = Classification_id;
    }

    public void setStatus(String status) {
        this.status = RegistStatus.of(status);
    }

    public String getStatus() {
        return status.getCode();
    }

    public RegistStatus getStatusEnum() {
        return status;
    }
}
