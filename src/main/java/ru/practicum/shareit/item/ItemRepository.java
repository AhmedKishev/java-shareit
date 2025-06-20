package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE (i.name ILIKE CONCAT('%', ?1, '%') " +
            "OR i.description ILIKE CONCAT('%', ?1, '%')) " +
            "AND i.available = true")
    List<Item> search(String text);
}
