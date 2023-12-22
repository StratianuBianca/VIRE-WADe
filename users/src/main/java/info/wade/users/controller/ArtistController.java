package info.wade.users.controller;

import info.wade.users.dto.ArtistDTO;
import info.wade.users.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/artists") //ok
    public ResponseEntity<?> getAllArtists(){
        List<ArtistDTO> artistList = artistService.getAllArtists();
        return new ResponseEntity<>(artistList, HttpStatus.OK);
    }
    @GetMapping("/artists/{artistId}") //ok
    public ResponseEntity<?> getArtistById(@PathVariable Long artistId){
        ArtistDTO artist = artistService.getArtistById(artistId);
        return new ResponseEntity<>(artist, HttpStatus.OK);

    }
    @PostMapping("/artists") //ok
    public ResponseEntity<?> postArtist(@RequestBody ArtistDTO artistDTO){
        ArtistDTO artist = artistService.addArtist(artistDTO);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }
    @PutMapping("/artists") //ok
    public ResponseEntity<?> putArtist(@RequestBody ArtistDTO artistDTO){
        ArtistDTO artist = artistService.updateArtist(artistDTO);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @DeleteMapping("/artists/{artistId}") //ok
    public ResponseEntity<?> deleteArtistById(@PathVariable Long artistId){
        artistService.deleteArtist(artistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
