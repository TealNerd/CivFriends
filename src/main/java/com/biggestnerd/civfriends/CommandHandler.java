package com.biggestnerd.civfriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import vg.civcraft.mc.namelayer.command.Command;

import com.biggestnerd.civfriends.commands.FriendAcceptCommand;
import com.biggestnerd.civfriends.commands.FriendDeleteCommand;
import com.biggestnerd.civfriends.commands.FriendListCommand;
import com.biggestnerd.civfriends.commands.FriendRequestCommand;

public class CommandHandler {

	public Map<String, Command> commands = new HashMap<String, Command>();
	
	public void registerCommands() {
		addCommand(new FriendAcceptCommand("fa"));
		addCommand(new FriendDeleteCommand("fd"));
		addCommand(new FriendListCommand("fl"));
		addCommand(new FriendRequestCommand("fr"));
		
	}
	
	public void addCommand(Command command) {
		commands.put(command.getIdentifier().toLowerCase(), command);
	}
	
	/**
	 * Is called when a command is executed.  Should not be touched by any outside
	 * plugin.
	 * @param sender
	 * @param cmd
	 * @param args
	 * @return
	 */
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args){
		if (commands.containsKey(cmd.getName().toLowerCase())){
			Command command = commands.get(cmd.getName().toLowerCase());
			if (args.length < command.getMinArguments() || args.length > command.getMaxArguments()){
				helpPlayer(command, sender);
				return true;
			}
			command.execute(sender, args);
			CivFriends.friendSave.purgeEmpty();
			CivFriends.saveFriendship();
		}
		return true;
	}

	/**
	 * Is called when a tab is pressed.  Should not be touched by any outside
	 * plugin.
	 * @param sender
	 * @param cmd
	 * @param args
	 * @return
	 */

	public List<String> complete(CommandSender sender, org.bukkit.command.Command cmd, String[] args){
		if (commands.containsKey(cmd.getName().toLowerCase())){
			Command command = commands.get(cmd.getName().toLowerCase());
			return command.tabComplete(sender, args);
		}
		return null;
	}
	/**
	 * Sends a player help message.
	 * @param The Command that was executed.
	 * @param The CommandSender who executed the command.
	 */
	public void helpPlayer(Command command, CommandSender sender){
		sender.sendMessage(new StringBuilder().append(ChatColor.RED + "Command: " ).append(command.getName()).toString());
		sender.sendMessage(new StringBuilder().append(ChatColor.RED + "Description: " ).append(command.getDescription()).toString());
		sender.sendMessage(new StringBuilder().append(ChatColor.RED + "Usage: ").append(command.getUsage()).toString());
	}
}
