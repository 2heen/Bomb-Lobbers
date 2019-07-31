package me.tsbloxorz.bomblobbers.Kits;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tsbloxorz.bomblobbers.MainClass;
import me.tsbloxorz.bomblobbers.Game.PlayerManager;


public class KitsCommands implements CommandExecutor {
	private MainClass plugin = MainClass.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (label.equalsIgnoreCase("kit")) {
				if (player.hasPermission("bomblobbers.play")) {
					if (plugin.gameManager.inProgress) {
						player.sendMessage(ChatColor.RED + "Game is in Progress! Change after the game!");
						return true;
					}
					if (args.length > 0) {
						 if (plugin.playerManager.containsKey(player)) {
							 player.sendMessage(ChatColor.GREEN + "Kit has been changed to " + args[0] + "!");
							 changeKit(player, args[0].toLowerCase());
						 } else if (args[0].equalsIgnoreCase("list")) {
							 player.sendMessage(ChatColor.RED + "Possible Kits are: Jumper, Armorer, Pitcher, Clocksman");
						 }
					} else {
						player.sendMessage(ChatColor.RED + "Usage: /kits <kit> or /kits list to list the kits");
						player.sendMessage(ChatColor.RED + "You may only change kits after you have joined the game!");
					}
				} else {
					player.sendMessage(ChatColor.RED + "You do not have the required permissions!");
				}
			}
		} else {
			sender.sendMessage("You must be a player!");
		}
		return true;
	}
	public void changeKit(Player player, String kit) {
		if (plugin.gameManager.inProgress) {
			player.sendMessage(ChatColor.RED + "Game is in Progress! Change after the game!");
			return;
		}
		if (!plugin.playerManager.containsKey(player)) {
			 player.sendMessage(ChatColor.RED + "You must join the game first!");
			 return;
		}
		if (checkKit(kit)) {
			PlayerManager x = plugin.playerManager.get(player);
			x.setKit(kit.toLowerCase());
			player.sendMessage(ChatColor.GREEN + "You have changed to kit " + kit + "!");
		} else {
			player.sendMessage(ChatColor.RED + "Invalid Kit!");
		}
	}
	
	private boolean checkKit(String kit) {
		if ((kit.equalsIgnoreCase("jumper")) || (kit.equalsIgnoreCase("armorer")) || (kit.equalsIgnoreCase("pitcher")) || (kit.equalsIgnoreCase("clocksman"))) {
			return true;
		} else {
			return false;
		}
	}
}

