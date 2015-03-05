package com.biggestnerd.civfriends;

import java.util.ArrayList;
import java.util.UUID;

public class FriendList {

	private UUID playerUUID;
	private ArrayList<UUID> friends = new ArrayList<UUID>();

	public FriendList(UUID player) {
		this.playerUUID = player;
	}
	
	public UUID getPlayer() {
		return this.playerUUID;
	}
	
	public void addFriend(UUID friend) {
		this.friends.add(friend);
	}
	
	public ArrayList<UUID> getFriends() {
		return this.friends;
	}
}
