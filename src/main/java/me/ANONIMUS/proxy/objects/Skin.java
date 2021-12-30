package me.ANONIMUS.proxy.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;

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