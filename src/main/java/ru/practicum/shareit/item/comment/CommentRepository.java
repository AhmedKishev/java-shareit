package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Поиск по ID предмета
    List<Comment> findAllByItemId(long itemId);

    // Поиск по ID автора
    List<Comment> findAllByAuthorId(long userId);
}
