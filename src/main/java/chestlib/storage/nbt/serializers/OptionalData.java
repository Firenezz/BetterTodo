package chestlib.storage.nbt.serializers;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.NotNull;

public class OptionalData {

    public static <T> Optional<T> readFromNBT(NBTBase nbt, Function<NBTBase, Optional<T>> factory) {
        if (!(nbt instanceof NBTTagCompound) && nbt.getId() != Constants.NBT.TAG_COMPOUND) {
            return Optional.empty();
        }

        var tag = (NBTTagCompound) nbt;

        try {
            if (tag.hasKey("option", Constants.NBT.TAG_COMPOUND)) return Optional.empty();
            else {
                return factory.apply(tag.getTag("option"));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> NBTBase writeValue(@NotNull Optional<T> value, Function<T, NBTBase> factory) {
        NBTTagCompound nbtOption = new NBTTagCompound();
        value.ifPresent(t -> nbtOption.setTag("option", factory.apply(t)));
        return nbtOption;
    }
}
