package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DetailsDTO extends DTO {
    private Long id;
    private String name;
    private Integer price;
    private Long store_id;   // FK

    public DetailsDTO() {  }

    public DetailsDTO(Long id, String name, Integer price, Long store_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.store_id = store_id;
    }
}
