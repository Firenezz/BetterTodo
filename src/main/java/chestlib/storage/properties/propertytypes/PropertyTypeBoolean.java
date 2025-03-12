package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ResourceLocation;

public class PropertyTypeBoolean extends PropertyTypeBase<Boolean> {

    public PropertyTypeBoolean(ResourceLocation key, boolean defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public Boolean readValue(NBTBase nbt) {
        if (nbt == null || nbt.getId() < 1 || nbt.getId() > 6) {
            return this.getDefault();
        }

        try {
            return ((NBTBase.NBTPrimitive) nbt).func_150290_f() > 0;
        } catch (Exception e) {
            return this.getDefault();
        }
    }

    @Override
    public NBTBase writeValue(Boolean value) {
        if (value == null) {
            return new NBTTagByte(this.getDefault() ? (byte) 1 : (byte) 0);
        }

        return new NBTTagByte(value ? (byte) 1 : (byte) 0);
    }
}
