package me.ANONIMUS.proxy.managers;

import lombok.Getter;
import lombok.SneakyThrows;
import me.ANONIMUS.proxy.objects.Config;
import me.ANONIMUS.proxy.utils.json.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Getter
public class ConfigManager {
    private final File file;
    private Config config;

    public ConfigManager(File file) {
        this.config = new Config();
        this.file = file;

        try {
            read();
        } catch (Exception e) {
            write();
        }
    }

    public void write() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(JsonUtil.toJsonString(config, true));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void read() {
        this.config = JsonUtil.fromJson(new FileReader(file), new Config());
    }
}