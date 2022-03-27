package me.Ikos3k.proxy.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.Ikos3k.proxy.protocol.objects.GameProfile;

@Getter
@RequiredArgsConstructor
public class Skin {
    private final GameProfile gameProfile;

    public String getValue() {
        return gameProfile.getProperty("textures").getValue();
    }

    public String getSignature() {
        return gameProfile.getProperty("textures").getSignature();
    }
}