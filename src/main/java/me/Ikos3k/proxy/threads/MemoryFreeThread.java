package me.Ikos3k.proxy.threads;

import java.util.TimerTask;

public class MemoryFreeThread extends TimerTask {
    public void run() {
        System.gc();
    }
}