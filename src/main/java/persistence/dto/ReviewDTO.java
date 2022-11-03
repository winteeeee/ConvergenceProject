package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private long id;
    private String contents;
    private long upper_review_id;   // FK
    // TODO user_id 필요할 것 같음

    public ReviewDTO(long id, String contents, long upper_review_id) {
        this.id = id;
        this.contents = contents;
        this.upper_review_id = upper_review_id;
    }
}
