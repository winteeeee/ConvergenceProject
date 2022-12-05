package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClassificationDTO{
    private Long id;
    private String name;
    private Long store_id;      // FK

    public ClassificationDTO() {  }

    public ClassificationDTO(Long id, String name, Long store_id) {
        this.id = id;
        this.name = name;
        this.store_id = store_id;
    }
}
