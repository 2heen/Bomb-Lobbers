package me.tsbloxorz.bomblobbers.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

import me.tsbloxorz.bomblobbers.MainClass;

public class Trails implements Runnable {
	private MainClass plugin = MainClass.getInstance();
	public HashMap<TNTPrimed, Player> tntList = new HashMap<TNTPrimed, Player>();
	
	@Override
	public void run() {
		if (plugin.tntTrails.tntList.size() >= 1) {
			for (TNTPrimed tnt : plugin.tntTrails.tntList.keySet()) {
				Player player = plugin.tntTrails.tntList.get(tnt);
				if ((tnt.isDead()) || (tnt == null)) {
					plugin.tntTrails.tntList.remove(tnt, player);
					return;
				}
				particle(tnt.getLocation(), player);
				new BukkitRunnable() {
					public void run() {
				    	//direct hit
				    	List<Entity> nearbyPlayers = tnt.getNearbyEntities(0.2, 0.2, 0.2);
						for (Entity entity : nearbyPlayers) {
							if ((entity.getType() == EntityType.PLAYER) && (!tnt.isOnGround())) {
								Player playerHit = (Player) entity;
								PlayerManager x = plugin.playerManager.get(playerHit);
								playerHit.damage(2);
								x.setDeathReason("direct");
								x.setKiller(player);
							}
						}
						/*nearbyPlayers = tnt.getNearbyEntities(5, 5, 5);
						for (Entity entity : nearbyPlayers) {
							if (entity.getType() == EntityType.PLAYER) {
								Player playerHit = (Player) entity;
								double distance = tnt.getLocation().distanceSquared(playerHit.getLocation());
								if (distance > 20) {
									distance = 20;
								}
								plugin.gameManager.playerDamage(playerHit, 20 - distance, "tnt", player);
							}
						} */
						
					
						
			      }
			    }.runTaskTimerAsynchronously(plugin, 5L, 5L);
				
			}
		}
		
	}
	private void particle(Location loc, Player player) {
		PlayerManager x = plugin.playerManager.get(player);
		EnumParticle e = EnumParticle.valueOf("FIREWORKS_SPARK");
		if (x.getTeam().equalsIgnoreCase("red")) {
			e = EnumParticle.valueOf("REDSTONE");
		} else if (x.getTeam().equalsIgnoreCase("blue")) {
			e = EnumParticle.valueOf("WATER_DROP");
		}
	    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(e, true, (float)loc.getX(), 
	      (float)loc.getY(), (float)loc.getZ(), 0.0F, 0.0F, 0.0F, 0.0F, 15, null);
	    for (Player p : Bukkit.getOnlinePlayers()) {
	      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	    }
	  }
	
	
}
