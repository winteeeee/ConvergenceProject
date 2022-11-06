package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import sharing.RootDTO;

@Getter
@Setter
public class StoreDTO implements RootDTO {
    private long id;
    private String name;
    private String tag;     // TODO 따로 테이블 구성할지 판단
    private int carry_price;
    private String phone;
    private String address;
    private String user_id;
    private int carry_sale;

    public StoreDTO(long id, String name, String tag, int carry_price, String phone, String address, String user_id, int carry_sale) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.carry_price = carry_price;
        this.phone = phone;
        this.address = address;
        this.user_id = user_id;
        this.carry_sale = carry_sale;
    }
}
