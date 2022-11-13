package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO extends DTO {
    private Long id;
    private String group_name;
    private String name;
    private Integer price;
    private Integer stock;
    private Long store_id;          // FK

    public MenuDTO() {  }

    public MenuDTO(Long id, String group_name, String name, Integer price, Integer stock, Long store_id) {
        this.id = id;
        this.group_name = group_name;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.store_id = store_id;
    }

    public MenuDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
