package com.lostshard.Lostshard.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.Build;
import com.lostshard.Lostshard.Skills.Skill;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Text;

public class SkillCommand {

	static PlayerManager pm = PlayerManager.getManager();
	
	@Command(aliases = { "skills" }, desc = "Skill commands", usage = "<subcommand>")
	public void skills(@Sender Player player, @Sender PseudoPlayer pPlayer, @Text @Optional(value="") String arg) {
		String[] args = arg.split(" ");
		if (args.length == 0) {
			Output.outputSkills(player);
			return;
		} else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reduce")) {
				double amount = 0;
				try {
					amount = Double.parseDouble(args[2]);
				} catch (final Exception e) {
					Output.simpleError(player, "Invalid skill amount, use /skills reduce (skill name) (amount)");
					return;
				}
				final int amountInt = (int) (amount * 10);
				if (amountInt > 0) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					int newSkillAmount = skill.getLvl() - amountInt;
					if (newSkillAmount < 0)
						newSkillAmount = 0;
					skill.setLvl(newSkillAmount);
					Output.positiveMessage(player, "You have reduced your " + skill.getName() + ".");
				} else
					Output.simpleError(player, "Must reduce by at least 1.");

				return;
			} else if (args[0].equalsIgnoreCase("increase")) {
				double amount = 0;
				try {
					amount = Double.parseDouble(args[2]);
				} catch (final Exception e) {
					Output.simpleError(player, "Invalid skill amount, use /skills increase (skill name) (amount)");
					return;
				}
				int amountInt = (int) (amount * 10);
				if (amountInt > pPlayer.getFreeSkillPoints()) {
					Output.simpleError(player, "Not enough free points. Remaining: "
							+ Utils.scaledIntToString(pPlayer.getFreeSkillPoints()));
					return;

				}
				if (amountInt + pPlayer.getCurrentBuild().getTotalSkillVal() > pPlayer.getMaxSkillValTotal()) {
					Output.simpleError(player, "can't increase skills beyond the max total of "
							+ Utils.scaledIntToString(pPlayer.getMaxSkillValTotal()) + ".");
					return;

				}
				if (amountInt > 0) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					final int curSkill = skill.getLvl();
					int newSkill = curSkill + amountInt;
					newSkill = Math.min(1000, newSkill);
					
					amountInt = newSkill-curSkill ;
					
					pPlayer.setFreeSkillPoints(pPlayer.getFreeSkillPoints() - amountInt);
					skill.setLvl(newSkill);
					Output.positiveMessage(player, "You have increased your " + skill.getName() + ".");
					pPlayer.update();
				} else
					Output.simpleError(player, "Must increase by at least 1.");
				return;
			} else if (args[0].equalsIgnoreCase("lock")) {
				if (args.length == 2) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					skill.setLocked(true);
					Output.positiveMessage(player, "You have locked " + skill.getName() + " it should no longer gain.");
				} else
					Output.simpleError(player, "Use \"/skills lock (skill name)\"");
				return;
			} else if (args[0].equalsIgnoreCase("unlock")) {
				if (args.length == 2) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					skill.setLocked(false);
					Output.positiveMessage(player,
							"You have unlocked " + skill.getName() + " it should once again gain.");
				} else
					Output.simpleError(player, "Use \"/skills unlock (skill name)\"");
				return;
			} else if (args[0].equalsIgnoreCase("give") && player.isOp()) {
				if (args.length >= 2 && args[1].equalsIgnoreCase("points"))
					try {
						final Player targetPlayer = Bukkit.getPlayer(args[2]);
						if (targetPlayer == null) {
							Output.simpleError(player, args[2] + " is not online.");
							return;
						}
						pPlayer = pm.getPlayer(targetPlayer);
						final double amount = Double.parseDouble(args[3]);
						final int amountInt = (int) (amount * 10);

						pPlayer.setFreeSkillPoints(pPlayer.getFreeSkillPoints() + amountInt);
						Output.positiveMessage(player,
								"You have increased " + targetPlayer.getName() + " freeskillpoints by " + amount + ".");
						pPlayer.update();
					} catch (final Exception e) {
						Output.simpleError(player, "Invalid skill amount, use /skills give points (target) (amount)");
					}
				else
					try {
						final Player targetPlayer = Bukkit.getPlayer(args[1]);
						pPlayer = pm.getPlayer(targetPlayer);
						final double amount = Double.parseDouble(args[3]);
						int amountInt = (int) (amount * 10);
						if (amountInt + pPlayer.getCurrentBuild().getTotalSkillVal() > pPlayer.getMaxSkillValTotal()) {
							Output.simpleError(player, "can't increase skills beyond the max total of "
									+ Utils.scaledIntToString(pPlayer.getMaxSkillValTotal()) + ".");
							return;

						}
						if (amountInt > 0) {
							final String skillName = args[2];
							final Skill skill = pPlayer.getSkillByName(skillName);
							if (skill == null) {
								Output.simpleError(player, "That skill does not exist.");
								return;
							}
							final int curSkill = skill.getLvl();
							int newSkill = curSkill + amountInt;
							int dif = 0;
							if (newSkill > 1000) {
								dif = newSkill - 1000;
								newSkill = 1000;
							}
							amountInt -= dif;
							skill.setLvl(newSkill);
							Output.positiveMessage(player, "You have increased " + targetPlayer.getName() + " "
									+ skill.getName() + " with " + args[3] + ".");
							pPlayer.update();
						} else
							Output.simpleError(player, "Must increase by at least 1.");

					} catch (final Exception e) {
						Output.simpleError(player, "/skills give (target) (skill) (amount)");
						Output.simpleError(player, "/skills give points (target) (amount)");
					}
				return;
			} else
				Output.simpleError(player, "/skills (reduce|lock|increase)");
			return;
		}
		return;
	}

	@Command(aliases = { "meditate" }, desc = "Regen mana faster")
	public void playerMeditate(@Sender Player player) {
		Output.positiveMessage(player, "You begin meditating...");
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setMeditating(true);
	}

	@Command(aliases = { "rest" }, desc = "Regens stamina faster")
	public void playerRest(@Sender Player player) {
		Output.positiveMessage(player, "You begin resting...");
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setResting(true);
	}

	@Command(aliases = { "resetallskills" }, desc = "Resets all your skills and increase on to level 50", usage = "<skill>")
	public void resetallskills(@Sender Player player, @Optional String name) {
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (name == null) {
			pPlayer.getBuilds().set(pPlayer.getCurrentBuildId(), new Build());
			pPlayer.update();
			Output.positiveMessage(player, "Skills wiped, but you diden chose a skill to increase.");
		} else {
			final Build build = new Build();
			final Skill skill = build.getSkillByName(name);
			if (skill == null) {
				Output.simpleError(player, "You chose a invalid skill to increase.");
				return;
			}
			pPlayer.getBuilds().set(pPlayer.getCurrentBuildId(), build);
			skill.setLvl(500);
			Output.positiveMessage(player, "Skills wiped, " + skill.getName() + " set to 50.0");
		}
		pPlayer.update();
	}
}
