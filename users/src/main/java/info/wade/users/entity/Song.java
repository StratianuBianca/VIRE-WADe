package info.wade.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "songs")
@Data
@Getter
@Setter
public class Song {

    @Id
    @Column(name = "song_id", length = 45)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String description;

    private float length;

    private Date release_date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "album_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Album album;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "songs")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Playlist> playlists;

    public void addPlaylist(Playlist playlist){
        playlists.add(playlist);
    }

    public void deletePlaylist(Playlist playlist){
        playlists.remove(playlist);
    }
    public void deleteAlbum(Album album){
        album = new Album();
    }

}
