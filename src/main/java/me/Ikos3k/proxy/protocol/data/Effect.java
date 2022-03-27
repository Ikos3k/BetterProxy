package me.Ikos3k.proxy.protocol.data;

import lombok.Data;

@Data
public class Effect {
    private final int effectReason;
    private final float value;
}