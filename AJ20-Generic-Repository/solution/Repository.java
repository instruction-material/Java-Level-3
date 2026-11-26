import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

class Repository<T extends Identified> {
    private final Map<String, T> items = new LinkedHashMap<>();

    /**
     * @brief Add one item to the repository
     *
     * @param item Item to store
     */
    public void add(T item) {
        // Protect the repository from duplicate ids
        if (items.containsKey(item.id())) {
            throw new IllegalArgumentException("duplicate id: " + item.id());
        }

        items.put(item.id(), item);
    }

    /**
     * @brief Get one item by id
     *
     * @param id Item id to find
     *
     * @return Optional item for the provided id
     */
    public Optional<T> get(String id) {
        return Optional.ofNullable(items.get(id));
    }

    /**
     * @brief Check whether an id is stored
     *
     * @param id Item id to check
     *
     * @return True when the repository contains the id
     */
    public boolean contains(String id) {
        return items.containsKey(id);
    }

    /**
     * @brief Remove one item by id
     *
     * @param id Item id to remove
     *
     * @return Optional removed item
     */
    public Optional<T> remove(String id) {
        return Optional.ofNullable(items.remove(id));
    }

    /**
     * @brief Return all stored items
     *
     * @return Immutable list of stored items
     */
    public List<T> all() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    /**
     * @brief Find all items that match a predicate
     *
     * @param filter Predicate used to test each item
     *
     * @return Immutable list of matching items
     */
    public List<T> findAll(Predicate<T> filter) {
        List<T> matches = new ArrayList<>();

        // Test every stored item with the caller-provided predicate
        for (T item : items.values()) {
            // Keep only items that satisfy the predicate
            if (filter.test(item)) {
                matches.add(item);
            }
        }

        return Collections.unmodifiableList(matches);
    }

    /**
     * @brief Return all items sorted with a comparator
     *
     * @param comparator Comparator that defines the item order
     *
     * @return Immutable sorted list of items
     */
    public List<T> sorted(Comparator<T> comparator) {
        List<T> sortedItems = new ArrayList<>(items.values());
        sortedItems.sort(comparator);
        return Collections.unmodifiableList(sortedItems);
    }
}
