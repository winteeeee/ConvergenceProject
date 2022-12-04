package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.enums.RegistStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class StoreDTO extends DTO {
    private Long id;
    private RegistStatus status;
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

    public StoreDTO(Long id, RegistStatus status, String name, String comment, String phone, String address, Integer review_count, Integer star_rating, LocalDateTime open_time, LocalDateTime close_time, Long user_pk) {
        this.id = id;
        this.status = status;
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

    public void setStatus(String status) {
        this.status = RegistStatus.of(status);
    }

    public String getStatus() {
        return status.getCode();
    }

    public RegistStatus getStatusEnum() {
        return status;
    }
}
