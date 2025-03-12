package chestlib.util.nbt;

import java.util.Optional;

import net.minecraft.nbt.NBTBase;

import chestlib.storage.properties.propertytypes.PropertyTypeBase;

public class NBTReaderUtil {

    public static Optional<Byte> readByteValue(NBTBase nbt) {
        if (!(nbt instanceof NBTBase.NBTPrimitive) || nbt.getId() != PropertyTypeBase.BYTE_TAG) {
            return Optional.empty();
        }

        return Optional.of(((NBTBase.NBTPrimitive) nbt).func_150290_f());
    }

    public static Optional<Boolean> readBooleanValue(NBTBase nbt) {
        if (nbt == null || nbt.getId() < 1 || nbt.getId() > 6) {
            return Optional.empty();
        }

        try {
            return Optional.of(((NBTBase.NBTPrimitive) nbt).func_150290_f() > 0);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
