package chestlib.util.nbt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import org.apache.logging.log4j.Level;

import chestlib.api.api.ChestAPI;

public class NBTUuidUtil {

    static private final String idFieldName = "id";
    static private final String idFieldNameHigh = idFieldName + "H";
    static private final String idFieldNameLow = idFieldName + "L";

    public static NBTTagCompound writeIdToNbt(UUID uuid) {
        NBTTagCompound tag = new NBTTagCompound();
        writeIdToNbt(uuid, tag);
        return tag;
    }

    public static void writeIdToNbt(UUID uuid, NBTTagCompound tag) {
        writeIdToNbt("", uuid, tag);
    }

    public static void writeIdToNbt(String prefix, UUID uuid, NBTTagCompound tag) {
        tag.setLong(prefix + idFieldNameHigh, uuid.getMostSignificantBits());
        tag.setLong(prefix + idFieldNameLow, uuid.getLeastSignificantBits());
    }

    public static NBTTagList writeIds(Collection<UUID> uuids) {
        NBTTagList tagList = new NBTTagList();
        uuids.forEach(uuid -> tagList.appendTag(writeIdToNbt(uuid)));
        return tagList;
    }

    public static Optional<UUID> tryReadFromNbt(String prefix, NBTTagCompound tag) {
        if (tag.hasKey(prefix + idFieldNameHigh, 99) && tag.hasKey(prefix + idFieldNameLow, 99)) {
            return Optional.of(readIdFromNbt(prefix, tag));
        }

        return Optional.empty();
    }

    public static UUID readIdFromNbt(NBTTagCompound tag) {
        return readIdFromNbt("", tag);
    }

    public static UUID readIdFromNbt(String prefix, NBTTagCompound tag) {
        var high = tag.getLong(prefix + idFieldNameHigh);
        var low = tag.getLong(prefix + idFieldNameLow);
        return new UUID(high, low);
    }

    public static List<UUID> readIds(NBTTagCompound tag, String key) {
        return readIds(tag.getTagList(key, Constants.NBT.TAG_COMPOUND));
    }

    public static List<UUID> readIds(NBTTagList tagList) {
        return getTagList(tagList).stream()
            .map(NBTTagCompound.class::cast)
            .map(NBTUuidUtil::readIdFromNbt)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private static Field f_tagList;

    @SuppressWarnings("unchecked")
    public static List<NBTBase> getTagList(NBTTagList tag) {
        try {
            return (ArrayList<NBTBase>) f_tagList.get(tag);
        } catch (IllegalAccessException e) {
            return Collections.emptyList();
        }
    }

    static {
        try {
            // noinspection JavaReflectionMemberAccess
            f_tagList = NBTTagList.class.getDeclaredField("field_74747_a");
            f_tagList.setAccessible(true);
        } catch (Exception e1) {
            try {
                f_tagList = NBTTagList.class.getDeclaredField("tagList");
                f_tagList.setAccessible(true);
            } catch (Exception e2) {
                ChestAPI.getLogger()
                    .log(Level.ERROR, "Unable to hook into NBTTagList!", e2);
            }
        }
    }
}
