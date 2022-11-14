package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO extends DTO {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private Long classification_id;          // FK

    public MenuDTO() {  }

    public MenuDTO(Long id, String name, Integer price, Integer stock, Long Classification_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.classification_id = Classification_id;
    }

    public MenuDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }

    @Override
    public String toString() {
        return name + "," + price + "원,";
    }
}
