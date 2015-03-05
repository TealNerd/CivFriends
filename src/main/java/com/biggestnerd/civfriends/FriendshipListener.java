package com.biggestnerd.civfriends;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import vg.civcraft.mc.namelayer.NameAPI;

public class FriendshipListener implements Listener {

	private static HashMap<UUID, List<UUID>> pendingInvites = new HashMap<UUID, List<UUID>>();
	FriendSave config = CivFriends.friendSave;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerDamage(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}	
		Player p = (Player) event.getEntity();
		UUID uuid = NameAPI.getUUID(p.getName());
		if((event.getDamager() instanceof Player) && config.shouldPreventFriendlyFire()) {
			//check if the players are friends and if so cancel the damage
			Player damager = (Player) event.getDamager();
			UUID damagerID = NameAPI.getUUID(damager.getName());
			if(config.getFriendListForPlayer(damagerID).getFriends().contains(uuid)) {
				event.setCancelled(true);
				return;
			}
		}
		if(!config.shouldIncreaseDamageDealt() && !config.shouldReduceDamageTaken()) {
			//well we don't need to do anything i guess
			return;
		}
		int nearbyFriends = getNearbyFriends(uuid, p.getLocation());
		double initialDamage = event.getDamage();
		double totalIncrease = 0;
		double totalReduction = 0;
		if(config.shouldReduceDamageTaken()) {
			float reductionPercentage = 0F;
			for(int i = 0; i < nearbyFriends; i++) {
				reductionPercentage += config.getBaseDamageReduction() / Math.pow(i, config.getDamageReductionScale());
			}
			reductionPercentage /= 10;
			reductionPercentage = cutDecimals(reductionPercentage);
			totalReduction = initialDamage * Math.min(reductionPercentage, config.getMaxReductionPercent());
		}
		if(config.shouldIncreaseDamageDealt()) {
			float increasePercentage = 0F;
			for(int i = 0; i < nearbyFriends; i++) {
				increasePercentage += config.getBaseDamageIncrease() / Math.pow(i, config.getDamageIncreaseScale());
			}
			increasePercentage /= 10;
			increasePercentage = cutDecimals(increasePercentage);
			totalIncrease = initialDamage * Math.min(increasePercentage, config.getMaxIncreasePercent());
		}
		double finalDamage = initialDamage - totalReduction + totalIncrease;
		event.setDamage(finalDamage);
	}
	
	@EventHandler
	public void playerLogin(PlayerJoinEvent event) {
		if(pendingInvites.containsKey(event.getPlayer().getUniqueId()) && !pendingInvites.get(event.getPlayer().getUniqueId()).isEmpty()) {
			StringBuilder out = new StringBuilder("You were invited to be friends with the following players while offline: ");
			for(UUID id : pendingInvites.get(event.getPlayer().getUniqueId())) {
				out.append(NameAPI.getCurrentName(id) + ", ");
			}
			String msg = out.toString();
			event.getPlayer().sendMessage(ChatColor.YELLOW + msg.substring(0,  msg.length() - 2));
		}
	}
	
	public static boolean hasInivte(UUID invitee, UUID inviter) {
		if(!pendingInvites.containsKey(invitee)) {
			pendingInvites.put(invitee, new ArrayList<UUID>());
		}
		if(pendingInvites.get(invitee).contains(inviter)) {
			return true;
		} 
		return false;
	}
	
	public static void removeNotification(UUID invitee, UUID inviter) {
		if(!pendingInvites.containsKey(invitee)) {
			pendingInvites.put(invitee, new ArrayList<UUID>());
		}
		pendingInvites.get(invitee).removeAll(Collections.singleton(inviter));
	}
	
	public static void addNotification(UUID invitee, UUID inviter) {
		if(!pendingInvites.containsKey(invitee)) {
			pendingInvites.put(invitee, new ArrayList<UUID>());
		}
		pendingInvites.get(invitee).add(inviter);
	}
	
	public float cutDecimals(float f) {
		DecimalFormat twoDForm = new DecimalFormat("#.#");
		return Float.valueOf(twoDForm.format(f));
	}
	
	private int getNearbyFriends(UUID uuid, Location loc) {
		int nearby = 0;
		FriendList friends = config.getFriendListForPlayer(uuid);
		for(UUID friendID : friends.getFriends()) {
			if(Bukkit.getOfflinePlayer(friendID).isOnline()) {
				Player friend = Bukkit.getPlayer(friendID);
				if(friend.getLocation().distance(loc) <= config.getFriendDistance())	{
					nearby++;
				}
			}
		}
		return nearby;
	}
}
