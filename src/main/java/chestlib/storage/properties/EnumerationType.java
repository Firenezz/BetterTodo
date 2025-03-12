package chestlib.storage.properties;

public enum EnumerationType {
    /// Will use the name of the enum to serialize the data
    String,
    /// will use an index to serialize the enum
    ///
    /// If the enum is changed it will make the old serializations invalid
    Ordinal
}
