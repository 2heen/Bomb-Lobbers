package me.tsbloxorz.bomblobbers.Kits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.tsbloxorz.bomblobbers.MainClass;
import me.tsbloxorz.bomblobbers.Game.PlayerManager;

public class Kits {
	private MainClass plugin = MainClass.getInstance();
	
	public void giveKits() {
		for (Player key : plugin.playerManager.keySet()) {
			PlayerManager x = plugin.playerManager.get(key);
			if (!x.isSpectator()) {
				if (x.getKit().equals("jumper")) {
					giveJumper(x.getPlayer());
				} else if (x.getKit().equals("armorer")) {
					giveArmorer(x.getPlayer());
				} else if (x.getKit().equals("clocksman")) {
					giveClocksman(x.getPlayer());
				} else if (x.getKit().equals("pitcher")) {
					givePitcher(x.getPlayer());
				}
			}
		}
		
	}
	private void giveClocksman(Player player) {
		ItemStack[] armor = new ItemStack[4];
		armor[0] = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
		armor[1] = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
		armor[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
		armor[3] = new ItemStack(Material.CHAINMAIL_HELMET, 1);
		 
		player.getInventory().setArmorContents(armor);
		player.getInventory().addItem(new ItemStack(Material.STONE_BUTTON, 1));
		
	}
	private void givePitcher(Player player) {
		ItemStack[] armor = new ItemStack[4];
		armor[0] = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
		armor[1] = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
		armor[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
		armor[3] = new ItemStack(Material.CHAINMAIL_HELMET, 1);
		 
		player.getInventory().setArmorContents(armor);
		player.getInventory().addItem(new ItemStack(Material.LEVER, 1));
		
	}
	private void giveArmorer(Player player) {
		ItemStack[] armor = new ItemStack[4];
		armor[0] = new ItemStack(Material.GOLD_BOOTS, 1);
		armor[1] = new ItemStack(Material.GOLD_LEGGINGS, 1);
		armor[2] = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		armor[3] = new ItemStack(Material.GOLD_HELMET, 1);
		 
		player.getInventory().setArmorContents(armor);
		
	}
	private void giveJumper(Player player) {
		//give armor
		ItemStack[] armor = new ItemStack[4];
		armor[0] = new ItemStack(Material.LEATHER_BOOTS, 1);
		armor[1] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		armor[2] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		armor[3] = new ItemStack(Material.LEATHER_HELMET, 1);
		//dye the armor
		for (int i = 0; i < armor.length; i++) {
			LeatherArmorMeta meta = (LeatherArmorMeta) armor[i].getItemMeta();
			PlayerManager x = plugin.playerManager.get(player);
			if (x.getTeam().equalsIgnoreCase("blue")) {
				meta.setColor(Color.BLUE);
			} else if (x.getTeam().equalsIgnoreCase("red")) {
				meta.setColor(Color.RED);
			}
			armor[i].setItemMeta(meta);
		}
		 
		player.getInventory().setArmorContents(armor);
		
	}
	

}
