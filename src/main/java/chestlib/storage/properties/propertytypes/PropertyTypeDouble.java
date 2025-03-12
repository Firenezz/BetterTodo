package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.ResourceLocation;

public class PropertyTypeDouble extends PropertyTypeBase<Double> {

    public PropertyTypeDouble(ResourceLocation key, Double defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public Double readValue(NBTBase nbt) {
        if (!(nbt instanceof NBTBase.NBTPrimitive) || nbt.getId() != 6) {
            return this.getDefault();
        }

        return ((NBTBase.NBTPrimitive) nbt).func_150286_g();
    }

    @Override
    public NBTBase writeValue(Double value) {
        if (value == null) {
            return new NBTTagDouble(this.getDefault());
        }

        return new NBTTagDouble(value);
    }
}
