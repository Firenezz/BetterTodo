package chestlib.api.database;

import java.util.List;

/// Taken from GTNH Better Questing
public interface IDatabase<T> {

    int nextID();

    DBEntry<T> add(int id, T value);

    boolean removeID(int key);

    boolean removeValue(T value);

    int getID(T value);

    T getValue(int id);

    int size();

    void reset();

    List<DBEntry<T>> getEntries();

    List<DBEntry<T>> bulkLookup(int... keys);
}
