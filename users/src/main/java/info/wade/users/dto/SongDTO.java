package info.wade.users.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class SongDTO {

    private Long id;

    private String title;

    private String description;

    private float length;

    private Date release_date;

    private Long albumId;

    private List<Long> playlistIds;
}
