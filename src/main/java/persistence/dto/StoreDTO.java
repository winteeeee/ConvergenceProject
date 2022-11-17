package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreDTO extends DTO {
    private Long id;
    private String name;
    private String comment;
    private String phone;
    private String address;
    private Integer review_count;
    private Integer star_rating;
    private LocalDateTime open_time;
    private LocalDateTime close_time;
    private Long user_pk;         // FK

    public StoreDTO() {  }

    public StoreDTO(Long id, String name, String comment, String phone, String address, Integer review_count, Integer star_rating, LocalDateTime open_time, LocalDateTime close_time, Long user_pk) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.phone = phone;
        this.address = address;
        this.review_count = review_count;
        this.star_rating = star_rating;
        this.open_time = open_time;
        this.close_time = close_time;
        this.user_pk = user_pk;
    }

    public StoreDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
