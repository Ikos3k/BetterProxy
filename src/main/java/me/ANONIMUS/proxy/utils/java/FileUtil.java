package me.ANONIMUS.proxy.utils.java;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.GroupType;
import me.ANONIMUS.proxy.objects.Account;

import java.io.File;
import java.util.Scanner;

public class FileUtil {
    public static void createMissing() {
        try {
            final File f1 = new File(BetterProxy.getInstance().getDirFolder() + "/accounts.txt");
            final File f2 = new File(BetterProxy.getInstance().getDirFolder() + "/world");
            if (!f1.exists()) { f1.createNewFile(); }
            if (!f2.exists()) { f2.mkdir(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAccounts() {
        try {
            final File f1 = new File(BetterProxy.getInstance().getDirFolder() + "/accounts.txt");
            final Scanner s = new Scanner(f1);
            while (s.hasNext()) {
                final String[] split = s.next().split(":", 3);
                final Account ac = new Account(split[0], split[1], GroupType.valueOf(split[2]));
                BetterProxy.getInstance().getAccounts().add(ac);
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
