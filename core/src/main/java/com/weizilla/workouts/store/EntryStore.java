package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Entry;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface EntryStore<K, V extends Entry<K>> {
    void add(V value);

    void addAll(Collection<V> values);

    V get(K id);

    List<V> get(LocalDate date);

    List<V> getAll();

    void update(V value);

    void delete(K id);
}
