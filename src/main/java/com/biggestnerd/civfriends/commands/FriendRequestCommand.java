package com.biggestnerd.civfriends.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.command.PlayerCommand;

import com.biggestnerd.civfriends.CivFriends;
import com.biggestnerd.civfriends.FriendList;
import com.biggestnerd.civfriends.FriendSave;
import com.biggestnerd.civfriends.FriendshipListener;

public class FriendRequestCommand extends PlayerCommand {

	FriendSave friendsLists = CivFriends.friendSave;
	
	public FriendRequestCommand(String name) {
		super(name);
		setIdentifier("fr");
		setDescription("Sends a friend request to a player");
		setUsage("/fr <playername>");
		setArguments(1,1);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage("Must be a player to perform this command.");
			return true;
		}
		Player p = (Player) sender;
		UUID executor = NameAPI.getUUID(p.getName());
		UUID uuid = NameAPI.getUUID(args[0]);
		if(uuid == null) {
			p.sendMessage(ChatColor.RED + "That player has never played before.");
			return true;
		}
		FriendList friends = friendsLists.getFriendListForPlayer(executor);
		if(friends.getFriends().contains(uuid)) {
			p.sendMessage(ChatColor.RED + "That player is already your friend!");
			return true;
		}
		if(FriendshipListener.hasInivte(uuid, executor)) {
			p.sendMessage(ChatColor.RED + "You already invited that player to be your friend");
			return true;
		}
		if(FriendshipListener.hasInivte(executor, uuid)) {
			p.sendMessage(ChatColor.YELLOW + "That player already sent you a friend request, use /faccept to accept their request");
			return true;
		}
		OfflinePlayer invitee = Bukkit.getOfflinePlayer(uuid);
		if(invitee.isOnline()) {
			Player invitedPlayer = (Player) invitee;
			FriendshipListener.addNotification(uuid, executor);
			p.sendMessage(ChatColor.GREEN + "Your friend request has been sent!");
			invitedPlayer.sendMessage(ChatColor.GREEN + p.getName() +  " has sent you a friend request, use /faccept to accept their request");
		} else {
			FriendshipListener.addNotification(uuid, executor);
			p.sendMessage(ChatColor.GREEN + "Your friend request has been sent!");
		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender arg0, String[] arg1) {
		return null;
	}
}
