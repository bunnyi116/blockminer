package com.github.bunnyi116.blockminer.config;

import com.github.bunnyi116.blockminer.BlockMinerMod;
import com.github.bunnyi116.blockminer.Debug;
import com.github.bunnyi116.blockminer.util.BlockUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.Blocks;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final File PATH_ROOT = new File(FabricLoader.getInstance().getConfigDir().toFile(), BlockMinerMod.MOD_ID);
    public static final File PATH_CONFIG = new File(PATH_ROOT, "config.json");
    public static final Config INSTANCE = Config.load();

    public List<String> blockWhitelist = getDefaultBlockWhitelist();
    public List<String> blockBlacklist = new ArrayList<>();
    public transient List<String> blockBlacklistServer = getDefaultBlockBlacklistServer();

    public static List<String> getDefaultBlockWhitelist() {
        var list = new ArrayList<String>();
        list.add(BlockUtils.getId(Blocks.BEDROCK));                  // 基岩
        return list;
    }

    public static List<String> getDefaultBlockBlacklistServer() {
        // 默认方块黑名单 (用于限制的服务器, 与自定义黑名单分离)
        var list = new ArrayList<String>();
        list.add(BlockUtils.getId(Blocks.BARRIER));                    // 屏障
        list.add(BlockUtils.getId(Blocks.COMMAND_BLOCK));              // 普通命令方块
        list.add(BlockUtils.getId(Blocks.CHAIN_COMMAND_BLOCK));        // 连锁型命令方块
        list.add(BlockUtils.getId(Blocks.REPEATING_COMMAND_BLOCK));    // 循环型命令方块
        list.add(BlockUtils.getId(Blocks.STRUCTURE_VOID));             // 结构空位
        list.add(BlockUtils.getId(Blocks.STRUCTURE_BLOCK));            // 结构方块
        list.add(BlockUtils.getId(Blocks.JIGSAW));                     // 拼图方块
        return list;
    }

    public static Config load() {
        Config config = null;
        Gson gson = new Gson();
        if (!PATH_ROOT.exists()) {
            PATH_ROOT.mkdirs();
        }
        try (Reader reader = new FileReader(PATH_CONFIG)) {
            config = gson.fromJson(reader, Config.class);
            Debug.alwaysWrite("已成功加载配置文件");
        } catch (Exception e) {
            if (PATH_CONFIG.exists()) {
                if (PATH_CONFIG.delete()) {
                    Debug.alwaysWrite("无法加载配置,已成功删除配置文件");
                } else {
                    Debug.alwaysWrite("无法加载配置,删除配置文件失败");
                }
            } else {
                Debug.alwaysWrite("找不到配置文件");
            }
        }
        if (config == null) {
            Debug.alwaysWrite("使用默认配置");
            config = new Config();
            save();
        }
        return config;
    }

    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!PATH_ROOT.exists()) {
            PATH_ROOT.mkdirs();
        }
        try (FileWriter writer = new FileWriter(PATH_CONFIG)) {
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            Debug.alwaysWrite("无法保存配置文件");
            e.printStackTrace();
        }
    }
}
