package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Entry;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoryEntryStore<K, V extends Entry<K>> {
    private final Map<K, V> byId = new HashMap<>();

    public void add(V value) {
        byId.put(value.getId(), value);
    }

    public void addAll(Collection<V> values) {
        values.forEach(this::add);
    }

    public V get(K id) {
        return byId.get(id);
    }

    public List<V> get(LocalDate date) {
        return getAll().stream()
            .filter(v -> v.getDate().equals(date))
            .collect(Collectors.toList());
    }

    public List<V> getAll() {
        return byId.values().stream()
            .sorted()
            .collect(Collectors.toList());
    }

    public void update(V value) {
        byId.put(value.getId(), value);
    }

    public void delete(K id) {
        byId.remove(id);
    }
}
