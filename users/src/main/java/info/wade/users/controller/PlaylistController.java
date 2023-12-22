package info.wade.users.controller;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/playlists") //ok
    public ResponseEntity<?> getAllPlaylists(){
        List<PlaylistDTO> playlistsList = playlistService.getAll();
        return new ResponseEntity<>(playlistsList, HttpStatus.OK);
    }
    @GetMapping("/playlists/{playlistId}") //ok
    public ResponseEntity<?> getPlaylistById(@PathVariable Long playlistId){
        PlaylistDTO playlist = playlistService.getPlaylistById(playlistId);
        return new ResponseEntity<>(playlist, HttpStatus.OK);

    }
    @GetMapping("/playlists/user/{userId}")
    public ResponseEntity<?> getPlaylistsByUserId(@PathVariable Long userId){
        List<PlaylistDTO> playlist = playlistService.getAllPlaylistsFromAUser(userId);
        return new ResponseEntity<>(playlist, HttpStatus.OK);

    }

    @GetMapping("/playlists/songs/{playlistId}") //ok
    public ResponseEntity<?> getAllSongOfPlaylist(@PathVariable Long playlistId){
        List<SongDTO> songs = playlistService.getAllSongsByPlaylist(playlistId);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @PostMapping("/playlists") //ok
    public ResponseEntity<?> postPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        PlaylistDTO playlist = playlistService.createPlaylist(playlistDTO);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }
    @PutMapping("/playlists/{playlistId}/songs/{songId}") //ok
    public ResponseEntity<?> postSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId){
        playlistService.addSongToPlaylist(playlistId, songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/playlist/{playlistId}/songs/{songId}") //ok
    public ResponseEntity<?> deleteSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId){
        playlistService.deleteSongFromPlaylist(playlistId, songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/playlists") //ok
    public ResponseEntity<?> putPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        PlaylistDTO playlist = playlistService.updatePlaylist(playlistDTO);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @DeleteMapping("/playlists/{playlistId}") //ok
    public ResponseEntity<?> deletePlaylistById(@PathVariable Long playlistId){
        playlistService.deletePlaylist(playlistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
