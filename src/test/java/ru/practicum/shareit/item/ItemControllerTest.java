package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    private ItemDto itemDto;

    @BeforeEach
    @Test
    void setItemDto() {
        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    @SneakyThrows
    @Test
    void addItem() {
        long userId = 0;
        when(itemService.addItem(userId, itemDto)).thenReturn(itemDto);
        String contentAsString = mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDto), contentAsString);
        verify(itemService).addItem(userId, itemDto);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        long itemId = 0L;
        long userId = 0L;
        when(itemService.updateItem(userId, itemDto)).thenReturn(itemDto);
        String contentAsString = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDto), contentAsString);
        verify(itemService, times(1)).updateItem(userId, itemDto);
    }

    @SneakyThrows
    @Test
    void getItemById() {
        long itemId = 0L;
        long userId = 0L;
        when(itemService.getItem(itemId, userId)).thenReturn(itemDto);
        String contentAsString = mockMvc.perform(get("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), contentAsString);
        verify(itemService, atLeast(1)).getItem(itemId, userId);
    }

    @Test
    void getItemsByUserId() {
    }

    @SneakyThrows
    @Test
    void seachText() {
        long userId = 0L;
        String text = "text";
        List<ItemDto> itemDtos = List.of(ItemDto.builder()
                .name("name")
                .available(true)
                .build());

        when(itemService.searchText(text)).thenReturn(itemDtos);

        String contentAsString = mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtos), contentAsString);
        verify(itemService, times(1)).searchText(text);
    }

    @SneakyThrows
    @Test
    void addComment() {
        long userId = 1L;
        long itemId = itemDto.getId();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("authorName")
                .created(LocalDateTime.now())
                .build();
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);

        String contentAsString = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(commentDto), contentAsString);

        verify(itemService, times(1)).addComment(userId, itemId, commentDto);
        verifyNoMoreInteractions(itemService);
    }
}