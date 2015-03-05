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

public class FriendDeleteCommand extends PlayerCommand {

	public FriendDeleteCommand(String name) {
		super(name);
		setIdentifier("fd");
		setDescription("Delets a players as your friend");
		setUsage("/fd <playername>");
		setArguments(1,1);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players can have fwends");
			return true;
		}
		Player p = (Player) sender;
		UUID executor = NameAPI.getUUID(p.getName());
		UUID uuid = NameAPI.getUUID(args[0]);
		if(uuid == null) {
			p.sendMessage(ChatColor.RED + "That player has never played before.");
			return true;
		}
		FriendList friends = CivFriends.friendSave.getFriendListForPlayer(executor);
		if(friends.getFriends().isEmpty()) {
			p.sendMessage(ChatColor.RED + "You don't have any friends to delete");
			return true;
		}
		friends.getFriends().remove(uuid);
		CivFriends.friendSave.getFriendListForPlayer(uuid).getFriends().remove(executor);
		p.sendMessage(ChatColor.GREEN + "You successfully deleted " + args[0] + " as a friend");
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender arg0, String[] arg1) {
		return null;
	}

}
