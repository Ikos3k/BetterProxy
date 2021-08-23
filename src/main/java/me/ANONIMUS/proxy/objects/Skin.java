package me.ANONIMUS.proxy.objects;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;

@Data
public class Skin {
    private final GameProfile gameProfile;

    public String getValue() {
        GameProfile.Property property = gameProfile.getProperty("textures");
        return property.getValue();
    }

    public String getSignature() {
        GameProfile.Property property = gameProfile.getProperty("textures");
        return property.getSignature();
    }
}