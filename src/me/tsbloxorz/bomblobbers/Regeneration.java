package me.tsbloxorz.bomblobbers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.material.MaterialData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import me.tsbloxorz.bomblobbers.Game.PlayerManager;

public class Regeneration implements Listener {
	private MainClass plugin = MainClass.getInstance();
	public ArrayList<BlockState> regenBlocks = new ArrayList<>();
	
	@EventHandler
	public void blockBreak(EntityExplodeEvent e) {
		if ((e.getEntityType() == EntityType.PRIMED_TNT) && (plugin.gameManager.inProgress)) {
			Player player = plugin.tntTrails.tntList.get((TNTPrimed) e.getEntity());
			PlayerManager x = plugin.playerManager.get(player);
			if (x.getTeam().equalsIgnoreCase("blue")) {
				if (plugin.gameManager.blueZone.containsLocation(e.getLocation())) {
					e.setCancelled(true);
					return;
				}
			} else if (x.getTeam().equalsIgnoreCase("red")) {
				if (plugin.gameManager.redZone.containsLocation(e.getLocation())) {
					e.setCancelled(true);
					return;
				}
			}
		}
		final List<Block> blockTemp = e.blockList();
		for (int i = 0; i < blockTemp.size(); i++) {
			plugin.regen.regenBlocks.add(blockTemp.get(i).getState());
			blockTemp.get(i).setType(Material.AIR);
		}
		
	}
	
	public void regen() {
		for (BlockState state : plugin.regen.regenBlocks) {
			state.update(true, false);
		}
		plugin.regen.regenBlocks.clear();
		System.out.println("The map has been regenerated!");
	} 
	


}
