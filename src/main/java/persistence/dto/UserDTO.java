package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Authority;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String pw;
    private Authority authority;
    private String address;

    public UserDTO(String id, String pw, Authority authority, String address) {
        this.id = id;
        this.pw = pw;
        this.authority = authority;
        this.address = address;
    }
}