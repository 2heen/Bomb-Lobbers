package me.tsbloxorz.bomblobbers.Setup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tsbloxorz.bomblobbers.MainClass;

public class SetupCommands implements CommandExecutor {
	private SetupYML setup;
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if ((label.equalsIgnoreCase("setup")) && (args.length >= 1)) {
				Player player = (Player) sender;
				if (player.hasPermission("bomblobbers.setup")) {
					setup = new SetupYML();
					try {
						setup.initiateFiles();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (args[0].equalsIgnoreCase("spawn")) {
						addSpawnPoint(player, args[1], args[2]);
						return true;
					} else if (args[0].equalsIgnoreCase("lobbyspawn")) {
						addLobbySpawn(player);
						return true;
					} else if (args[0].equalsIgnoreCase("spectator")) {
						addSpectatorSpawn(player, args[1]);
						return true;
					} else if (args[0].equalsIgnoreCase("map")) {
						addMap(player, args[1], args[2]);
						return true;
					} else if (args[0].equalsIgnoreCase("zone")) {
						addZone(player, args[1], args[2], args[3]);
						return true;
					} else {
						printSetupHelp(player);
						return true;
					}
				} else {
					player.sendMessage(ChatColor.RED + "You do not have the required permission!");
					return false;
				}
			}
		} else {
			sender.sendMessage("You must be a player!");
			return false;
		}
		return false;
	}
	private void addZone(Player player, String map, String team, String num) {
		if ((getMapNum(map) != -1) && (checkTeam(team)) && ((num.equals("1") || num.equals("2")))) {
			setup.getModifyFile().set("Map_" + getMapNum(map) + "." + team + "_zone_" + num, player.getLocation());
			setup.saveFile();
			player.sendMessage("The Cuboid zone has been created!");
		}
	}
	private void addMap(Player player, String map, String author) {
		if (!map.isEmpty() && !author.isEmpty()) {
			int numMaps = setup.getModifyFile().getInt("NumberOfMaps") + 1;
			setup.getModifyFile().set("Map_" + numMaps + ".Name", map);
			setup.getModifyFile().set("Map_" + numMaps + ".Author", author);
			setup.getModifyFile().set("NumberOfMaps", numMaps);
			setup.saveFile();
			player.sendMessage("The map has been added!");
		}
		
	}
	private int getMapNum(String map) {
		int numMaps = setup.getModifyFile().getInt("NumberOfMaps");
		for (int i = 1; i <= numMaps; i++) {
			if (setup.getModifyFile().get("Map_" + i + ".Name").equals(map)) {
				return i;
			}
		}
		return -1;
	}
	private boolean checkTeam(String team) {
		if (team.isEmpty()) {
			return false;
		} 
		if ((team.equals("blue")) || (team.equals("red"))) {
			return true;
		}
		return false;
	}
	private void printSetupHelp(Player player) {
		player.sendMessage(ChatColor.RED + "/setup map (map) (author)");
		player.sendMessage(ChatColor.RED + "/setup spawn (team) (map)");
		player.sendMessage(ChatColor.RED + "/setup spectator (map)");
		player.sendMessage(ChatColor.RED + "/setup zone (map) (team) (number (1/2))");
		player.sendMessage(ChatColor.RED + "/setup lobbyspawn");
	}


	private void addSpectatorSpawn(Player player, String map) {
		if (map.isEmpty() || getMapNum(map) == -1) {
			player.sendMessage("You must specify what map!");
			return;
		}
		setup.getModifyFile().set("Map_" + getMapNum(map) + ".Spectator_Spawn", player.getLocation());
		setup.saveFile();
		player.sendMessage("The spectator spawn has been added!");
	}


	private void addLobbySpawn(Player player) {
		setup.getModifyFile().set("LobbySpawn", player.getLocation());
		setup.saveFile();
		player.sendMessage("The lobby spawnpoint has been added!");
	}


	private void addSpawnPoint(Player player, String team, String map) {
		if ((team.isEmpty()) || (map.isEmpty()) || (!checkTeam(team)) || (getMapNum(map) == -1)) {
			player.sendMessage("You must specify what team and the map!");
			return;
		}
		setup.getModifyFile().set("Map_" + getMapNum(map) + "." + team + "_spawn", player.getLocation());
		setup.saveFile();
		player.sendMessage("The team spawnpoint has been added!");
	}
	
}
