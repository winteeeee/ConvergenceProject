package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO extends DTO {
    private Long id;
    private String group;
    private String name;
    private Integer price;
    private Integer stock;
    private Integer store_id;          // FK

    public MenuDTO() {  }

    public MenuDTO(Long id, String group, String name, Integer price, Integer stock, Integer store_id) {
        this.id = id;
        this.group = group;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.store_id = store_id;
    }

    public MenuDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
