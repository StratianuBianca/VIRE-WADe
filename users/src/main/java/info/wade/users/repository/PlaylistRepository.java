package info.wade.users.repository;

import info.wade.users.entity.Playlist;
import info.wade.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {

    List<Playlist> getPlaylistsByCreatedBy(User user);
}
