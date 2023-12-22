package info.wade.users.service;

import info.wade.users.dto.AlbumDTO;
import info.wade.users.entity.Album;
import info.wade.users.entity.Artist;
import info.wade.users.entity.Song;
import info.wade.users.repository.AlbumRepository;
import info.wade.users.repository.ArtistRepository;
import info.wade.users.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    SongRepository songRepository;

    @Autowired
    ArtistRepository artistRepository;

    public List<AlbumDTO> getAllAlbum(){
        List<Album> albums = albumRepository.findAll();
        List<AlbumDTO> albumDTOS = new ArrayList<>();
        for(Album album:albums){
            AlbumDTO albumDTO = new AlbumDTO();
            albumDTO.setId(album.getId());
            albumDTO.setDescription(album.getDescription());
            albumDTO.setTitle(album.getTitle());
            setAlbum(albumDTO, album);
            albumDTOS.add(albumDTO);
        }
        return albumDTOS;

    }

    public AlbumDTO getAlbumById(Long albumId){
        Optional<Album> queryResult = albumRepository.findById(albumId);
        AlbumDTO albumDTO = new AlbumDTO();
        if(queryResult.isPresent()){
            Album album = queryResult.get();
            albumDTO.setTitle(album.getTitle());
            albumDTO.setDescription(album.getDescription());
            albumDTO.setId(album.getId());
            setAlbum(albumDTO, album);
        }
        return albumDTO;
    }

    private void setAlbum(AlbumDTO albumDTO, Album album) {
        List<Artist> artists = album.getArtists();
        List<Long> artistsIds = new ArrayList<>();
        for(Artist artist:artists){
            artistsIds.add(artist.getId());
        }
        albumDTO.setArtistIds(artistsIds);
        List<Song> songs = album.getSongs();
        List<Long> songIds = new ArrayList<>();
        for(Song song:songs){
            songIds.add(song.getId());
        }
        albumDTO.setSongIds(songIds);
    }

    public AlbumDTO addAlbum(AlbumDTO newAlbum){
        Album album = new Album();
        album.setTitle(newAlbum.getTitle());
        album.setDescription(newAlbum.getDescription());
        this.setArtistToAlbum(album, newAlbum.getArtistIds());
        this.setSongToAlbum(newAlbum, album);
        return newAlbum;

    }
    private void setArtistToAlbum(Album album, List<Long> ids){
        List<Artist> artists = new ArrayList<>();
        for(Long id:ids){
            Optional<Artist> queryResult = artistRepository.findById(id);
            if(queryResult.isPresent()){
                Artist artist = queryResult.get();
                artists.add(artist);
                artist.getAlbums().add(album);
                artistRepository.save(artist);
            }
        }
        album.setArtists(artists);
    }
    private List<Artist> setArtist(List<Long> artistIds){
        List<Artist> artists = new ArrayList<>();
        for(Long id: artistIds){
            Optional<Artist> queryResult = artistRepository.findById(id);
            queryResult.ifPresent(artists::add);
        }
        return artists;
    }
    private List<Song> setSongs(List<Long> songsIds, Album album){
        List<Song> songs = new ArrayList<>();
        for(Long id: songsIds){
            Optional<Song> queryResult = songRepository.findById(id);
            queryResult.ifPresent(songs::add);
        }
        return songs;
    }

    public void deleteAlbum(Long id){
        Optional<Album> queryResult = albumRepository.findById(id);
        if(queryResult.isPresent()){
            Album album = queryResult.get();
            List<Artist> artists = album.getArtists();
            for(Artist artist:artists){
                artist.getAlbums().remove(album);
                artistRepository.save(artist);
            }
            List<Song> songs = album.getSongs();
            for(Song song:songs){
                song.setAlbum(new Album());
                songRepository.save(song);
            }
            albumRepository.delete(album);
        }
    }

    public AlbumDTO updateAlbum(AlbumDTO albumDTO){
        Optional<Album> queryResult = albumRepository.findById(albumDTO.getId());
        if(queryResult.isPresent()){
            Album album = queryResult.get();
            album.setTitle(albumDTO.getTitle());
            album.setDescription(albumDTO.getDescription());
            this.deleteAlbumFromSong(album.getSongs(), album);
            album.setSongs(new ArrayList<>());
            setSongToAlbum(albumDTO, album);
        }
        return albumDTO;
    }
    public void deleteAlbumFromSong(List<Song> songs, Album album){
        for(Song song:songs){
            song.deleteAlbum();
            songRepository.save(song);
        }
    }

    private void setSongToAlbum(AlbumDTO albumDTO, Album album) {
        List<Song> songs = this.setSongs(albumDTO.getSongIds(), album);
        album.setSongs(songs);
        List<Artist> artists = setArtist(albumDTO.getArtistIds());
        album.setArtists(artists);
        albumRepository.save(album);
        albumDTO.setId(album.getId());
    }

}
