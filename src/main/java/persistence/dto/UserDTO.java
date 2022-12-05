package persistence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.enums.Authority;
import persistence.enums.RegistStatus;

@Getter
@Setter
@Builder
public class UserDTO extends DTO {
    private Long pk;
    private RegistStatus status;
    private Authority authority;
    private String id;
    private String pw;
    private String name;
    private String phone;
    private Integer age;

    public UserDTO() {  }

    public UserDTO(Long pk, RegistStatus status, Authority authority, String id, String pw, String name, String phone, Integer age) {
        this.pk = pk;
        this.status = status;
        this.authority = authority;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    public UserDTO(Long pk, String status, String authority, String id, String pw, String name, String phone, Integer age) {
        this.pk = pk;
        this.status = RegistStatus.of(status);
        this.authority = Authority.of(authority);
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }


    public void setStatus(String status) {
        this.status = RegistStatus.of(status);
    }

    public String getStatus() {
        return status.getCode();
    }

    public void setAuthority(String authority) {
        this.authority = Authority.of(authority);
    }

    public String getAuthority() {
        return authority.getCode();
    }

    public Authority getAuthorityEnum() {
        return authority;
    }
}