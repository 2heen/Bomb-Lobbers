package me.tsbloxorz.bomblobbers.Stats;

import java.io.IOException;
import java.text.DecimalFormat;

import org.bukkit.entity.Player;



public class Stats {
	StatsYML database = new StatsYML();
	
	public Stats() {
		try {
			database.initiateFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addPlayer(Player player) {
		if (!database.getModifyFile().contains(player.getName().toLowerCase())) {
			database.getModifyFile().createSection(player.getName().toLowerCase());
			database.getModifyFile().set(player.getName().toLowerCase() + ".GamesPlayed", 0);
			database.getModifyFile().set(player.getName().toLowerCase() + ".Wins", 0);
			database.getModifyFile().set(player.getName().toLowerCase() + ".Kills", 0);
			database.getModifyFile().set(player.getName().toLowerCase() + ".Deaths", 0);
			database.getModifyFile().set(player.getName().toLowerCase() + ".KDRatio", 0.0);
			database.saveFile();
		}
	}
	
	public void addGame(Player player) {
		int x = database.getModifyFile().getInt(player.getName().toLowerCase() + ".GamesPlayed");
		database.getModifyFile().set(player.getName().toLowerCase() + ".GamesPlayed", x + 1);
		database.saveFile();
	}
	public void addWin(Player player) {
		int x = database.getModifyFile().getInt(player.getName().toLowerCase() + ".Wins");
		database.getModifyFile().set(player.getName().toLowerCase() + ".Wins", x + 1);
		database.saveFile();
	}
	public void addKill(Player player) {
		int x = database.getModifyFile().getInt(player.getName().toLowerCase() + ".Kills");
		database.getModifyFile().set(player.getName().toLowerCase()+ ".Kills", x + 1);
		database.saveFile();
		calcKDRatio(player);
	}
	public void addDeath(Player player) {
		int x = database.getModifyFile().getInt(player.getName().toLowerCase() + ".Deaths");
		database.getModifyFile().set(player.getName().toLowerCase() + ".Deaths", x + 1);
		database.saveFile();
		calcKDRatio(player);
	}
	private void calcKDRatio(Player player) {
		if (database.getModifyFile().getInt(player.getName().toLowerCase() + ".Deaths") == 0) {
			database.getModifyFile().set(player.getName().toLowerCase() + ".KDRatio", 100000);
		} else {
			int kills = database.getModifyFile().getInt(player.getName().toLowerCase() + ".Kills");
			int deaths = database.getModifyFile().getInt(player.getName().toLowerCase() + ".Deaths");
			database.getModifyFile().set(player.getName().toLowerCase() + ".KDRatio", (double) (kills / deaths));
		}
		database.saveFile();
	}
	public void getStats(Player player) { //this is if the player asks about his own stats
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		player.sendMessage("Your stats:");
		player.sendMessage("Games Played: " + database.getModifyFile().getInt(player.getName().toLowerCase() + ".GamesPlayed"));
		player.sendMessage("Wins: " + database.getModifyFile().getInt(player.getName().toLowerCase() + ".Wins"));
		player.sendMessage("Kills: " + database.getModifyFile().getInt(player.getName().toLowerCase() + ".Kills"));
		player.sendMessage("Deaths: " + database.getModifyFile().getInt(player.getName().toLowerCase() + ".Deaths"));
		player.sendMessage("KD Ratio: " + df.format(database.getModifyFile().getDouble(player.getName().toLowerCase() + ".KDRatio")));
	}
	public void getStats(Player player, String otherPlayer) { //this is if the player wants to view someone else's stats
		if (database.getModifyFile().contains(otherPlayer.toLowerCase())) {
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			player.sendMessage(otherPlayer + "'s stats:");
			player.sendMessage("Games Played: " + database.getModifyFile().getInt(otherPlayer.toLowerCase() + ".GamesPlayed"));
			player.sendMessage("Wins: " + database.getModifyFile().getInt(otherPlayer.toLowerCase() + ".Wins"));
			player.sendMessage("Kills: " + database.getModifyFile().getInt(otherPlayer.toLowerCase() + ".Kills"));
			player.sendMessage("Deaths: " + database.getModifyFile().getInt(otherPlayer.toLowerCase() + ".Deaths"));
			player.sendMessage("KD Ratio: " + df.format(database.getModifyFile().getDouble(otherPlayer.toLowerCase() + ".KDRatio")));
		} else {
			player.sendMessage("Cannot find player!");
		}
	}
}
