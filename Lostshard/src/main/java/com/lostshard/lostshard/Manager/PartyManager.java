package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Objects.Groups.Party;

public class PartyManager {

	public static PartyManager getManager() {
		return manager;
	}

	private static PartyManager manager = new PartyManager();

	private List<Party> paries = new ArrayList<Party>();

	private PartyManager() {

	}

	public List<Party> getParies() {
		return this.paries;
	}

	public void setParies(List<Party> paries) {
		this.paries = paries;
	}

}
