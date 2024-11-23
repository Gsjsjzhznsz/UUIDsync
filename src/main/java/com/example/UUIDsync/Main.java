package com.example.UUIDsync;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, UUID> uuidMap = new HashMap<>();
    private File uuidMapFile;

    @Override
    public void onEnable() {
        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);

        // 创建插件文件夹
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // 初始化 UUID 映射文件
        uuidMapFile = new File(getDataFolder(), "uuidmap.txt");
        loadUuidMap();

        getLogger().info("UUIDModifier 插件已启用！");
    }

    @Override
    public void onDisable() {
        saveUuidMap();
        getLogger().info("UUIDModifier 插件已禁用！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cop")) {
            if (args.length != 2) {
                sender.sendMessage("使用方法: /cop <UUID1> <UUID2>");
                return true;
            }

            try {
                UUID uuid1 = UUID.fromString(args[0]);
                UUID uuid2 = UUID.fromString(args[1]);

                uuidMap.put(uuid1, uuid2);
                uuidMap.put(uuid2, uuid1); // 双向映射

                saveUuidMap(); // 立即保存映射到文件
                sender.sendMessage("成功建立 UUID 映射: " + uuid1 + " <-> " + uuid2);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("无效的 UUID 格式，请检查后再试。");
            }
            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        // 检查该玩家是否在 UUID 映射中
        if (!uuidMap.containsKey(playerUuid)) {
            return;
        }

        UUID targetUuid = uuidMap.get(playerUuid);
        syncPlayerData(playerUuid, targetUuid);
    }

    private void syncPlayerData(UUID sourceUuid, UUID targetUuid) {
        File playerDataFolder = new File(getServer().getWorlds().get(0).getWorldFolder(), "playerdata");
        File sourceFile = new File(playerDataFolder, sourceUuid + ".dat");
        File targetFile = new File(playerDataFolder, targetUuid + ".dat");

        if (sourceFile.exists()) {
            try {
                // 确保父目录存在
                File parentDir = targetFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                // 复制数据文件到目标 UUID
                Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                getLogger().info("玩家数据已从 " + sourceUuid + " 复制到 " + targetUuid);
            } catch (IOException e) {
                getLogger().warning("复制玩家数据时出错: " + e.getMessage());
            }
        } else {
            getLogger().warning("源玩家数据文件不存在: " + sourceFile.getName());
        }
    }

    private void loadUuidMap() {
        if (!uuidMapFile.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(uuidMapFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    UUID uuid1 = UUID.fromString(parts[0]);
                    UUID uuid2 = UUID.fromString(parts[1]);
                    uuidMap.put(uuid1, uuid2);
                    uuidMap.put(uuid2, uuid1); // 双向映射
                }
            }
            getLogger().info("已加载 UUID 映射数据。");
        } catch (IOException e) {
            getLogger().warning("加载 UUID 映射时出错: " + e.getMessage());
        }
    }

    private void saveUuidMap() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(uuidMapFile))) {
            Set<UUID> processed = new HashSet<>();
            for (Map.Entry<UUID, UUID> entry : uuidMap.entrySet()) {
                UUID uuid1 = entry.getKey();
                UUID uuid2 = entry.getValue();
                if (!processed.contains(uuid1) && !processed.contains(uuid2)) {
                    writer.write(uuid1 + "=" + uuid2);
                    writer.newLine();
                    processed.add(uuid1);
                    processed.add(uuid2);
                }
            }
            getLogger().info("UUID 映射数据已保存。");
        } catch (IOException e) {
            getLogger().warning("保存 UUID 映射时出错: " + e.getMessage());
        }
    }
}
