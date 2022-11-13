package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO extends DTO {
    private Long id;
    private String contents;
    private LocalDateTime regdate;
    private Integer star_rating;
    private Long user_pk;           // FK
    private Long orders_id;          // FK

    public ReviewDTO() {  }

    public ReviewDTO(Long id, String contents, LocalDateTime regdate, Integer star_rating, Long user_pk, Long orders_id) {
        this.id = id;
        this.contents = contents;
        this.regdate = regdate;
        this.star_rating = star_rating;
        this.user_pk = user_pk;
        this.orders_id = orders_id;
    }

    public ReviewDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
