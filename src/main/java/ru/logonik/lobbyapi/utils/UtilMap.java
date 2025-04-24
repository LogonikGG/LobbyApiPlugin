package ru.logonik.lobbyapi.utils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UtilMap {


    /**
     * Updates the value associated with the key that is equal to the provided key,
     * without replacing the key object itself (preserves key identity).
     *
     * @param map the map to modify
     * @param key the key to search for (compared via equals)
     * @param value the new value to set
     * @return the old value associated with the found key, or {@code null} if not found
     */
    public static <K, V> V putValueWithoutSetKey(Map<K, V> map, K key, V value) {
        Map.Entry<K, V> foundEntry = getEntryByKey(map, key);
        if(foundEntry == null) return null;
        V oldValue = foundEntry.getValue();
        map.put(foundEntry.getKey(), value);
        return oldValue;
    }

    /**
     * Replaces the key in the map with a new equivalent key (equal by {@code equals()} but not necessarily {@code ==}),
     * while preserving the associated value.
     *
     * <p>This is useful when the key object has changed its internal state (but {@code equals()} still same) and needs to be refreshed.</>
     *
     * @param map the map to update
     * @param newEquivalentKey the new key equal to the old one
     * @return {@code true} if the key existed and was replaced, {@code false} otherwise
     */
    public static <K, V> boolean replaceEquivalentKey(Map<K, V> map, K newEquivalentKey) {
        if (map.containsKey(newEquivalentKey)) {
            V value = map.remove(newEquivalentKey);
            map.put(newEquivalentKey, value);
            return true;
        }
        return false;
    }

    /**
     * Finds and returns the map entry with a key equal to the provided key.
     * This method can be useful when direct access to the {@code Map.Entry} is required,
     * for example to inspect both key and value or modify the entry in-place.
     *
     * @param map the map to search
     * @param key the key to find (compared via {@code equals()})
     * @return the found map entry, or {@code null} if no such entry exists
     */
    public static <K, V> Map.Entry<K, V> getEntryByKey(Map<K, V> map, K key) {
        Set<Map.Entry<K, V>> entries = map.entrySet();
        for (Map.Entry<K, V> entry : entries) {
            if(Objects.equals(entry.getKey(), key)) {
                return entry;
            }
        }
        return null;
    }
}
