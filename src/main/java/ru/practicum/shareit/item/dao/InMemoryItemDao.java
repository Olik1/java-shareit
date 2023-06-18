package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository("InMemoryItemDao")
@Slf4j
public class InMemoryItemDao implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public Item addItem(Item item) {
        item.setId(increment());
        items.put(item.getId(), item);
        log.info("Добавлена новая вещь; {}", item.getName());
        return item;
    }

    @Override
    public Item updateUser(Item item) {
        items.put(item.getId(), item);
        log.info("Данные вещи обновлены: {}", item.getName());
        return item;
    }

    @Override
    public Item getItemByUserId(long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        List<Item> result = new ArrayList<>();
        for (Item value : items.values()) {
            if(value.getOwner().getId() == userId) {
                result.add(value);
            }
        }
        return result;
    }

    @Override
    public List<Item> searchText(String text) {
        List<Item> itemListSearch = new ArrayList<>();
        for (Item item : items.values()) {
            if (Objects.equals(item.isAvailable(), true) &&
                    (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                itemListSearch.add(item);
            }
        }
        return itemListSearch;
    }

    private Long increment() {
        return itemId++;
    }

}
