package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Objects.Groups.Party;

public class PartyManager {

	private static PartyManager manager = new PartyManager();
	private List<Party> paries = new ArrayList<Party>();
	
	private PartyManager() {
		
	}

	public static PartyManager getManager() {
		return manager;
	}

	public List<Party> getParies() {
		return paries;
	}

	public void setParies(List<Party> paries) {
		this.paries = paries;
	}
	
}
