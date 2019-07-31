package me.tsbloxorz.bomblobbers.Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.tsbloxorz.bomblobbers.MainClass;
import me.tsbloxorz.bomblobbers.Stats.Stats;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;

public class GameListeners implements Listener {
	private MainClass plugin = MainClass.getInstance();
	
	@EventHandler
	public void doubleJump(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!plugin.playerManager.containsKey(player)) {
			return;
		}
		Material m = event.getPlayer().getLocation().getBlock().getType();
	    if (m == Material.STATIONARY_WATER || m == Material.WATER) {
	    	return;
	    }
		PlayerManager x = plugin.playerManager.get(player);
		if ((plugin.gameManager.inProgress == true) && (x.getKit().equalsIgnoreCase("jumper"))){
			if ((player.getGameMode() != GameMode.CREATIVE) && (!player.isFlying()) && (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) && (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.WATER)) {
				player.setAllowFlight(true);
			}
		}
			
	}
	@EventHandler
	public void toggleFlight(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if ((player.getGameMode() == GameMode.CREATIVE) || (player.getGameMode() == GameMode.SPECTATOR)){
			return;
		}
		event.setCancelled(true);
		player.setAllowFlight(false);
		player.setFlying(false);
		player.setVelocity(player.getLocation().getDirection().multiply(1.2).setY(1));
	}
	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		if ((plugin.gameManager.inProgress) && (plugin.playerManager.containsKey(e.getPlayer()))) {
			Material m = e.getPlayer().getLocation().getBlock().getType();
			PlayerManager x = plugin.playerManager.get(e.getPlayer());
		    if (m == Material.STATIONARY_WATER || m == Material.WATER) {
		        e.getPlayer().damage(2);
		        x.setDeathReason("water");
		        x.setKiller(null);
		    }
			if (!x.isSpectator()) {
				if (x.getTeam().equalsIgnoreCase("blue")) {
					if (!plugin.gameManager.blueZone.containsLocation(e.getPlayer().getLocation())) {
						e.getPlayer().damage(20);
				        x.setDeathReason("cheating");
				        x.setKiller(null);
					}
				} else if (x.getTeam().equalsIgnoreCase("red")) {
					if (!plugin.gameManager.redZone.containsLocation(e.getPlayer().getLocation())) {
						e.getPlayer().damage(20);
				        x.setDeathReason("cheating");
				        x.setKiller(null);
					}
				}
			}
			
		}
	}
	@EventHandler
	public void changeVelocityTicks(PlayerInteractEvent e) { 
		if (e.getMaterial() == Material.LEVER) {
			PlayerManager x = plugin.playerManager.get(e.getPlayer());
			double currentVelocity = x.getTntVelocity();
			if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				currentVelocity -= 0.1;
			} else if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
				currentVelocity += 0.1;
			}
			x.setTntVelocity(currentVelocity);
			e.getPlayer().sendMessage(ChatColor.RED + "TNT Velocity: " + currentVelocity);
		} else if (e.getMaterial() == Material.STONE_BUTTON) {
			PlayerManager x = plugin.playerManager.get(e.getPlayer());
			int currentTicks = x.getTntTicks();
			if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				currentTicks -= 5;
			} else if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
				currentTicks += 5;
			}
			x.setTntTicks(currentTicks);
			e.getPlayer().sendMessage(ChatColor.RED + "TNT Ticks: " + currentTicks);
		}
	} 
	@EventHandler
	public void playerThrowTNT(PlayerInteractEvent event) {
		if ((plugin.gameManager.inProgress) && (plugin.playerManager.containsKey(event.getPlayer()))) {
			Player player = event.getPlayer();
			PlayerManager x = plugin.playerManager.get(player);
			if ((x.isSpectator()) || (player.getGameMode() == GameMode.CREATIVE)){
				return;
			}
			if (event.getMaterial() == Material.TNT) {
				
				
				Entity tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
				((TNTPrimed)tnt).setFuseTicks(x.getTntTicks());

				
			    tnt.setVelocity(player.getEyeLocation().getDirection().multiply(x.getTntVelocity()));
			    plugin.tntTrails.tntList.put((TNTPrimed) tnt, player);
			    tnt.setCustomName(x.getPlayer().getName());
			    player.getInventory().removeItem(new ItemStack(Material.TNT, 1));
			}
		}
	} 
	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		if ((plugin.gameManager.inProgress) && (plugin.playerManager.containsKey(e.getPlayer()))) {
			plugin.gameManager.gameEnd(e.getPlayer());
		}
	}
	@EventHandler
	public void playerDeath(PlayerDeathEvent e) {
		Player player = (Player) e.getEntity();
		e.setDeathMessage("");
		e.setDroppedExp(0);
		e.getDrops().clear();
		
		if (player.getLastDamageCause().getCause() == DamageCause.ENTITY_EXPLOSION) {
			final EntityDamageEvent damage = (EntityDamageEvent) player.getLastDamageCause();
		    if (damage instanceof EntityDamageByEntityEvent) {
		        final Player killer = plugin.tntTrails.tntList.get((TNTPrimed) (((EntityDamageByEntityEvent) damage).getDamager()));
		        PlayerManager x = plugin.playerManager.get(player);
		        if (killer != null) {
		        	x.setDeathReason("tnt");
		        	x.setKiller(killer);
		        }
		    }
		} 
		if ((plugin.gameManager.inProgress) && (plugin.playerManager.containsKey(player))) {
			plugin.gameManager.gameEnd(player);
		} else {
			player.teleport((Location) plugin.getConfig().get("LobbySpawn"));
		}
		
	}
	/*@EventHandler
	public void tntExplode(EntityDamageByEntityEvent e) {
		Player player = (Player) e.getEntity();
		if (player.getLastDamageCause().getCause() == DamageCause.ENTITY_EXPLOSION) {
			final EntityDamageEvent damage = ((Player) e).getPlayer().getLastDamageCause();
		    if (damage instanceof EntityDamageByEntityEvent) {
		    	
		    	System.out.println(player.getName() + " died by " + ((EntityDamageByEntityEvent) damage).getDamager().getCustomName());
		    }
		} 
		if ((player.getLastDamageCause().getCause() == DamageCause.ENTITY_EXPLOSION) && (player.isDead())) {
			System.out.println(player.getName() + " died by " + e.getDamager().getCustomName());
		} 
	} */
	
	@EventHandler
	public void fallDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if ((plugin.gameManager.inProgress) && (plugin.playerManager.containsKey((Player) e.getEntity()))) {
				Player player = (Player) e.getEntity();
				PlayerManager x = plugin.playerManager.get(player);
				if (plugin.playerManager.get(player).getKit().equalsIgnoreCase("jumper")) {
					e.setCancelled(true);
				} else {
					x.setDeathReason("fall damage");
					x.setKiller(null);
				}
			}
		} 
	}
	@EventHandler
	public void voidDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.VOID) {
			if ((plugin.gameManager.inProgress) && (plugin.playerManager.containsKey((Player) e.getEntity()))) {
				Player player = (Player) e.getEntity();
				PlayerManager x = plugin.playerManager.get(player);
				x.setDeathReason("void");
				x.setKiller(null);
				
			}
		} 
	}

	
}
