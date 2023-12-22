package info.wade.users.service;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.entity.Album;
import info.wade.users.entity.Playlist;
import info.wade.users.entity.Song;
import info.wade.users.repository.AlbumRepository;
import info.wade.users.repository.PlaylistRepository;
import info.wade.users.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PlaylistRepository playlistRepository;


    public List<SongDTO> getAllSongs(){
        List<Song> songs = songRepository.findAll();
        List<SongDTO> songDTOS = new ArrayList<>();
        for(Song song:songs){
            SongDTO songDTO = new SongDTO();
            songDTO.setDescription(song.getDescription());
            songDTO.setLength(song.getLength());
            songDTO.setTitle(song.getTitle());
            songDTO.setRelease_date(song.getRelease_date());
            songDTO.setId(song.getId());
            songDTO.setAlbumId(song.getAlbum().getId());
            List<Long> ids = new ArrayList<>();
            songDTOS.add(songDTO);
        }
        return songDTOS;
    }

    public SongDTO getSongById(Long songId){
        Optional<Song> queryResult = songRepository.findById(songId);
        SongDTO songDTO = new SongDTO();
        if(queryResult.isPresent()){
            Song song = queryResult.get();
            songDTO.setDescription(song.getDescription());
            songDTO.setLength(song.getLength());
            songDTO.setTitle(song.getTitle());
            songDTO.setRelease_date(song.getRelease_date());
            songDTO.setId(song.getId());

        }
        return songDTO;
    }

    public void deleteSong(Long songId){
        Optional<Song> queryResult = songRepository.findById(songId);
        if(queryResult.isPresent()){
            Song song = queryResult.get();
            songRepository.delete(song);
        }
    }

    public SongDTO createSong(SongDTO songDTO){
        Optional<Album> album = albumRepository.findById(songDTO.getAlbumId());
        Song song = new Song();
        if(album.isPresent()){
            song.setAlbum(album.get());
            song.setLength(songDTO.getLength());
            song.setTitle(songDTO.getTitle());
            song.setDescription(songDTO.getDescription());
            song.setRelease_date(songDTO.getRelease_date());
            songRepository.save(song);
            Album album1 = album.get();
            album1.addToSongs(song);
            albumRepository.save(album1);
            songDTO.setId(song.getId());
        }
        return songDTO;
    }
    public SongDTO updateSong(SongDTO songDTO){
        Optional<Song> queryResult = songRepository.findById(songDTO.getId());
        Optional<Album> album = albumRepository.findById(songDTO.getAlbumId());
        if(queryResult.isPresent() && album.isPresent()){
            Song song = queryResult.get();
            song.setAlbum(album.get());
            song.setLength(songDTO.getLength());
            song.setTitle(songDTO.getTitle());
            song.setDescription(songDTO.getDescription());
            song.setRelease_date(songDTO.getRelease_date());
            songRepository.save(song);
            Album album1 = album.get();
            album1.addToSongs(song);
            albumRepository.save(album1);
            songDTO.setId(song.getId());
        }
        return songDTO;
    }
    public List<PlaylistDTO> getAllPlaylistWithSong(Long songId){
        Optional<Song> queryResult = songRepository.findById(songId);
        return new ArrayList<>();
    }

    private List<Playlist> setPlaylist(List<Long> ids){
        List<Playlist> playlists = new ArrayList<>();
        for(Long id:ids){
            Optional<Playlist> playlist = playlistRepository.findById(id);
            playlist.ifPresent(playlists::add);
        }
        System.out.println(playlists);
        return playlists;
    }
    private void addSongToPlaylist(List<Long> ids, Song song){
        for(Long id:ids){
            Optional<Playlist> playlist = playlistRepository.findById(id);
            if(playlist.isPresent()){
                playlist.get().addSong(song);
                playlistRepository.save(playlist.get());
            }
        }

    }
}
