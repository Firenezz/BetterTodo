package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ResourceLocation;

public class PropertyTypeInteger extends PropertyTypeBase<Integer> {

    public PropertyTypeInteger(ResourceLocation key, Integer defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public Integer readValue(NBTBase nbt) {
        if (!(nbt instanceof NBTBase.NBTPrimitive) || nbt.getId() != 3) {
            return this.getDefault();
        }

        return ((NBTBase.NBTPrimitive) nbt).func_150287_d();
    }

    @Override
    public NBTBase writeValue(Integer value) {
        if (value == null) {
            return new NBTTagInt(this.getDefault());
        }

        return new NBTTagInt(value);
    }
}
