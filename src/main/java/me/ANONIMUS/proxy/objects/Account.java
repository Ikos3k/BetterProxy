package me.ANONIMUS.proxy.objects;

import lombok.Data;
import me.ANONIMUS.proxy.enums.GroupType;

@Data
public class Account {
    private final String username;
    private final String password;
    private final GroupType group;
}