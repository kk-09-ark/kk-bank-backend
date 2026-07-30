package codewithkk.backend.repository;

import codewithkk.backend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {

    List<Note> findByActiveTrue();

    long countByActiveTrue();
}
