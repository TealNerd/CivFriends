package com.biggestnerd.civfriends;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

public class FriendSave {
	
	private double friendDistance = 50D;
	private float maxReductionPercent = 0.35F;
	private float damageReductionScale = 1.09F;
	private int baseDamageReduction = 1;
	private float maxIncreasePercent = 0.2F;
	private float damageIncreaseScale = 1.65F;
	private int baseDamageIncrease = 1;
	private ArrayList<FriendList> friendLists = new ArrayList<FriendList>();
	private boolean reduceDamageTaken = true;
	private boolean increaseDamageDealt = false;
	private boolean preventFriendlyFire = false;
	
	public FriendSave() {
		this.friendLists = new ArrayList<FriendList>();
	}
	
	public FriendList getFriendListForPlayer(UUID player) {
		for(FriendList friends : friendLists) {
			if(friends.getPlayer().equals(player)) {
				return friends;
			}
		}
		FriendList friends = new FriendList(player);
		friendLists.add(friends);
		return friends;
	}
	
	public boolean shouldPreventFriendlyFire() {
		return this.preventFriendlyFire;
	}
	
	public boolean shouldReduceDamageTaken() {
		return this.reduceDamageTaken;
	}
	
	public float getMaxIncreasePercent() {
		return maxIncreasePercent;
	}

	public float getDamageIncreaseScale() {
		return damageIncreaseScale;
	}

	public int getBaseDamageIncrease() {
		return baseDamageIncrease;
	}

	public boolean shouldIncreaseDamageDealt() {
		return this.increaseDamageDealt;
	}
	
	public int getBaseDamageReduction() {
		return this.baseDamageReduction;
	}
	
	public double getFriendDistance() {
		return this.friendDistance;
	}
	
	public float getMaxReductionPercent() {
		return this.maxReductionPercent;
	}
	
	public void addFriendList(FriendList list) {
		this.friendLists.add(list);
	}
	
	public float getDamageReductionScale() {
		return this.damageReductionScale;
	}
	
	public void purgeEmpty() {
		ArrayList<FriendList> tbr = new ArrayList<FriendList>();
		for(FriendList fl : friendLists) {
			if(fl.getFriends().isEmpty()) {
				tbr.add(fl);
			}
		}
		friendLists.removeAll(tbr);
	}
	
	public void save(File file) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			String json = gson.toJson(this);

			FileWriter writer = new FileWriter(file);
			writer.write(json);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FriendSave load(File file) {
		Gson gson = new Gson();
		try {
			return (FriendSave) gson.fromJson(new FileReader(file), FriendSave.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new FriendSave();
	}
}
