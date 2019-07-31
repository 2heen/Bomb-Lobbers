package me.tsbloxorz.bomblobbers.Game;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.fusesource.jansi.Ansi.Color;

import me.tsbloxorz.bomblobbers.MainClass;
import me.tsbloxorz.bomblobbers.Menu;
import me.tsbloxorz.bomblobbers.Kits.Kits;
import me.tsbloxorz.bomblobbers.Setup.SetupYML;
import me.tsbloxorz.bomblobbers.Stats.Stats;

public class GameManager implements Runnable {
	private MainClass plugin = MainClass.getInstance();
	public boolean inProgress = false;
	public int gameCountdown = plugin.getConfig().getInt("Game_Countdown");
	public boolean countdown = false; //true when the countdown should happen, false when it should not
	public boolean callCountdown = true;
	public String winningTeam;
	public int mapNum; //the map # in setup.yml (Map_mapNum)
	public boolean preGame = false; //false means preGame hasn't been completed yet, true means it has
	public Cuboid redZone;
	public Cuboid blueZone;
	@Override
	public void run() {
		int numPlayers = 0;
		for (Player key : plugin.playerManager.keySet()) {
			PlayerManager x = plugin.playerManager.get(key);
			if (!x.isSpectator()) {
				numPlayers++;
			}
		}
		if ((numPlayers >= plugin.getConfig().getInt("Min_Players")) && (!plugin.gameManager.preGame) && (plugin.gameManager.callCountdown)) {
			plugin.gameManager.countdown = true;
			countdown();
		}
		if ((plugin.gameManager.gameCountdown == 0)  && (!plugin.gameManager.preGame)){
			preGame();
		}
		if ((plugin.gameManager.gameCountdown == 0) && (plugin.gameManager.preGame)) {
			if (!checkGameEnd()) {
				duringGame();
			} else {
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "The winning Team is " + plugin.gameManager.winningTeam);
				gameEnd();
				
			}
		}
		
	}
	private void gameEnd() {
		Stats stats = new Stats();
		SetupYML setup = new SetupYML();
		try {
			setup.initiateFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Player key : plugin.playerManager.keySet()) {
			PlayerManager x = plugin.playerManager.get(key);
			x.getPlayer().teleport((Location) setup.getModifyFile().get("LobbySpawn"));
			if (!x.isSpectator()) {
				stats.addGame(x.getPlayer());
			}
			if (x.getTeam().equalsIgnoreCase(plugin.gameManager.winningTeam)) {
				stats.addWin(x.getPlayer());
			}
			x.getPlayer().getInventory().clear();
			removeArmor(x.getPlayer());
			x.getPlayer().setGameMode(GameMode.SURVIVAL);
			if (!x.getPlayer().getInventory().contains(new ItemStack(Material.PAPER))) {
				Menu menu = new Menu();
				menu.giveMenu(x.getPlayer());
			}
		}
		plugin.playerManager.clear();
		plugin.gameManager.preGame = false;
		plugin.gameManager.inProgress = false;
		plugin.gameManager.gameCountdown = plugin.getConfig().getInt("Game_Countdown");;
		plugin.gameManager.callCountdown = true;
		plugin.tntTrails.tntList.clear();
		plugin.regen.regen();
		Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "The game has ended!");
	}
	public void removeArmor(Player player) {
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
	    player.getInventory().setBoots(null);
		
	}
	private boolean checkGameEnd() {
		//check the teams
		int numBlue = 0;
		int numRed = 0;
		for (Player key : plugin.playerManager.keySet()) {
			PlayerManager x = plugin.playerManager.get(key);
			if ((x.getTeam().equalsIgnoreCase("blue")) && (!x.isSpectator())) 
				numBlue++;
			else if ((x.getTeam().equalsIgnoreCase("red")) && (!x.isSpectator()))
				numRed++;
		}
		if ((numBlue == 0) || (numRed == 0)) {
			if (numBlue > numRed) {
				plugin.gameManager.winningTeam = "Blue";
			} else if (numRed > numBlue) {
				plugin.gameManager.winningTeam = "Red";
			} else {
				plugin.gameManager.winningTeam = "Inconclusive";
			}
			return true;
		} else {
			return false;
		}
	}
	public void gameEnd(Player player) {
		Stats stats = new Stats();
		SetupYML setup = new SetupYML();
		try {
			setup.initiateFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PlayerManager x = plugin.playerManager.get(player);
		x.getPlayer().teleport((Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + ".spectator_spawn"));
		x.getPlayer().setGameMode(GameMode.SPECTATOR);
		x.setSpectator(true);
		x.getPlayer().getInventory().clear();
		stats.addDeath(player);
		stats.addGame(player);
		printDeath(player);
	}
	private void duringGame() {
		for (Player key : plugin.playerManager.keySet()) {
			PlayerManager x = plugin.playerManager.get(key);
			if (!x.isSpectator()) {
				if (!x.getPlayer().getInventory().contains(Material.TNT, 3)) {
					x.getPlayer().getInventory().addItem(new ItemStack(Material.TNT, 1));
				}
			}
		}
		
	}
	
	private void preGame() {
		SetupYML setup = new SetupYML();
		try {
			setup.initiateFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//choose and set the maps in setup.yml
		Random rand = new Random();
		plugin.gameManager.mapNum = rand.nextInt(setup.getModifyFile().getInt("NumberOfMaps")) + 1;
		setup.getModifyFile().set("Current_Map_Number", plugin.gameManager.mapNum);
		setup.saveFile();
		//set teams, blue will always have equal/more players than red
		int numBlue = 0;
		int numRed = 0;
		for (Player key : plugin.playerManager.keySet()) {
			PlayerManager x = plugin.playerManager.get(key);
			if (!x.isSpectator()) {
				if (numBlue <= numRed) {
					x.setTeam("blue");
					numBlue++;
				} else if (numBlue > numRed) {
					x.setTeam("red");
					numRed++;
				}
			}
		}
		
		//teleport players
		for (Player key : plugin.playerManager.keySet()) {
			String map = (String) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + ".Name");
			PlayerManager x = plugin.playerManager.get(key);
			if (x.isSpectator()) {
				x.getPlayer().teleport((Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + "." + "spectator_spawn"));
				x.getPlayer().setGameMode(GameMode.SPECTATOR);
			} else {
				x.getPlayer().teleport((Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + "." + x.getTeam().toLowerCase() + "_spawn"));
				x.getPlayer().setGameMode(GameMode.SURVIVAL);
			}
			x.getPlayer().getInventory().clear();
			x.getPlayer().setFoodLevel(20);
			x.getPlayer().setHealth(20);
		}
		//give Kits
		Kits kits = new Kits();
		kits.giveKits();
		plugin.gameManager.preGame = true;
		plugin.tntTrails.tntList.clear();
		plugin.gameManager.inProgress = true;
		plugin.gameManager.countdown = false;
		plugin.gameManager.redZone = new Cuboid((Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + ".red_zone_1"), (Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + ".red_zone_2"));
		plugin.gameManager.blueZone = new Cuboid((Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + ".blue_zone_1"), (Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + ".blue_zone_2"));
		Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Game starting!");
	}

	private void countdown() {
		plugin.gameManager.callCountdown = false;
		new BukkitRunnable() {
			@Override
			public void run() {
				if ((plugin.gameManager.gameCountdown > 0) && (plugin.gameManager.countdown)) {
					if ((plugin.gameManager.gameCountdown == 60) || (plugin.gameManager.gameCountdown == 30) || (plugin.gameManager.gameCountdown <= 5)) {
						Bukkit.getServer().broadcastMessage(ChatColor.RED + "The game will start in " + plugin.gameManager.gameCountdown + " seconds.");
					}
					plugin.gameManager.gameCountdown--;
				}
				
				
			}
		}.runTaskTimer(plugin, 0, 20L);
		
	}
	private void printDeath(Player player) {
			String deathMessage = "";
			PlayerManager x = plugin.playerManager.get(player);
			if (x.getDeathReason().equalsIgnoreCase("Cheating")) {
				if (x.getTeam().equalsIgnoreCase("blue")) {
					deathMessage = "" + ChatColor.BLUE + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + " Cheating.";
				} else {
					deathMessage = "" + ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + " Cheating.";
				}
			} else if (x.getDeathReason().equalsIgnoreCase("fall damage")) {
				if (x.getTeam().equalsIgnoreCase("blue")) {
					deathMessage = "" + ChatColor.BLUE + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + " Fall Damage.";
				} else {
					deathMessage = "" + ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + " Fall Damage.";
				}
			} else if ((x.getDeathReason().equalsIgnoreCase("direct")) && (x.getKiller() != null)) {
				Stats stats = new Stats();
				stats.addKill(x.getKiller());
				if (x.getTeam().equalsIgnoreCase("blue")) {
					deathMessage = "" + ChatColor.BLUE + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.RED + x.getKiller().getName() + ChatColor.GRAY + " due to " + ChatColor.YELLOW + "Direct Hit.";
				} else {
					deathMessage = "" + ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.BLUE + x.getKiller().getName() + ChatColor.GRAY + " due to " + ChatColor.YELLOW + "Direct Hit.";
				}
			} else if ((x.getDeathReason().equalsIgnoreCase("tnt")) && (x.getKiller() != null)) {
				Stats stats = new Stats();
				stats.addKill(x.getKiller());
				if (x.getTeam().equalsIgnoreCase("blue")) {
					deathMessage = "" + ChatColor.BLUE + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.RED + x.getKiller().getName() + ChatColor.GRAY + " due to " + ChatColor.YELLOW + "TNT.";
				} else {
					deathMessage = "" + ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.BLUE + x.getKiller().getName() + ChatColor.GRAY + " due to " + ChatColor.YELLOW + "TNT.";
				}
			} else if (x.getDeathReason().equalsIgnoreCase("water")) {
				if (x.getTeam().equalsIgnoreCase("blue")) {
					deathMessage = "" + ChatColor.BLUE + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + "Water.";
				} else {
					deathMessage = "" + ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + "Water.";
				}
			} else if (x.getDeathReason().equalsIgnoreCase("void")) {
				if (x.getTeam().equalsIgnoreCase("blue")) {
					deathMessage = "" + ChatColor.BLUE + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + " Void.";
				} else {
					deathMessage = "" + ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed due to " + ChatColor.YELLOW + " Void.";
				}
			} else {
				if (x.getTeam().equalsIgnoreCase("blue")) {
					deathMessage = "" + ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has died.";
				} else {
					deathMessage = "" + ChatColor.RED + player.getName() + ChatColor.GRAY + " has died.";
				}
			}
			for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
				onlinePlayers.sendMessage(deathMessage);
			}
			
		
	}
	
	
	
}
