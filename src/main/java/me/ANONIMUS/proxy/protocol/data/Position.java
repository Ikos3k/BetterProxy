package me.ANONIMUS.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    private double x, y, z;
}
