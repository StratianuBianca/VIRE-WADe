package info.wade.users.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArtistDTO {
    private Long id;

    private String name;

    private List<Long> albumIds;
}
