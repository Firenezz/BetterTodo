package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.ResourceLocation;

public class PropertyTypeShort extends PropertyTypeBase<Short> {

    public PropertyTypeShort(ResourceLocation key, Short defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public Short readValue(NBTBase nbt) {
        if (!(nbt instanceof NBTBase.NBTPrimitive) || nbt.getId() != 2) {
            return this.getDefault();
        }

        return ((NBTBase.NBTPrimitive) nbt).func_150289_e();
    }

    @Override
    public NBTBase writeValue(Short value) {
        if (value == null) {
            return new NBTTagShort(this.getDefault());
        }

        return new NBTTagShort(value);
    }
}
