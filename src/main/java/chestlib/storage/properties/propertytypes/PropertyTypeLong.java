package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ResourceLocation;

public class PropertyTypeLong extends PropertyTypeBase<Long> {

    public PropertyTypeLong(ResourceLocation key, Long defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public Long readValue(NBTBase nbt) {
        if (!(nbt instanceof NBTBase.NBTPrimitive) || nbt.getId() != 4) {
            return this.getDefault();
        }

        return ((NBTBase.NBTPrimitive) nbt).func_150291_c();
    }

    @Override
    public NBTBase writeValue(Long value) {
        if (value == null) {
            return new NBTTagLong(this.getDefault());
        }

        return new NBTTagLong(value);
    }
}
