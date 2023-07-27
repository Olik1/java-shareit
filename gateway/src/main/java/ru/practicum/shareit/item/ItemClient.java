package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemItemRequestDto;

import javax.validation.ValidationException;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(long userId, ItemItemRequestDto itemDto) {
        validateItemDto(itemDto, false);
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(long userId, ItemItemRequestDto itemDto) {
        validateItemDto(itemDto, true);
        return patch("/" + userId, itemDto);
    }

    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsByUserId(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchText(String text, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );
        return get("/search?text={text}&from={from}&size={size}", parameters);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentDto commentDto) {
        return post(String.format("/%s/comment", itemId), userId, commentDto);
    }

    private void validateItemDto(ItemItemRequestDto itemDto, boolean isUpdate) {
        if (isUpdate && (itemDto.getName() != null && itemDto.getName().isBlank()) ||
                (!isUpdate && (itemDto.getName() == null || itemDto.getName().isBlank()))) {
            throw new ValidationException("Не указано поле Name");
        }

        if (isUpdate && (itemDto.getDescription() != null && itemDto.getDescription().isBlank()) ||
                !isUpdate && (itemDto.getDescription() == null || itemDto.getDescription().isBlank())) {
            throw new ValidationException("Не указано поле Description");
        }

        if (!isUpdate && itemDto.getAvailable() == null) {
            throw new ValidationException("Отсуствует поле Available");
        }

    }

}
