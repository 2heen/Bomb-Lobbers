package me.tsbloxorz.bomblobbers;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.tsbloxorz.bomblobbers.Game.GameCommands;
import me.tsbloxorz.bomblobbers.Kits.KitsCommands;
import me.tsbloxorz.bomblobbers.Setup.SetupYML;
import me.tsbloxorz.bomblobbers.Stats.Stats;

public class Menu implements Listener {
	private MainClass plugin = MainClass.getInstance();
	private Inventory gui;
	
	public void giveMenu(Player player) {
		ItemStack menu = new ItemStack(Material.PAPER);
		ItemMeta meta = menu.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Game Menu");
		menu.setItemMeta(meta);
		player.getInventory().addItem(new ItemStack(menu));
		giveBook(player);
	}
	private void giveBook(Player player) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookmeta = (BookMeta) book.getItemMeta();
		bookmeta.setAuthor("TSBloxorz");
		bookmeta.setTitle(ChatColor.AQUA + "Bomb Lobbers");
		ArrayList<String> pages = new ArrayList<String>();
		pages.add(ChatColor.GREEN + "Welcome to Bomb Lobbers! This is as close of a copy to Mineplex's Bomb Lobbers game as you'll find!\nCreated by: TSBloxorz");
		pages.add(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Table of Contents\n" + ChatColor.AQUA + "How to play\nPlayer Commands\nKit Commands\nExtra Information\n/Patch Notes/Updates");
		pages.add(ChatColor.GOLD + "The game is simple. You get a TNT every 3 seconds, and throw the TNT at the other team to blow them up! Be the last to survive and you win!");
		pages.add(ChatColor.RED + "" + ChatColor.BOLD + "Player Commands\n/bl join - joins the game\n/bl leave - leaves the game\n/"
				+ "bl status - displays the current status of the game\n/bl watch - watch the current game");
		pages.add("Kit Commands\n/kit list - lists all the kits\n/kit <kit> - to change kits");
		pages.add(ChatColor.RED + "" + ChatColor.BOLD + "This is simply 'fan' made and purely for fun. If you find any bugs though, please let me know!");
		pages.add("Newest Updates:\n1. Fixed direct hits\n2. Added TNT trail");
		bookmeta.setPages(pages);
		
		book.setItemMeta(bookmeta);
		player.getInventory().addItem(book);
	}
	@EventHandler
	public void openMenu(PlayerInteractEvent e) {
		if ((!plugin.gameManager.inProgress) || (!plugin.playerManager.containsKey(e.getPlayer()))) {
		    if (e.getPlayer().getInventory().getItemInHand().getType().equals(Material.PAPER)) {
		    	createMenu();
		    	e.getPlayer().openInventory(gui);
		    }
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		GameCommands gameCom = new GameCommands();
		KitsCommands kitsCom = new KitsCommands();
		switch(e.getCurrentItem().getType()) {
		case EMERALD_BLOCK:
			gameCom.joinGame(player);
			break;
		case REDSTONE_BLOCK:
			gameCom.leaveGame(player);
			break;
		case IRON_BLOCK:
			gameCom.printGameStatus(player);
			break;
		case GOLD_BLOCK:
			gameCom.watchGame(player);
			break;
		case LEATHER_BOOTS:
			kitsCom.changeKit(player, "jumper");
			break;
		case GOLD_CHESTPLATE:
			kitsCom.changeKit(player, "armorer");
			break;
		case LEVER:
			kitsCom.changeKit(player, "pitcher");
			break;
		case STONE_BUTTON:
			kitsCom.changeKit(player, "clocksman");
			break;
		default:
			break;
		} 
		player.closeInventory();
		e.setCancelled(true);
	}
	private void createMenu() {
		//create gui
		gui = Bukkit.createInventory(null, 18, ChatColor.RED + "Game Menu!");
		ItemMeta meta;
		//create variables
		ItemStack joinGame = new ItemStack(Material.EMERALD_BLOCK);
		meta = joinGame.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Join game");
		joinGame.setItemMeta(meta);
		
		ItemStack leaveGame = new ItemStack(Material.REDSTONE_BLOCK);
		meta = leaveGame.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Leave game");
		leaveGame.setItemMeta(meta);
		
		ItemStack statusGame = new ItemStack(Material.IRON_BLOCK);
		meta = statusGame.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Game Status");
		statusGame.setItemMeta(meta);
		
		ItemStack watchGame = new ItemStack(Material.GOLD_BLOCK);
		meta = watchGame.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Watch game");
		watchGame.setItemMeta(meta);
		
		ItemStack jumperKit = new ItemStack(Material.LEATHER_BOOTS);
		meta = jumperKit.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Jumper");
		jumperKit.setItemMeta(meta);
		
		ItemStack armorerKit = new ItemStack(Material.GOLD_CHESTPLATE);
		meta = armorerKit.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Armorer");
		armorerKit.setItemMeta(meta);
		
		ItemStack pitcherKit = new ItemStack(Material.LEVER);
		meta = pitcherKit.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Pitcher");
		pitcherKit.setItemMeta(meta);
		
		ItemStack clocksmanKit = new ItemStack(Material.STONE_BUTTON);
		meta = clocksmanKit.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Clocksman");
		clocksmanKit.setItemMeta(meta);
		
		//add them to inventory
		gui.setItem(2, joinGame);
		gui.setItem(3, leaveGame);
		gui.setItem(4, statusGame);
		gui.setItem(5, watchGame);
		gui.setItem(11, jumperKit);
		gui.setItem(12, armorerKit);
		gui.setItem(13, pitcherKit);
		gui.setItem(14, clocksmanKit);
		
		
	}
	
}
