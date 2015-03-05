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

public class FriendListCommand extends PlayerCommand {

	public FriendListCommand(String name) {
		super(name);
		setIdentifier("fl");
		setDescription("Lists all your fwends");
		setUsage("/fl");
		setArguments(0,0);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Sorry, only players can have friends");
			return true;
		}
		StringBuilder out = new StringBuilder("You are friends with the following players: ");
		Player p = (Player) sender;
		UUID uuid = NameAPI.getUUID(p.getName());
		FriendList friends = CivFriends.friendSave.getFriendListForPlayer(uuid);
		if(friends.getFriends().isEmpty() && friends.getFriends().size() == 0) {
			sender.sendMessage(ChatColor.RED + "You have no friends :/");
			return true;
		}
		for(UUID id : friends.getFriends()) {
			out.append(NameAPI.getCurrentName(id) + ", ");
		}
		String msg = out.toString();
		sender.sendMessage(ChatColor.GREEN + msg.substring(0, msg.length() - 2));
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender arg0, String[] arg1) {
		return null;
	}

}
