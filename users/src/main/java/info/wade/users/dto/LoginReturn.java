package info.wade.users.dto;

import lombok.Data;

@Data
public class LoginReturn {
    private String jwt;
    private Long id;
    private String username;
}
