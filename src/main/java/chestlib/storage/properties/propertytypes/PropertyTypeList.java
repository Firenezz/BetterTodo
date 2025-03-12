package chestlib.storage.properties.propertytypes;

import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;

/// Not ready yet
public class PropertyTypeList<TValue> extends PropertyTypeBase<List<TValue>> {

    private final Class<TValue> valueType;

    @SuppressWarnings("unchecked")
    public PropertyTypeList(ResourceLocation key, List<TValue> defaultValue) {
        super(key, defaultValue);
        assert !defaultValue
            .isEmpty() : "The default list must be non-empty to get the class. Use the other constructor to manually set the class";

        this.valueType = (Class<TValue>) defaultValue.get(0)
            .getClass();
    }

    public PropertyTypeList(ResourceLocation key, List<TValue> defaultValue, Class<TValue> valueType) {
        super(key, defaultValue);
        this.valueType = valueType;
    }

    @Override
    public List<TValue> readValue(NBTBase nbt) {
        return null;
    }

    @Override
    public NBTBase writeValue(List<TValue> value) {
        return null;
    }
}
