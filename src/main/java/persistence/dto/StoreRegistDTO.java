package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Status;

@Getter
@Setter
public class StoreRegistDTO extends DTO {
    private Long id;
    private Status status;
    private String name;
    private String comment;
    private String phone;
    private String address;
    private Long user_pk;           // FK

    public StoreRegistDTO() {  }

    public StoreRegistDTO(Long id, Integer status, String name, String comment, String phone, String address, Long user_pk) {
        this.id = id;
        this.status = Status.of(status);
        this.name = name;
        this.comment = comment;
        this.phone = phone;
        this.address = address;
        this.user_pk = user_pk;
    }

    public StoreRegistDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}
