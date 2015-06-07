package com.gmail.gogobebe2.gthub;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Trail extends BukkitRunnable {

    private Player player;

    private int counter;

    public Trail(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (player.isFlying() || player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
            player.getWorld().playEffect(player.getLocation(), Effect.HAPPY_VILLAGER, 1);
        }
        else {
            this.cancel();
        }
    }
}