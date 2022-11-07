package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO extends DTO {
    private long id;
    private String name;
    private int price;
    private int sale;
    private long store_id;          // FK
    private long ghost_review_id;   // FK

    public MenuDTO(long id, String name, int price, int sale, long store_id, long ghost_review_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sale = sale;
        this.store_id = store_id;
        this.ghost_review_id = ghost_review_id;
    }
}
