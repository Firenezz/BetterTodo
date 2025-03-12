package chestlib.api.database;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.BiMap;

public interface IUuidDatabase<TValue> extends BiMap<UUID, TValue> {

    /** Returns an unused UUID. */
    UUID generateKey();

    @Nullable
    UUID lookupKey(TValue value);

    /**
     * Returns this database's entries in a stable order.
     * Useful for ensuring that exported files change as little as possible.
     */
    Stream<Entry<UUID, TValue>> orderedEntries();

    Stream<TValue> getAll(Collection<UUID> keys);

    Map<UUID, TValue> filterKeys(Collection<UUID> keys);

    BiMap<UUID, TValue> filterValues(Collection<TValue> values);

    BiMap<UUID, TValue> filterEntries(BiPredicate<UUID, TValue> filter);

    /**
     * Removes {@code value} from the database, and returns its corresponding key.
     * Returns null if {@code value} is not present in the database.
     */
    @Nullable
    UUID removeValue(TValue value);
}
