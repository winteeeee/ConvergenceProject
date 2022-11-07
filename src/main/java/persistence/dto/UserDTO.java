package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Authority;

@Getter
@Setter
public class UserDTO extends DTO {
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

    public UserDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }
}