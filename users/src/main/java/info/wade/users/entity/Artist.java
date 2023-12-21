package info.wade.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "artist")
@Data
@Getter
@Setter
public class Artist {

    @Id
    @Column(name = "artist_id", length = 45)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
    private List<Album> albums;


}
