package com.gmail.gogobebe2.gthub.GTHub;

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
            Location location = ((Player) sender).getLocation();

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
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        World world = Bukkit.getWorld(getConfig().getString("spawn.world"));
        double x = getConfig().getDouble("spawn.x");
        double y = getConfig().getDouble("spawn.y");
        double z = getConfig().getDouble("spawn.z");
        float yaw = (float) getConfig().getDouble("spawn.yaw");
        float pitch = (float) getConfig().getDouble("spawn.pitch");
        Location spawn = new Location(world, x, y, z, yaw, pitch);
        Player player = event.getPlayer();
        player.getInventory().clear();
        player.teleport(spawn);
        event.setJoinMessage("");
    }

    @EventHandler
    public void onHungerDrop(FoodLevelChangeEvent event) {
        event.setCancelled(true);
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
        player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
        player.playEffect(player.getLocation(), Effect.HAPPY_VILLAGER, 1);
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