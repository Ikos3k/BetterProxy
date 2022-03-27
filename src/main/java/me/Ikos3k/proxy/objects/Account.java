package me.Ikos3k.proxy.objects;

import lombok.Data;
import me.Ikos3k.proxy.enums.GroupType;

@Data
public class Account {
    private final String password;
    private final GroupType group;
}