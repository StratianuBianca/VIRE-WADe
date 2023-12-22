package info.wade.users.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlaylistDTO {
    private Long id;

    private String title;
    private String category;
    private List<Long> songIds;
    private Long createdById;
    private Date createdDate;
}
