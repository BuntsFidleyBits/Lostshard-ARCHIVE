package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.lostshard.Lostshard.Manager.SpellManager;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Serializer;

@Embeddable
public class SpellBook {

	@Transient
	SpellManager sm = SpellManager.getManager();
	
	@ElementCollection
	@CollectionTable
	private final List<Scroll> spells = new ArrayList<Scroll>();

	public void addSpell(Scroll spell) {
		if (!this.spells.contains(spell))
			this.spells.add(spell);
	}

	public boolean containSpell(Scroll spellType) {
		return this.spells.contains(spellType);
	}

	public List<Scroll> getSpells() {
		return this.spells;
	}

	public ArrayList<Scroll> getSpellsOnPage(int pageNumber) {
		final ArrayList<Scroll> spellsOnPage = new ArrayList<Scroll>();
		for (final Scroll scroll : this.spells)
			if (scroll.getPage() == pageNumber)
				spellsOnPage.add(scroll);
		return spellsOnPage;
	}

	public String toJson() {
		final List<String> tjson = new ArrayList<String>();
		for (final Scroll s : this.spells)
			tjson.add(s.getName());
		return Serializer.serializeStringArray(tjson);
	}
}
