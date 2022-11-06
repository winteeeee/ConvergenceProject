package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import sharing.RootDTO;

@Getter
@Setter
public class OptionDTO implements RootDTO {
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
