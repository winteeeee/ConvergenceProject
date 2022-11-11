package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import persistence.enums.Authority;

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
}