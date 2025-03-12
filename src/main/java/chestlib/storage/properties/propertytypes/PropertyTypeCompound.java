package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import chestlib.api.storage.INBTSaveLoad;

public class PropertyTypeCompound<T extends INBTSaveLoad<NBTTagCompound>> extends PropertyTypeBase<T> {

    public PropertyTypeCompound(ResourceLocation key, T defaultValue) {
        super(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T readValue(NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound nbtTag) || nbt.getId() != PropertyTypeBase.COMPOUND_TAG) {
            return this.getDefault();
        }

        try {
            T value = (T) createInstance(
                this.getDefault()
                    .getClass());
            value.readFromNBT(nbtTag);
            return value;
        } catch (Exception e) {
            return this.getDefault();
        }
    }

    @Override
    public NBTBase writeValue(T value) {
        return null;
    }

    public static <T extends INBTSaveLoad<NBTTagCompound>> T createInstance(Class<T> clazz) {
        try {
            return clazz.cast(
                clazz.getDeclaredConstructor()
                    .newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
        }
    }
}
