package ru.practicum.main.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query(value = "select * from comments where event_id = ?1; ", nativeQuery = true)
//    List<Comment> getCommentsAndEventByEventId(long eventId);

    List<Comment> findAllByEventId(Long eventId);

//    @Query(value = "SELECT * FROM comments WHERE author_id = ?1; ", nativeQuery = true)
//    List<Comment> getByAuthorId(long userId);

    List<Comment> findAllByAuthorId(Long userId);

//    @Query(value = "select * from comments where text like '%', ?1, '%'; ", nativeQuery = true)
//    List<Comment> findAllByText(String text);

    List<Comment> findAllByTextContaining(String text);

    List<Comment> findAllByTextLike(String text);

    @Query(value = "select c from Comment c where lower(c.text) like lower(concat('%', ?1, '%')) ")
    List<Comment> findAllByText(String text);

}
