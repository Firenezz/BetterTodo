package chestlib.api.database;

import static chestlib.api.database.SimpleDatabase.CACHE_MAX_SIZE;
import static chestlib.api.database.SimpleDatabase.SPARSE_RATIO;

import java.util.function.Function;
import java.util.function.Predicate;

enum LookupLogicType {

    Empty(db -> db.mapDB.isEmpty(), EmptyLookupLogic::new),
    ArrayCache(db -> db.mapDB.size() < CACHE_MAX_SIZE
        && db.mapDB.size() > SPARSE_RATIO * (db.mapDB.lastKey() - db.mapDB.firstKey()), ArrayCacheLookupLogic::new),
    Naive(db -> true, NaiveLookupLogic::new);

    private final Predicate<SimpleDatabase<?>> shouldUse;
    private final Function<SimpleDatabase<?>, LookupLogic<?>> factory;

    LookupLogicType(Predicate<SimpleDatabase<?>> shouldUse, Function<SimpleDatabase<?>, LookupLogic<?>> factory) {
        this.shouldUse = shouldUse;
        this.factory = factory;
    }

    static LookupLogicType determine(SimpleDatabase<?> db) {
        for (LookupLogicType type : values()) {
            if (type.shouldUse.test(db)) return type;
        }
        return Naive;
    }

    @SuppressWarnings("unchecked")
    <T> LookupLogic<T> get(SimpleDatabase<T> db) {
        return (LookupLogic<T>) factory.apply(db);
    }
}
