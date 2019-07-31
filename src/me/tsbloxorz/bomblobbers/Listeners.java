package me.tsbloxorz.bomblobbers;

import java.io.IOException;
import java.util.ArrayList;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
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
import org.bukkit.event.player.PlayerRespawnEvent;

import me.tsbloxorz.bomblobbers.Setup.SetupYML;
import me.tsbloxorz.bomblobbers.Stats.Stats;


public class Listeners implements Listener {
	private MainClass plugin = MainClass.getInstance();
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		e.getBlock().getDrops().clear();
		e.setCancelled(true);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		SetupYML setup = new SetupYML();
		try {
			setup.initiateFiles();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
		e.getPlayer().getInventory().clear();
		e.getPlayer().sendMessage(ChatColor.RED + "Welcome to TSBloxorz's Bomb Lobbers server!");
		e.getPlayer().sendMessage(ChatColor.RED + "If this is your first time joining, you may need to disconnect and relog!");
		//TELEPORT THEM TO LOBBY
		e.getPlayer().teleport((Location) setup.getModifyFile().get("LobbySpawn"));
		Stats stats = new Stats();
		stats.addPlayer(e.getPlayer());
		Menu menu = new Menu();
		menu.giveMenu(e.getPlayer());
		plugin.gameManager.removeArmor(e.getPlayer());
	}
	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void pickupItem(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void fullHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e) {
		SetupYML setup = new SetupYML();
		try {
			setup.initiateFiles();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e.setRespawnLocation((Location) setup.getModifyFile().get("LobbySpawn"));
		Menu menu = new Menu();
		menu.giveMenu(e.getPlayer());
		plugin.gameManager.removeArmor(e.getPlayer());
	}
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		e.setCancelled(true);
	}

}
