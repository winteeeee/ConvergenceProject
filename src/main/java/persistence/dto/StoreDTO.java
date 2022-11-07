package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreDTO extends DTO {
    private long id;
    private String name;
    private String tag;
    private int carry_price;
    private String phone;
    private String address;
    private int carry_sale;
    private int review_count;
    private int star_rating;
    private LocalDateTime open_time;
    private LocalDateTime close_time;
    private String user_id;         // FK
    private long ghost_review_id;   // FK

    public StoreDTO(long id, String name, String tag, int carry_price, String phone, String address, int carry_sale, int review_count, int star_rating, String user_id, long ghost_review_id) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.carry_price = carry_price;
        this.phone = phone;
        this.address = address;
        this.carry_sale = carry_sale;
        this.review_count = review_count;
        this.star_rating = star_rating;
        this.user_id = user_id;
        this.ghost_review_id = ghost_review_id;
    }
}
