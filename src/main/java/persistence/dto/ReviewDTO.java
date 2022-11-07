package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO extends DTO {
    private long id;
    private String contents;
    private LocalDateTime regdate;
    private int star_rating;
    private long upper_review_id;   // FK
    private String user_id;         // FK

    public ReviewDTO(long id, String contents, LocalDateTime regdate, int star_rating, long upper_review_id, String user_id) {
        this.id = id;
        this.contents = contents;
        this.regdate = regdate;
        this.star_rating = star_rating;
        this.upper_review_id = upper_review_id;
        this.user_id = user_id;
    }

    public ReviewDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
