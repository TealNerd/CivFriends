package com.biggestnerd.civfriends;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CivFriends extends JavaPlugin {

	static Logger logger;
	private static File saveFile;
	public static FriendSave friendSave;
	private CommandHandler cHandler;
	private static CivFriends instance;
	
	@Override
	public void onEnable() {
		instance = this;
		logger = getLogger();
		if (!Bukkit.getPluginManager().isPluginEnabled("NameLayer")){
			Log("Frienship wont work without NameLayer :(");
			this.getPluginLoader().disablePlugin(this); // shut down
		}
		initSave();
		registerCommands();
		registerListener();
	}
	
	@Override
	public void onDisable() {
		saveFriendship();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return cHandler.execute(sender, cmd, args);
	}
	
	public void initSave() {
		//getLogger().log(Level.INFO, this.getDataFolder().getAbsolutePath());
		File pluginDir = getDataFolder();
		if(!pluginDir.isDirectory()) {
			pluginDir.mkdir();
		}
		saveFile = new File(pluginDir, "friendsList.json");
		if(!saveFile.isFile()) {
			try {
				saveFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			friendSave = new FriendSave();
			friendSave.save(saveFile);
		} else {
			friendSave = FriendSave.load(saveFile);
			if(friendSave == null) {
				friendSave = new FriendSave();
			}
			friendSave.save(saveFile);
		}
	}
	
	public static void Log(String message){
		logger.log(Level.INFO, "[CivFriends] " + message);
	}
	
	public void registerListener() {
		getServer().getPluginManager().registerEvents(new FriendshipListener(), this);
	}
	
	public void registerCommands() {
		cHandler = new CommandHandler();
		cHandler.registerCommands();
	}
	
	public static void saveFriendship() {
		friendSave.save(saveFile);
	}
}
