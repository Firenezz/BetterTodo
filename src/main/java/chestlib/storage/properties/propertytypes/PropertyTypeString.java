package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

public class PropertyTypeString extends PropertyTypeBase<String> {

    public PropertyTypeString(ResourceLocation key, String defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public String readValue(NBTBase nbt) {
        if (!(nbt instanceof NBTTagString) || nbt.getId() != 8) {
            return this.getDefault();
        }

        try {
            return ((NBTTagString) nbt).func_150285_a_();
        } catch (Exception e) {
            return this.getDefault();
        }
    }

    @Override
    public NBTBase writeValue(String value) {
        if (value == null) {
            return new NBTTagString(this.getDefault());
        }

        return new NBTTagString(value);
    }
}
