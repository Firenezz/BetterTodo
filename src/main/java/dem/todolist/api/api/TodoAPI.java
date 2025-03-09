package dem.todolist.api.api;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import betterquesting.api.storage.INameCache;

/**
 * Storage for all the questing API hooks. Includes built-in and custom APIs
 */
public class TodoAPI {

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

    public static <T> T getAPI(ApiKey<T> key) {
        Object obj = apis.get(key);
        return obj == null ? null : (T) obj;
    }

    /**
     * This should be used over the vanilla method of obtaining the player's UUID.
     * This is because offline servers do not sync UUIDs properly to the client
     */
    public static UUID getQuestingUUID(EntityPlayer player) {
        if (player == null) {
            return null;
        }

        if (player.worldObj.isRemote) {
            // TODO: Decouple the reference to BQ
            UUID uuid = betterquesting.api.api.QuestingAPI.getAPI(betterquesting.api.api.ApiReference.NAME_CACHE)
                .getUUID(
                    player.getGameProfile()
                        .getName());

            if (uuid != null) {
                return uuid;
            }
        }

        return player.getGameProfile()
            .getId();
    }

    private static Logger logger = null;

    public static Logger getLogger() {
        if (logger == null) {
            logger = LogManager.getLogger("todolist");
        }

        return logger;
    }

    public static EntityPlayerMP getPlayer(UUID uuid) {
        Optional<EntityPlayerMP> onlinePlayer = MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList.stream()
                .filter(Objects::nonNull)
                .filter(o -> getQuestingUUID((EntityPlayer) o).equals(uuid))
                .findFirst();
        return (EntityPlayerMP) onlinePlayer.orElse(null);
    }
}
