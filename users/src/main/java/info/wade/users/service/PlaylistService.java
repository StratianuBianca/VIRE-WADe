package info.wade.users.service;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.entity.Playlist;
import info.wade.users.entity.Song;
import info.wade.users.entity.User;
import info.wade.users.repository.PlaylistRepository;
import info.wade.users.repository.SongRepository;
import info.wade.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    private List<PlaylistDTO> getAllPlaylistDTOS(List<Playlist> playlists){
        List<PlaylistDTO> playlistDTOS = new ArrayList<>();
        for(Playlist playlist:playlists){
            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.setCategory(playlist.getCategory());
            playlistDTO.setTitle(playlist.getTitle());
            playlistDTO.setId(playlist.getPlaylist_id());
            playlistDTO.setCreatedById(playlist.getCreatedBy().getId());
            playlistDTO.setCreatedDate(playlist.getCreate_date());
            List<Long> ids = new ArrayList<>();
            List<Song> songs = playlist.getSongs();
            for(Song song:songs){
                ids.add(song.getId());
            }
            playlistDTO.setSongIds(ids);
            playlistDTOS.add(playlistDTO);
        }
        return playlistDTOS;
    }
    public List<PlaylistDTO> getAll(){
        List<Playlist> playlists = playlistRepository.findAll();
        return this.getAllPlaylistDTOS(playlists);
    }
    public PlaylistDTO getPlaylistById(Long id){
        Optional<Playlist> queryResult = playlistRepository.findById(id);
        PlaylistDTO playlistDTO = new PlaylistDTO();
        if(queryResult.isPresent()){
            Playlist playlist = queryResult.get();
            playlistDTO.setTitle(playlist.getTitle());
            playlistDTO.setId(playlist.getPlaylist_id());
            playlistDTO.setCategory(playlist.getCategory());
            playlistDTO.setCreatedById(playlist.getCreatedBy().getId());
            playlistDTO.setCreatedDate(playlist.getCreate_date());
            List<Song> songs = playlist.getSongs();
            List<Long> ids = new ArrayList<>();
            for(Song song:songs){
                ids.add(song.getId());
            }
            playlistDTO.setSongIds(ids);
        }
        return playlistDTO;
    }
    public void deletePlaylist(Long id){
        Optional<Playlist> queryResult = playlistRepository.findById(id);
        if(queryResult.isPresent()){
            Playlist playlist = queryResult.get();
            List<Song> songs = playlist.getSongs();
            songRepository.saveAll(songs);
            playlistRepository.delete(playlist);
        }
    }
    public PlaylistDTO createPlaylist(PlaylistDTO playlistDTO){
        Playlist playlist = new Playlist();
        User user = userRepository.findById(playlistDTO.getCreatedById());
        if(user != null){
            playlist.setCategory(playlistDTO.getCategory());
            playlist.setCreatedBy(user);
            playlist.setTitle(playlistDTO.getTitle());
            playlist.setCreate_date(playlistDTO.getCreatedDate());
            List<Song> songs = this.setSongs(playlistDTO.getSongIds());
            playlist.setSongs(songs);
            playlistRepository.save(playlist);
            setPlaylistToSong(playlistDTO.getSongIds(), playlist);
            playlistDTO.setId(playlist.getPlaylist_id());
        }
        return playlistDTO;
    }

    public PlaylistDTO updatePlaylist(PlaylistDTO playlistDTO){
        Optional<Playlist> queryResult = playlistRepository.findById(playlistDTO.getId());
        User user = userRepository.findById(playlistDTO.getCreatedById());
        if(queryResult.isPresent() && user != null){
            Playlist playlist = queryResult.get();
            playlist.setTitle(playlistDTO.getTitle());
            playlist.setCategory(playlistDTO.getCategory());
            playlist.setCreatedBy(user);
            playlist.setCreate_date(playlist.getCreate_date());
            playlist.setSongs(this.setSongs(playlistDTO.getSongIds()));
            playlistRepository.save(playlist);
            setPlaylistToSong(playlistDTO.getSongIds(), playlist);
            playlistDTO.setId(playlist.getPlaylist_id());
        }
        return playlistDTO;
    }

    public List<PlaylistDTO> getAllPlaylistsFromAUser(Long userId){
        User user = userRepository.findById(userId);
        List<Playlist> playlists = new ArrayList<>();
        if(user != null){
            playlists = playlistRepository.getPlaylistsByCreatedBy(user);
        }

        return this.getAllPlaylistDTOS(playlists);
    }

    public List<SongDTO> getAllSongsByPlaylist(Long playlistId){
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        List<SongDTO> songDTOS = new ArrayList<>();
        if(playlist.isPresent()){
            for(Song song:playlist.get().getSongs()){
                SongDTO songDTO = new SongDTO();
                songDTO.setId(song.getId());
                songDTO.setLength(song.getLength());
                songDTO.setDescription(song.getDescription());
                songDTO.setRelease_date(song.getRelease_date());
                songDTO.setTitle(song.getTitle());
                songDTO.setAlbumId(song.getAlbum().getId());
                songDTOS.add(songDTO);
            };
        }
        return songDTOS;
    }

    public void addSongToPlaylist(Long playlistId, Long songId){
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        Optional<Song> song = songRepository.findById(songId);
        if(playlist.isPresent() && song.isPresent()){
            Playlist playlist1 = playlist.get();
            Song song1 = song.get();
            playlist1.getSongs().add(song1);
            playlistRepository.save(playlist1);
            setPlaylistToSong(List.of(songId), playlist.get());
        }
    }

    public void deleteSongFromPlaylist(Long playlistId, Long songId){
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        Optional<Song> song = songRepository.findById(songId);
        if(playlist.isPresent() && song.isPresent()){
            Playlist playlist1 = playlist.get();
            Song song1 = song.get();
            playlist1.getSongs().remove(song1);
            playlistRepository.save(playlist1);
            this.deletePlaylistFromSong(List.of(songId), playlist1);
        }

    }

    public List<Song> setSongs(List<Long> ids){
        List<Song> songs = new ArrayList<>();
        for(Long id:ids){
            Optional<Song> song = songRepository.findById(id);
            song.ifPresent(songs::add);
        }
        return songs;
    }
    public void deletePlaylistFromSong(List<Long> ids, Playlist playlist){
        for(Long id:ids){
            Optional<Song> queryResult = songRepository.findById(id);
            if(queryResult.isPresent()){
                Song song = queryResult.get();
                songRepository.save(song);
            }
        }
    }
    public void setPlaylistToSong(List<Long> ids, Playlist playlist){
        for(Long id:ids){
            Optional<Song> queryResult = songRepository.findById(id);
            if(queryResult.isPresent()){
                Song song = queryResult.get();
                songRepository.save(song);
            }
        }
    }

}
