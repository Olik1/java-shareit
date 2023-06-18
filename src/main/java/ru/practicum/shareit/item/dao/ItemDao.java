package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

    Item addItem(Item item);

    Item updateUser(Item item);

    Item getItemByUserId(long id);

    List<Item> getAllItems();

    List<Item> searchText(String text);

}
