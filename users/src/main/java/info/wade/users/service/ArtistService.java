package info.wade.users.service;

import info.wade.users.dto.ArtistDTO;
import info.wade.users.entity.Album;
import info.wade.users.entity.Artist;
import info.wade.users.repository.AlbumRepository;
import info.wade.users.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;

    public List<ArtistDTO> getAllArtists() {
        List<Artist> artists = artistRepository.findAll();
        List<ArtistDTO> artistDTOS = new ArrayList<>();
        for(Artist artist:artists){
            ArtistDTO artistDTO = new ArtistDTO();
            setArtist(artistDTO, artist);
            artistDTOS.add(artistDTO);
        }

        return artistDTOS;
    }

    public ArtistDTO getArtistById(Long id){
        Optional<Artist> queryResult = artistRepository.findById(id);
        ArtistDTO artistDTO = new ArtistDTO();
        if(queryResult.isPresent()){
            Artist artist = queryResult.get();
            setArtist(artistDTO, artist);
        }
        return artistDTO;
    }

    private void setArtist(ArtistDTO artistDTO, Artist artist) {
        artistDTO.setId(artist.getId());
        artistDTO.setName(artist.getName());
        List<Long> ids= new ArrayList<>();
        List<Album> albums = artist.getAlbums();
        for(Album album:albums){
            ids.add(album.getId());
        }
        artistDTO.setAlbumIds(ids);
    }

    public List<Album> setAlbums(List<Long> ids){
        List<Album> albums = new ArrayList<>();
        for(Long id: ids){
            Optional<Album> albumOptional = albumRepository.findById(id);
            albumOptional.ifPresent(albums::add);
        }
        return albums;
    }
    public void addArtistToAlbum(Artist artist, List<Long> ids){
        for(Long id: ids){
            Optional<Album> queryResult = albumRepository.findById(id);
            if(queryResult.isPresent()){
                Album album = queryResult.get();
                album.getArtists().add(artist);
                albumRepository.save(album);
            }
        }
    }
    public void deleteArtistToAlbum(Artist artist, List<Album> albums){
        for(Album album:albums){
            album.deleteArtist(artist);
            albumRepository.save(album);
        }
    }
    public ArtistDTO addArtist(ArtistDTO artistDTO){
        Artist artist = new Artist();
        artist.setName(artistDTO.getName());
        artist.setAlbums(this.setAlbums(artistDTO.getAlbumIds()));
        artistRepository.save(artist);
        artistDTO.setId(artist.getId());
        this.addArtistToAlbum(artist, artistDTO.getAlbumIds());
        return artistDTO;
    }
    public void deleteArtist(Long id){
        Optional<Artist> queryResult = artistRepository.findById(id);
        if(queryResult.isPresent()){
            Artist artist = queryResult.get();
            List<Album> albums = artist.getAlbums();
            for(Album album:albums){
                album.deleteArtist(artist);
                albumRepository.save(album);
            }
            artistRepository.delete(artist);
        }

    }
    public ArtistDTO updateArtist(ArtistDTO artistDTO){
        Optional<Artist> queryResult = artistRepository.findById(artistDTO.getId());
        if(queryResult.isPresent()){
            Artist artist = queryResult.get();
            this.deleteArtistToAlbum(artist, artist.getAlbums());
            artist.setAlbums(new ArrayList<>());
            artist.setName(artistDTO.getName());
            artist.setAlbums(this.setAlbums(artistDTO.getAlbumIds()));
            artistRepository.save(artist);
            artistDTO.setId(artist.getId());
            this.addArtistToAlbum(artist, artistDTO.getAlbumIds());
            return artistDTO;
        }
        return artistDTO;

    }
}
