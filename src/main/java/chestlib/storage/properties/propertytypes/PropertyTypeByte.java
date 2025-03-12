package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ResourceLocation;

import chestlib.util.nbt.NBTReaderUtil;

public class PropertyTypeByte extends PropertyTypeBase<Byte> {

    public PropertyTypeByte(ResourceLocation key, Byte defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public Byte readValue(NBTBase nbt) {
        return NBTReaderUtil.readByteValue(nbt)
            .orElse(this.getDefault());
    }

    @Override
    public NBTBase writeValue(Byte value) {
        if (value == null) {
            return new NBTTagByte(this.getDefault());
        }

        return new NBTTagByte(value);
    }
}
