package ru.practicum.shareit.request;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.FailBooking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {


    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;
    ItemService itemService;

    @Override
    public List<ItemRequestDto> getAllItemRequests(Integer from, Integer size, Long userId) {
        return null;
    }

    @Transactional
    @Override
    public ItemRequest save(ItemRequestDto itemRequestDto, Long requesterId) {
        validate(itemRequestDto);
        User requester = userRepository.getReferenceById(requesterId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(requester);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public ItemRequestDto getItemRequestById(long requestId, Long userId) {
        List<ItemDto> items = itemService.getItemsByUser(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).get();
        return ItemRequestMapper.toItemRequestDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findItemRequestByRequester_Id(userId);
        return itemRequests
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    private void validate(ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isBlank()) {
            throw new FailBooking("Request cannot be null or blank");
        }
    }
}
