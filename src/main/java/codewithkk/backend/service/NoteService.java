package codewithkk.backend.service;

import codewithkk.backend.entity.Note;
import codewithkk.backend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // Add Note
    public Note addNote(Note note) {
        return noteRepository.save(note);
    }

    // Get All Notes
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Get Note By Id
    public Note getNoteById(String id) {
        return noteRepository.findById(id).orElse(null);
    }

    // Update Note
    public Note updateNote(String id, Note note) {

        Note existingNote = noteRepository.findById(id).orElse(null);

        if (existingNote == null) {
            return null;
        }

        existingNote.setTitle(note.getTitle());
        existingNote.setDescription(note.getDescription());
        existingNote.setPdfUrl(note.getPdfUrl());
        existingNote.setPrice(note.getPrice());
        existingNote.setActive(note.isActive());

        return noteRepository.save(existingNote);
    }

    // Delete Note
    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}