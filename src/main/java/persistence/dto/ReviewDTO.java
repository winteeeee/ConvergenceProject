package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO extends DTO {
    private long id;
    private LocalDateTime regdate;
    private String contents;
    private long upper_review_id;   // FK
    private String user_id;         // FK

    public ReviewDTO(long id, LocalDateTime regdate, String contents, long upper_review_id, String user_id) {
        this.id = id;
        this.regdate = regdate;
        this.contents = contents;
        this.upper_review_id = upper_review_id;
        this.user_id = user_id;
    }

    public ReviewDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
