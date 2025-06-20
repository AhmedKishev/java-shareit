package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.DescriptionException;
import ru.practicum.shareit.exception.FailBooking;
import ru.practicum.shareit.exception.NameException;
import ru.practicum.shareit.exception.ObjectNotFound;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    UserRepository userRepository;

    ItemRepository itemRepository;

    CommentRepository commentRepository;

    BookingRepository bookingRepository;

    @Override
    @Transactional
    public Item save(Long userId, ItemDto item) {
        Optional<User> master = userRepository.findById(userId);
        checkUserAndItemDto(userId, master, item);
        Item saveItem = ItemMapper.toItem(item);
        saveItem.setOwner(master.get());
        log.info(saveItem.toString());
        return itemRepository.save(saveItem);
    }

    @Transactional
    @Override
    public Item edit(long itemId, ItemDto editItem, Long userId) {
        Item updateItem = itemRepository.findById(itemId).get();
        if (!updateItem.getOwner().getId().equals(userId)) {
            throw new ObjectNotFound("У пользователя с Id=" + userId + " нет предмета с itemId=" + itemId);
        }
        editItem(updateItem, editItem);
        log.info(updateItem.toString());
        itemRepository.save(updateItem);
        return updateItem;
    }

    @Transactional
    @Override
    public ItemDtoOut getItemById(long itemId) {
        if (itemId > itemRepository.count()) throw new ObjectNotFound("Предмета с Id=" + itemId + " не существует");
        List<CommentDtoOut> comments = getComments(itemId);
        BookingDtoShort last = bookingRepository.findFirstByItemIdAndStartLessThanEqualAndStatus(itemId, LocalDateTime.now(),
                        Status.APPROVED, Sort.by(DESC, "end"))
                .map(BookingMapper::toBookingDtoShort).orElse(null);

        BookingDtoShort next = bookingRepository
                .findFirstByItemIdAndStartAfterAndStatus(itemId, LocalDateTime.now(),
                        Status.APPROVED, Sort.by(ASC, "end"))
                .map(BookingMapper::toBookingDtoShort).orElse(null);

        return ItemMapper.toItemDtoOut(itemRepository.findById(itemId).get(), comments, last, next);
    }

    @Transactional
    @Override
    public List<Item> getItemsByUser(long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Transactional
    @Override
    public List<Item> getItemsByText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(text);
    }

    @Transactional
    @Override
    public CommentDtoOut addComment(CommentDto commentDto, long userId, long itemId) {
        Item itemById = itemRepository.findById(itemId).get();
        User userById = userRepository.findById(userId).get();
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())) {
            throw new FailBooking("Пользователь не пользовался вещью");
        }
        Comment saveComment = commentRepository.save(new Comment(1L, commentDto.getText(), itemById, userById, LocalDateTime.now()));
        return CommentMapper.toCommentDtoOut(saveComment);
    }

    @Transactional
    @Override
    public List<CommentDtoOut> getAllCommentByItemId(long userId, long itemId) {
        return commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(Collectors.toList());
    }


    private void checkUserAndItemDto(Long userId, Optional<User> master, ItemDto item) {
        if (master.isEmpty()) {
            throw new ObjectNotFound("Пользователя с Id=" + userId + " не существует");
        }
        if (item.getDescription() == null) {
            throw new DescriptionException("Описание не может быть пустым");
        }
        if (item.getName().isBlank()) {
            throw new NameException("Имя не может быть пустым");
        }
    }


    private void editItem(Item editItem, ItemDto itemDto) {
        if (itemDto.getAvailable() != null) {
            editItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            editItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            editItem.setDescription(itemDto.getDescription());
        }
    }

    private List<CommentDtoOut> getComments(long itemId) {
        return commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(Collectors.toList());
    }
}
