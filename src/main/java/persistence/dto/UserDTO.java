package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Authority;
import persistence.enums.RegistStatus;

@Getter
@Setter
public class UserDTO extends DTO {
    private Long pk;
    private Authority authority;
    private String id;
    private String pw;
    private String name;
    private Integer age;

    public UserDTO() {}

    public UserDTO(Long pk, Integer authority, String id, String pw, String name, Integer age) {
        this.pk = pk;
        this.authority = Authority.of(authority);
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.age = age;
    }

    public UserDTO(byte[] arr) {
        setMembersByByteArray(arr);
    }

    public void setAuthority(Integer authority) {
        this.authority = Authority.of(authority);
    }

    public Integer getAuthority() {
        return authority.getCode();
    }

    public Authority getAuthorityEnum() {
        return authority;
    }
}