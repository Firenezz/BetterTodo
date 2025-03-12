package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.ResourceLocation;

public class PropertyTypeFloat extends PropertyTypeBase<Float> {

    public PropertyTypeFloat(ResourceLocation key, Float defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public Float readValue(NBTBase nbt) {
        if (!(nbt instanceof NBTBase.NBTPrimitive) || nbt.getId() != 5) {
            return this.getDefault();
        }

        return ((NBTBase.NBTPrimitive) nbt).func_150288_h();
    }

    @Override
    public NBTBase writeValue(Float value) {
        if (value == null) {
            return new NBTTagFloat(this.getDefault());
        }

        return new NBTTagFloat(value);
    }
}
