package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.enums.OrdersStatus;
import persistence.enums.RegistStatus;

@Getter
@Setter
@Builder
public class StatisticsDTO extends DTO {
    private Long id;
    private String name;
    private Integer price;
    private Integer count;

    public StatisticsDTO() {}

    public StatisticsDTO(Long id, String name, Integer price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
