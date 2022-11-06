package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Status;
import sharing.RootDTO;

@Getter
@Setter
public class StoreRegistDTO implements RootDTO {
    private long id;
    private Status status;
    private String comment;
    private String name;
    private String tag;
    private String phone;
    private String address;
    private String user_id;     // FK

    public StoreRegistDTO(long id, int status, String comment, String name, String tag, String phone, String address, String user_id) {
        this.id = id;
        this.status = Status.of(status);
        this.comment = comment;
        this.name = name;
        this.tag = tag;
        this.phone = phone;
        this.address = address;
        this.user_id = user_id;
    }
}
