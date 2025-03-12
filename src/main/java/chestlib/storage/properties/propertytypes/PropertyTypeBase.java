package chestlib.storage.properties.propertytypes;

import net.minecraft.util.ResourceLocation;

import chestlib.api.properties.IPropertyType;

public abstract class PropertyTypeBase<T> implements IPropertyType<T> {

    public static final byte BYTE_TAG = 1;
    public static final byte SHORT_TAG = 2;
    public static final byte INT_TAG = 3;
    public static final byte LONG_TAG = 4;
    public static final byte FLOAT_TAG = 5;
    public static final byte DOUBLE_TAG = 6;
    public static final byte BYTEARRAY_TAG = 7;
    public static final byte STRING_TAG = 8;
    public static final byte LIST_TAG = 9;
    public static final byte COMPOUND_TAG = 10;
    public static final byte INTARRAY_TAG = 11;

    private final ResourceLocation key;
    private final T defaultValue;

    public PropertyTypeBase(ResourceLocation key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public ResourceLocation getKey() {
        return key;
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }
}
