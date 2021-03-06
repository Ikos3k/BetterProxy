package me.ANONIMUS.proxy.commands.admins;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class CommandStatus extends Command {
    public CommandStatus() {
        super("status", null, null, "", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        long inUseMemory = totalMemory - freeMemory;
        int inUse = (int) (inUseMemory * 100L / maxMemory);
        double cpu = 0.0;
        try {
            cpu = getProcessCpuLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChatUtil.sendChatMessage("", sender, false);
        ChatUtil.sendChatMessage("----------------------------------", sender, false);
        ChatUtil.sendChatMessage("Available memory: &e" + humanReadableByteCount(totalMemory), sender, true);
        ChatUtil.sendChatMessage("Assigned memory: &a" + humanReadableByteCount(maxMemory), sender, true);
        ChatUtil.sendChatMessage("Used Memory: &4" + humanReadableByteCount(inUseMemory) + " &8(" + "&f" + inUse + "%&8)", sender, true);
        ChatUtil.sendChatMessage("Used CPU: &6" + cpu + "%", sender, true);
        ChatUtil.sendChatMessage("----------------------------------", sender, false);
    }

    private double getProcessCpuLoad() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});
        if (list.isEmpty()) {
            return Double.NaN;
        }
        Attribute att = (Attribute) list.get(0);
        Double value = (Double) att.getValue();
        if (value != -1.0) {
            return (double) ((int) (value * 1000.0)) / 10.0;
        }
        return Double.NaN;
    }

    private String humanReadableByteCount(long bytes) {
        if (bytes < 1536L) {
            return bytes + " B";
        }
        int exp = (int) (Math.log((double) bytes) / Math.log(1536.0));
        String pre = String.valueOf("KMGTPE".charAt(exp - 1));
        return String.format("%.2f %sB", (double) bytes / Math.pow(1024.0, exp), pre);
    }
}