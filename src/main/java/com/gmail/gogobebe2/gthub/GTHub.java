package com.gmail.gogobebe2.gthub;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

public class GTHub extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Starting up GTHub. If you need me to update this plugin, email at gogobebe2@gmail.com");
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling GTHub. If you need me to update this plugin, email at gogobebe2@gmail.com");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("permspawn")) {
            if (!sender.hasPermission("gtspawn.setspawn")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Error! You have to be a player to use this command!");
                return true;
            }
            Player player = (Player) sender;
            Location location = player.getLocation();

            String worldName = location.getWorld().getName();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            float yaw = location.getYaw();
            float pitch = location.getPitch();

            getConfig().set("spawn.world", worldName);
            getConfig().set("spawn.x", x);
            getConfig().set("spawn.y", y);
            getConfig().set("spawn.z", z);
            getConfig().set("spawn.yaw", yaw);
            getConfig().set("spawn.pitch", pitch);
            saveConfig();
            player.sendMessage(ChatColor.GREEN + "Spawn set!");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!getConfig().isSet("spawn.world")) {
            player.sendMessage(ChatColor.RED + "Error! No spawn set!");
            return;
        }
        World world = Bukkit.getWorld(getConfig().getString("spawn.world"));
        double x = getConfig().getDouble("spawn.x");
        double y = getConfig().getDouble("spawn.y");
        double z = getConfig().getDouble("spawn.z");
        float yaw = (float) getConfig().getDouble("spawn.yaw");
        float pitch = (float) getConfig().getDouble("spawn.pitch");
        Location spawn = new Location(world, x, y, z, yaw, pitch);
        player.getInventory().clear();
        player.teleport(spawn);
        player.setHealth(20);
        player.setFoodLevel(20);
        event.setJoinMessage("");
    }

    @EventHandler
    public void onHungerDrop(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
        if (event.getFoodLevel() == 20) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHealthDrop(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        event.setCancelled(true);
        player.setAllowFlight(false);
        player.setFlying(false);
        Location location = player.getLocation();
        player.setVelocity(location.getDirection().multiply(1.5).setY(1));
        new Trail(player).runTaskTimer(this, 0, 3);
        player.playSound(location, Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if ((player.getGameMode() != GameMode.CREATIVE)
                && (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR)
                && (!player.isFlying())) {
            player.setAllowFlight(true);

        }
    }
}
