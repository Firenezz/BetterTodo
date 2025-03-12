package chestlib.util.nbt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

import chestlib.api.api.ChestAPI;

public class NBTTagUtil {

    private static Field tagList;

    public static List<NBTBase> readListOfNBT(final NBTTagList nbtList) {
        if (tagList == null) {
            try {
                tagList = nbtList.getClass()
                    .getDeclaredField("tagList");
            } catch (final Throwable t) {
                try {
                    tagList = nbtList.getClass()
                        .getDeclaredField("field_74747_a");
                } catch (final Throwable z) {
                    ChestAPI.getLogger()
                        .debug(t);
                    ChestAPI.getLogger()
                        .debug(z);
                }
            }
        }

        try {
            tagList.setAccessible(true);
            return (List<NBTBase>) tagList.get(nbtList);
        } catch (final Throwable t) {
            ChestAPI.getLogger()
                .debug(t);
        }

        return new ArrayList<>();
    }
}
