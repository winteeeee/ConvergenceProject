package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Status;

@Getter
@Setter
public class MenuRegistDTO extends DTO {
    private long id;
    private Status status;
    private String comment;
    private String name;
    private int price;
    private long store_id;      // FK

    public MenuRegistDTO(long id, Status status, String comment, String name, int price, long store_id) {
        this.id = id;
        this.status = status;
        this.comment = comment;
        this.name = name;
        this.price = price;
        this.store_id = store_id;
    }
}
