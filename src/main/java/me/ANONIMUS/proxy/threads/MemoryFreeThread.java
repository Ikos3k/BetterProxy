package me.ANONIMUS.proxy.threads;

import java.util.TimerTask;

public class MemoryFreeThread extends TimerTask {
    public void run() { System.gc(); }
}