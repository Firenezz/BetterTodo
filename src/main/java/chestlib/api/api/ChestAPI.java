package chestlib.api.api;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Storage for all the questing API hooks. Includes built-in and custom APIs
 */
public class ChestAPI {

    private static final HashMap<ApiKey<?>, Object> apis = new HashMap<>();

    public static <T> void registerAPI(ApiKey<T> key, T api) {
        if (key == null) {
            throw new NullPointerException("API key can not be NULL");
        } else if (api == null) {
            throw new NullPointerException("Tried to registed NULL API");
        } else if (apis.containsKey(key)) {
            throw new IllegalArgumentException("Key cannot be registered twice");
        }

        apis.put(key, api);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAPI(ApiKey<T> key) {
        Object obj = apis.get(key);
        return obj == null ? null : (T) obj;
    }

    private static Logger logger = null;

    public static Logger getLogger() {
        if (logger == null) {
            logger = LogManager.getLogger("chestlib");
        }

        return logger;
    }
}
