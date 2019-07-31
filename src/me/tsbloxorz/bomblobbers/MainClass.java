package me.tsbloxorz.bomblobbers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.java.JavaPlugin;

import me.tsbloxorz.bomblobbers.Game.GameListeners;
import me.tsbloxorz.bomblobbers.Game.GameManager;
import me.tsbloxorz.bomblobbers.Game.PlayerManager;
import me.tsbloxorz.bomblobbers.Game.Trails;
import me.tsbloxorz.bomblobbers.Setup.SetupYML;
import me.tsbloxorz.bomblobbers.Stats.Stats;

public class MainClass extends JavaPlugin {
	private static MainClass instance;
	public GameManager gameManager;
	public HashMap<Player,PlayerManager> playerManager = new HashMap<Player,PlayerManager>();
	public Regeneration regen;
	public Trails tntTrails;
	
	public void onEnable() { 
	    setInstance(this);
		loadFiles();
		this.getConfig().options().copyDefaults(true);
		instanceClasses();
       
		getCommand("stats").setExecutor(new me.tsbloxorz.bomblobbers.Stats.StatsCommands());
		getCommand("bl").setExecutor(new me.tsbloxorz.bomblobbers.Game.GameCommands());
		getCommand("setup").setExecutor(new me.tsbloxorz.bomblobbers.Setup.SetupCommands());
		getCommand("kit").setExecutor(new me.tsbloxorz.bomblobbers.Kits.KitsCommands());
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getServer().getPluginManager().registerEvents(new Menu(), this);
		getServer().getPluginManager().registerEvents(new GameListeners(), this);
		getServer().getPluginManager().registerEvents(new Regeneration(), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GameManager(), 0L, 60L); 
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Trails(), 0L, 1L);
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nBOMB LOBBERS has been ENABLED\n\n");
	}
	private void checkSetup() {
		
		
	}
	public void onDisable() {
		saveConfig();
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nBOMB LOBBERS has been DISABLED\n\n");
	}
	public static MainClass getInstance() {
        return instance;
    }

	private void setInstance(MainClass mainClass) {
		instance = mainClass;
	}
	private void loadFiles() {
		getConfig().options().copyDefaults();
		saveConfig();
		SetupYML setup = new SetupYML();
		try {
			setup.initiateFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ((Location) setup.getModifyFile().get("LobbySpawn") == null) {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nPlease set a lobby spawn with /setup lobbyspawn\n\n");
		}
	}
	private void instanceClasses() {
		gameManager = new GameManager();
		regen = new Regeneration();
		tntTrails = new Trails();
	}
	

}
