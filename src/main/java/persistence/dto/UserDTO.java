package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Authority;
import sharing.RootDTO;

@Getter
@Setter
public class UserDTO implements RootDTO {
    private String id;
    private String pw;
    private Authority authority;
    private String address;

    public UserDTO(String id, String pw, int authority, String address) {
        this.id = id;
        this.pw = pw;
        this.authority = Authority.of(authority);
        this.address = address;
    }
}