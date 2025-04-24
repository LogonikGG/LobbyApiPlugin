package ru.logonik.lobbyapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UtilBukkit {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");


    public static String colorize(String raw) {
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public static String removeColorCodes(String input) {
        return input == null ? null : ChatColor.stripColor(STRIP_COLOR_PATTERN.matcher(input).replaceAll(""));
    }

    public static Location getLocFromSection(ConfigurationSection section, World world) {
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        double yaw = section.getDouble("yaw");
        double pitch = section.getDouble("pitch");
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public static void setLocation(YamlConfiguration yaml, String path, Location location) {
        yaml.set(path + ".x", location.getX());
        yaml.set(path + ".y", location.getY());
        yaml.set(path + ".z", location.getZ());
        yaml.set(path + ".yaw", location.getYaw());
        yaml.set(path + ".pitch", location.getPitch());
    }

    public static List<Block> getBlocksAround(Block block) {
        ArrayList<Block> result = new ArrayList<>();
        for (int x = -1; x < 2; x = x + 1) {
            for (int y = -1; y < 2; y = y + 1) {
                for (int z = -1; z < 2; z = z + 1) {
                    result.add(block.getRelative(x, y, z));
                }
            }
        }
        return result;
    }

    public static List<Block> getBlocksAround(Block block, int radius) {
        List<Block> result = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    result.add(block.getRelative(x, y, z));
                }
            }
        }
        return result;
    }

    public static World getWorldOrThrow(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            throw new IllegalArgumentException("World with name '" + name + "' not found, or not loaded");
        }
        return world;
    }

    public static String getNames(Collection<? extends Player> gamers) {
        return gamers.stream().map(Player::getName).collect(Collectors.joining(", "));
    }
    public static List<String> getNamesList(Collection<? extends Player> gamers) {
        return gamers.stream().map(Player::getName).collect(Collectors.toList());
    }

    public static String getStrWithPass(String[] args, int pass) {
        return Arrays.stream(args).skip(pass).collect(Collectors.joining(" "));
    }


    public static Block findSolidBlockUnder(Block block) {
        do {
            block = block.getRelative(BlockFace.DOWN);
        } while (!block.getType().isSolid() && block.getY() >= 0);
        return block;
    }
}
