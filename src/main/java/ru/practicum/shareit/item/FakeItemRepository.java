package ru.practicum.shareit.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DescriptionException;
import ru.practicum.shareit.exception.NameException;
import ru.practicum.shareit.exception.ObjectNotFound;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.FakeUserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FakeItemRepository {
    Multimap<Long, Long> userToItems = ArrayListMultimap.create();
    Map<Long, Item> items = new HashMap<>();
    FakeUserRepository fakeUserRepository;

    private long getIndex() {
        return items.size() + 1;
    }

    public Item save(Long userId, ItemDto item) {
        Optional<User> master = fakeUserRepository.getUserById(userId);
        if (master.isEmpty()) {
            throw new ObjectNotFound("Пользователя с Id=" + userId + " не существует");
        }
        if (item.getDescription() == null) {
            throw new DescriptionException("Описание не может быть пустым");
        }
        if (item.getName().isBlank()) {
            throw new NameException("Имя не может быть пустым");
        }
        Item saveItem = ItemMapper.toItem(item);
        saveItem.setId(getIndex());
        saveItem.setOwner(master.get());
        userToItems.put(userId, saveItem.getId());
        items.put(saveItem.getId(), saveItem);
        return saveItem;
    }


    private List<Long> getItemsByUser(long userId) {
        return userToItems.entries().stream()
                .filter(entry -> entry.getKey() == userId)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void check(long userId, long itemId) {
        List<Long> itemByUser = getItemsByUser(userId);
        if (!itemByUser.contains(itemId)) {
            throw new ObjectNotFound("У пользователя с Id=" + userId + " нету предметов с itemId=" + itemId);
        }
    }

    public Item edit(long itemId, ItemDto editItem, Long userId) {
        check(userId, itemId);
        Item findItem = items.get(itemId);
        if (editItem.getName() != null) {
            findItem.setName(editItem.getName());
        }
        if (editItem.getAvailable() != null) {
            findItem.setAvailable(editItem.getAvailable());
        }
        if (editItem.getDescription() != null) {
            findItem.setDescription(editItem.getDescription());
        }
        return findItem;
    }

    public Item getItemById(long itemId) {
        if (itemId > items.size()) throw new ObjectNotFound("Предмета с Id=" + itemId + " не существует");
        return items.get(itemId);
    }

    public List<Item> getItemsByUserId(long userId) {
        List<Long> itemsIdByUser = getItemsByUser(userId);
        return items.values().stream()
                .filter(item -> itemsIdByUser.contains(item.getId()))
                .collect(Collectors.toList());
    }

    public List<Item> getItemsByText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> (item.getName().toUpperCase().contains(text)
                        || item.getDescription().toUpperCase().contains(text)) && item.isAvailable())
                .collect(Collectors.toList());
    }
}
