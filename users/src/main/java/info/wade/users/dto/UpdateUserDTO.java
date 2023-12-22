package info.wade.users.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {

    private Long id;

    private String name;

    private String email;

    private String password;
}
