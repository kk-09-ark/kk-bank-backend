package codewithkk.backend.controller;

import codewithkk.backend.entity.Note;
import codewithkk.backend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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