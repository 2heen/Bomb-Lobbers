package me.tsbloxorz.bomblobbers.Game;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.tsbloxorz.bomblobbers.MainClass;

public class PlayerManager  {
	private MainClass plugin = MainClass.getInstance();
	private Player player;
	private boolean isSpectator;
	private String team;
	private double tntVelocity;
	private int tntTicks;
	private String kit;
	private String deathReason;
	private Player killer;
	
	public PlayerManager(Player player, boolean isSpectator) {
		this.player = player;
		this.isSpectator = isSpectator;
		this.team = null;
		tntVelocity = plugin.getConfig().getDouble("Default_TNT_Velocity");
		tntTicks = plugin.getConfig().getInt("Default_TNT_Ticks");
		setKit("jumper");
		setDeathReason("");
		setKiller(null);
	}
	
	
	public Player getPlayer() {
		return player;
	}


	public boolean isSpectator() {
		return isSpectator;
	}

	public void setSpectator(boolean isSpectator) {
		this.isSpectator = isSpectator;
	}
	

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public double getTntVelocity() {
		return tntVelocity;
	}

	public void setTntVelocity(double tntVelocity) {
		this.tntVelocity = tntVelocity;
	}

	public int getTntTicks() {
		return tntTicks;
	}

	public void setTntTicks(int tntTicks) {
		this.tntTicks = tntTicks;
	}
	public String getKit() {
		return kit;
	}
	public void setKit(String kit) {
		this.kit = kit;
	}



	public String getDeathReason() {
		return deathReason;
	}



	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}


	public Player getKiller() {
		return killer;
	}


	public void setKiller(Player killer) {
		this.killer = killer;
	}

}
