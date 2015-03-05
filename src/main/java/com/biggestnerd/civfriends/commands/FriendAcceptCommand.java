package com.biggestnerd.civfriends.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.command.PlayerCommand;

import com.biggestnerd.civfriends.CivFriends;
import com.biggestnerd.civfriends.FriendList;
import com.biggestnerd.civfriends.FriendSave;
import com.biggestnerd.civfriends.FriendshipListener;

public class FriendAcceptCommand extends PlayerCommand {

	FriendSave friendsLists = CivFriends.friendSave;
	
	public FriendAcceptCommand(String name) {
		super(name);
		setIdentifier("fa");
		setDescription("Accepts a friend request from a player, or all friend requests if player is not specified");
		setUsage("/fa <playername>");
		setArguments(0,1);
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
		if(args.length == 0) {
			//accept all pending friend requests
			return true;
		}
		if(uuid == null) {
			p.sendMessage(ChatColor.RED + "That player has never played before.");
			return true;
		}
		if(!FriendshipListener.hasInivte(executor, uuid)) {
			p.sendMessage(ChatColor.RED + "That player hasn't sent you a friend request, but you can send them one with /frequest");
			return true;
		}
		FriendList execFriends = friendsLists.getFriendListForPlayer(executor);
		FriendList reqFriends = friendsLists.getFriendListForPlayer(uuid);
		execFriends.addFriend(uuid);
		reqFriends.addFriend(executor);
		FriendshipListener.removeNotification(uuid, executor);
		p.sendMessage(ChatColor.GREEN + args[0] +  " is now your friend!");
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender arg0, String[] arg1) {
		return null;
	}
}
