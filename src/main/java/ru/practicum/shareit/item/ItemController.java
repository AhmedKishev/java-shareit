package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                        @Valid @RequestBody ItemDto item) {
        return itemService.save(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@PathVariable("itemId") long itemId,
                         @Valid @RequestBody ItemDto editItem,
                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.edit(itemId, editItem, userId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable("itemId") long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemsByText(@PathParam("text") String text) {
        return itemService.getItemsByText(text);
    }


}
