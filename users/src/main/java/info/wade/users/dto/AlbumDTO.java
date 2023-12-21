package info.wade.users.dto;


import lombok.Data;

import java.util.List;

@Data
public class AlbumDTO {
    private Long id;
    private String title;
    private String description;
    private List<Long> artistIds;
    private List<Long> songIds;
}
