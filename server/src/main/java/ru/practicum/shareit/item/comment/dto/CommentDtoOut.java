package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDtoOut {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
