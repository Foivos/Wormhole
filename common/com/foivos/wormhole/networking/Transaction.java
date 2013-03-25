package com.foivos.wormhole.networking;

import java.util.PriorityQueue;

public class Transaction implements Comparable<Transaction>{

	public Transaction(NetworkedInventory origin, NetworkedInventory target, int slot, int weight, int distance) {
		this.origin = origin;
		this.target = target;
		this.slot = slot;
		this.weight = weight;
		this.distance = distance;
	}
	
	public NetworkedInventory origin, target;
	/**
	 * The slot of origin to perform the transaction with.
	 * Target has to be inspected.
	 */
	public int slot;
	/**
	 * -1: from target to origin.
	 * 1: from origin to target;
	 */
	public int weight;
	public int distance;
	
	@Override
	public int compareTo(Transaction o) {
		if(weight == o.weight)
			return this.distance - o.distance;
		return Math.abs(this.weight) - Math.abs(o.weight);
	}

}
