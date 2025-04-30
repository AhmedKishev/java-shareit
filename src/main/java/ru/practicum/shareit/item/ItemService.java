package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemService {
    FakeItemRepository itemRepository;

    public Item save(Long userId, @Valid ItemDto item) {
        return itemRepository.save(userId, item);
    }

    public Item edit(long itemId, ItemDto editItem, Long userId) {
        return itemRepository.edit(itemId, editItem, userId);
    }

    public Item getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    public List<Item> getItemsByUser(long userId) {
        return itemRepository.getItemsByUserId(userId);
    }

    public List<Item> getItemsByText(String text) {
        return itemRepository.getItemsByText(text);
    }
}
