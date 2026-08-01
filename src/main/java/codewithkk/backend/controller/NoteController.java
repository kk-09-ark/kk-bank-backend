package codewithkk.backend.controller;

import codewithkk.backend.entity.Note;
import codewithkk.backend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public Note addNote(@RequestBody Note note) {
        return noteService.addNote(note);
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadNote(@PathVariable String id) throws IOException {
        Note note = noteService.getNoteById(id);
        if (note == null || note.getPdfUrl() == null || note.getPdfUrl().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(note.getPdfUrl()).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(60000);

        try (InputStream in = conn.getInputStream()) {
            byte[] content = in.readAllBytes();
            String filename = sanitizeFilename(note.getTitle()) + ".pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
                    .body(content);
        } finally {
            conn.disconnect();
        }
    }

    private String sanitizeFilename(String title) {
        String t = title == null || title.isBlank() ? "notes" : title.trim();
        t = t.replaceAll("[\\\\/:*?\"<>|\\r\\n]+", "_");
        t = t.replaceAll("\\s+", "_");
        t = t.replaceAll("_+", "_");
        return t.replaceAll("^_+|_+$", "");
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable String id,
                           @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable String id) {

        noteService.deleteNote(id);

        return "Note Deleted Successfully";
    }
}