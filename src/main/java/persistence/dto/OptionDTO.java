package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionDTO extends DTO {
    private Long id;
    private String name;
    private Integer price;
    private Long store_id;   // FK

    public OptionDTO() {  }

    public OptionDTO(Long id, String name, Integer price, Long store_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.store_id = store_id;
    }

    public OptionDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
