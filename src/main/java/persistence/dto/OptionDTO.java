package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionDTO extends DTO {
    private long id;
    private String name;
    private int price;
    private long menu_id;   // FK

    public OptionDTO(long id, String name, int price, long menu_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu_id = menu_id;
    }
}
