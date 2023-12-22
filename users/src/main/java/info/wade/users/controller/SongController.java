package info.wade.users.controller;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("/songs") //ok
    public ResponseEntity<?> getAllSongs(){
        List<SongDTO> songList = songService.getAllSongs();
        return new ResponseEntity<>(songList, HttpStatus.OK);
    }
    @GetMapping("/songs/{songId}") //ok
    public ResponseEntity<?> getSongById(@PathVariable Long songId){
        SongDTO song = songService.getSongById(songId);
        return new ResponseEntity<>(song, HttpStatus.OK);

    }
    @PostMapping("/songs") //ok
    public ResponseEntity<?> postSong(@RequestBody SongDTO songDTO) {
        SongDTO song = songService.createSong(songDTO);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PutMapping("/songs") //ok
    public ResponseEntity<?> putSong(@RequestBody SongDTO songDTO) {
        SongDTO song = songService.updateSong(songDTO);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }
    @GetMapping("/songs/playlists/{songId}") //ok
    public ResponseEntity<?> getAllPlaylistWithSong(@PathVariable Long songId){
        List<PlaylistDTO> playlists = songService.getAllPlaylistWithSong(songId);
        return new ResponseEntity<>(playlists, HttpStatus.OK);

    }

    @DeleteMapping("/songs/{songId}") ///ok
    public ResponseEntity<?> deleteSongById(@PathVariable Long songId){
        songService.deleteSong(songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
