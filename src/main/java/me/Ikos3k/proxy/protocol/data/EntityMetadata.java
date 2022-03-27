package me.Ikos3k.proxy.protocol.data;

import lombok.Data;

@Data
public class EntityMetadata {
    private final int id;
    private final MetadataType type;
    private final Object value;
}
