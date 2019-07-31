package me.tsbloxorz.bomblobbers.Stats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommands implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (label.equalsIgnoreCase("stats")) {
				Player p = (Player) sender;
				if (p.hasPermission("bomblobbers.play")) {
					Stats stats = new Stats();
					if (args.length > 0) {
						stats.getStats(p, args[0]);
						return true;
					} else {
						stats.getStats(p);
						return true;
					}
				}
			}
		} else {
			sender.sendMessage("You must be a player!");
		}
		return false;
	}
}
