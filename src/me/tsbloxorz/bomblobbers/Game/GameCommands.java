package me.tsbloxorz.bomblobbers.Game;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tsbloxorz.bomblobbers.MainClass;
import me.tsbloxorz.bomblobbers.Setup.SetupYML;

public class GameCommands implements CommandExecutor {
	private MainClass plugin = MainClass.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if ((label.equalsIgnoreCase("bl")) && (args.length == 1)) {
				Player player = (Player) sender;
				if (player.hasPermission("bomblobbers.play")) {
					if (args[0].equalsIgnoreCase("join")) {
						joinGame(player);
					} else if (args[0].equalsIgnoreCase("leave")) {
						leaveGame(player);
					} else if (args[0].equalsIgnoreCase("status")) {
						printGameStatus(player);
					} else if (args[0].equalsIgnoreCase("watch")) {
						watchGame(player);
					} else {
						printHelp(player);
					}
				} else {
					player.sendMessage(ChatColor.RED + "You do not have the required permission!");
					return false;
				}
			}
		}
		return true;
	}
	private void printHelp(Player player) {
		player.sendMessage(ChatColor.RED + "/bl join - joins the game");
		player.sendMessage(ChatColor.RED + "/bl leave - leaves the game");
		player.sendMessage(ChatColor.RED + "/bl status - displays the game status");
		player.sendMessage(ChatColor.RED + "/bl watch - watch the game");
	}
	public void watchGame(Player player) {
		if (plugin.gameManager.inProgress) {
			SetupYML setup = new SetupYML();
			try {
				setup.initiateFiles();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plugin.playerManager.put(player, new PlayerManager(player, true));
			player.teleport((Location) setup.getModifyFile().get("Map_" + plugin.gameManager.mapNum + ".spectator_spawn"));
			player.setGameMode(GameMode.SPECTATOR);
		} else {
			if (plugin.playerManager.containsKey(player)) {
				player.sendMessage(ChatColor.RED + "You are already in the game!");
				return;
			}
			plugin.playerManager.put(player, new PlayerManager(player, true));
			player.sendMessage(ChatColor.GREEN + "You have joined the watch squad!");
		}
		
	}
	public void printGameStatus(Player player) {
		if (plugin.gameManager.inProgress) {
			player.sendMessage("The current game is in Progress!");
		} else {
			player.sendMessage("The game has not yet started! Use /bl join to join!");
		}
		
	}
	public void leaveGame(Player player) {
		if (plugin.gameManager.inProgress) {
			player.sendMessage(ChatColor.RED + "Game is already in progress!");
			return;
		}
		if (plugin.playerManager.size() <= 2) {
			player.sendMessage(ChatColor.RED + "Too late now!");
			return;
		}
		if (plugin.playerManager.containsKey(player)) {
			plugin.playerManager.remove(player);
			player.sendMessage(ChatColor.RED + "You have left the game!");
		} else {
			player.sendMessage("You are not in the current game!");
		}
		
	}
	public void joinGame(Player player) {
		if (plugin.playerManager.containsKey(player)) {
			player.sendMessage("You are already in the game!");
		} else {
			plugin.playerManager.put(player, new PlayerManager(player, false));
			player.sendMessage(ChatColor.GREEN + "You have joined the game!");
		}
	}
	
}
