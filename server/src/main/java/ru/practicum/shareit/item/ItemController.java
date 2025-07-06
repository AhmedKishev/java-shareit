package ru.practicum.shareit.item;


import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;


import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    ItemService itemService;
    final String sharerUserId = "X-Sharer-User-Id";

    @PostMapping
    public Item addItem(@RequestHeader(sharerUserId) Long userId,
                        @RequestBody ItemDto item) {
        return itemService.save(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@PathVariable("itemId") long itemId,
                         @RequestBody ItemDto editItem,
                         @RequestHeader(sharerUserId) Long userId) {
        Item item = itemService.edit(itemId, editItem, userId);
        return item;
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@PathVariable("itemId") long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getItemsByUser(@RequestHeader(sharerUserId) long userId) {
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemsByText(@PathParam("text") String text) {
        return itemService.getItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@RequestHeader(sharerUserId) long userId,
                                    @RequestBody CommentDto commentDto,
                                    @PathVariable("itemId") long itemId) {
        return itemService.addComment(commentDto, userId, itemId);
    }

}
