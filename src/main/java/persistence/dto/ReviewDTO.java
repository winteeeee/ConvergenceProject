package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewDTO extends DTO {
    private Long id;
    private String comment;
    private String owner_comment;
    private LocalDateTime regdate;
    private Integer star_rating;
    private Long user_pk;           // FK
    private Long total_orders_id;          // FK

    public ReviewDTO() {  }

    public ReviewDTO(Long id, String comment, String owner_comment, LocalDateTime regdate, Integer star_rating, Long user_pk, Long total_orders_id) {
        this.id = id;
        this.comment = comment;
        this.owner_comment = owner_comment;
        this.regdate = regdate;
        this.star_rating = star_rating;
        this.user_pk = user_pk;
        this.total_orders_id = total_orders_id;
    }

    public ReviewDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
