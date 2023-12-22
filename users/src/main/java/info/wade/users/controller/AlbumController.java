package info.wade.users.controller;

import info.wade.users.dto.AlbumDTO;
import info.wade.users.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping("/albums") //ok
    public ResponseEntity<?> getAllAlbums(){
        List<AlbumDTO> albumList = albumService.getAllAlbum();
        return new ResponseEntity<>(albumList, HttpStatus.OK);
    }
    @GetMapping("/albums/{albumId}") //ok
    public ResponseEntity<?> getAlbumById(@PathVariable Long albumId){
        AlbumDTO album = albumService.getAlbumById(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);

    }

    @PostMapping("/albums")
    public ResponseEntity<?> postAlbum(@RequestBody AlbumDTO albumDTO) {
        AlbumDTO album = albumService.addAlbum(albumDTO);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PutMapping("/albums") //ok cred, de revazut la list songs
    public ResponseEntity<?> putAlbum(@RequestBody AlbumDTO albumDTO) {
        AlbumDTO album = albumService.updateAlbum(albumDTO);
        return new ResponseEntity<>(album, HttpStatus.OK);

    }
    @DeleteMapping("/albums/{albumId}") //ok, cand stergem albumul se sterg si melodiile
    public ResponseEntity<?> deleteAlbumById(@PathVariable Long albumId){
        albumService.deleteAlbum(albumId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
