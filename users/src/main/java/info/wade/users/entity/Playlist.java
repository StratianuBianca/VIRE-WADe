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
@Table(name = "playlists")
@Data
@Getter
@Setter
public class Playlist {
    @Id
    @Column(name = "playlist_id", length = 45)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long playlist_id;

    private String title;
    private String category;

    @ManyToMany(
            fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User createdBy;

    private Date create_date;

    public void addSong(Song song){
        this.songs.add(song);
    }
}
