package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestController {
    private static final String HEADER_SHARER_USER_ID = "X-Sharer-User-Id";
    ItemRequestService itemRequestService;

    @GetMapping()
    public List<ItemRequestDto> getItemRequests(@RequestHeader(value = HEADER_SHARER_USER_ID, required = false) Long userId) {
        return itemRequestService.getAllItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(value = HEADER_SHARER_USER_ID, required = false) Long userId,
                                                   @RequestParam(required = false) Integer from,
                                                   @RequestParam(required = false) Integer size) {
        return itemRequestService.getAllItemRequests(from, size, userId);
    }

    @PostMapping()
    public ItemRequest createItemRequest(@RequestHeader(value = HEADER_SHARER_USER_ID, required = false) Long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.save(itemRequestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader(value = HEADER_SHARER_USER_ID, required = false) Long userId,
                                         @PathVariable long requestId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }
}