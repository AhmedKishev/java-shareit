package ru.practicum.shareit.item.comment.mapper;

import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.comment.model.Comment;

public class CommentMapper {
    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        return new CommentDtoOut(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
