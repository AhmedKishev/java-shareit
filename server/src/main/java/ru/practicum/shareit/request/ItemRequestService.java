package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;


import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAllItemRequests(Integer from, Integer size, Long userId);

    ItemRequest save(ItemRequestDto itemRequestDto, Long requesterId);

    ItemRequestDto getItemRequestById(long requestId, Long userId);

    List<ItemRequestDto> getAllItemRequests(Long userId);
}
