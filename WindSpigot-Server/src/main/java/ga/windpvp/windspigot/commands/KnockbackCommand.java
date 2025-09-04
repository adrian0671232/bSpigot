package ga.windpvp.windspigot.commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.cobblesword.nachospigot.knockback.KnockbackProfile;
import ga.windpvp.windspigot.commons.ClickableBuilder;
import ga.windpvp.windspigot.knockback.CraftKnockbackProfile;
import ga.windpvp.windspigot.knockback.KnockbackConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class KnockbackCommand extends Command {

	private final String separator = "";

	public KnockbackCommand(String name) {
		super(name);
		this.description = "Assists in knockback configuration.";
		this.setPermission("windspigot.command.knockback");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender) || !(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;

		switch (args.length) {
            case 1: {
                if (args[0].toLowerCase().equalsIgnoreCase("help")) {
                    player.sendMessage("§bKnockback Help:\n"
                    + "§f/kb §7-§e View all knockback profiles\n"
                    + "§f/kb create §6<name> §7-§e Create a knockback profile\n"
                    + "§f/kb delete §6<name> §7-§e Delete an existing knockback profile\n"
                    + "§f/kb load §6<name> §7-§e Load a knockback profile for everyone\n"
                    + "§f/kb view §6<name> §7-§e View a knockback profile's values\n"
                    + "§f/kb projectile §6<name> §7-§e View a knockback profile's projectile values\n"
                    + "§f/kb set §6<name> §7-§e Set a specific player to have a specific knockback profile\n"
                    + "\n§7/kb help to view this message");
                }
                break;
            }
		case 2: {
			switch (args[0].toLowerCase()) {
			case "create": {
				if (!isProfileName(args[1])) {
					CraftKnockbackProfile profile = new CraftKnockbackProfile(args[1]);
					KnockbackConfig.getKbProfiles().add(profile);
					profile.save();
					knockbackCommandMain(player);
					player.sendMessage("§aThe profile §e" + args[1] + " §ahas been created.");
					return true;
				} else {
					player.sendMessage("§cA knockback profile with that name already exists.");
				}
				break;
			}
			case "delete": {
				if (KnockbackConfig.getCurrentKb().getName().equalsIgnoreCase(args[1])) {
					knockbackCommandMain(player);
					player.sendMessage("§cYou cannot delete the profile that is being used.");
					return false;
				}
				if (KnockbackConfig.getKbProfiles().removeIf(profile -> profile.getName().equalsIgnoreCase(args[1]))) {
					KnockbackConfig.set("knockback.profiles." + args[1], null);
					knockbackCommandMain(player);
					player.sendMessage("§aThe profile §e" + args[1] + " §ahas been removed.");
					return true;
				} else {
					player.sendMessage("§cThis profile doesn't exist.");
				}
				break;
			}
			case "load": {
				KnockbackProfile profile = KnockbackConfig.getKbProfileByName(args[1]);
				if (profile != null) {
					if (KnockbackConfig.getCurrentKb().getName().equalsIgnoreCase(args[1])) {
						player.sendMessage("§cThis profile is loaded.");
						return false;
					}
					KnockbackConfig.setCurrentKb(profile);
					KnockbackConfig.set("knockback.current", profile.getName());
					KnockbackConfig.save();
					knockbackCommandMain(player);
					player.sendMessage("§aThe profile §e" + args[1] + " §ahas been loaded.");
					return true;
				} else {
					player.sendMessage("§cThis profile doesn't exist.");
				}
				break;
			}
			case "view": {
				KnockbackProfile profile = KnockbackConfig.getKbProfileByName(args[1]);
				if (profile != null) {
					knockbackCommandView(player, profile);
					return true;
				}
				player.sendMessage("§cThis profile doesn't exist.");
				break;
			}
			case "projectile": {
				KnockbackProfile profile = KnockbackConfig.getKbProfileByName(args[1]);
				if (profile != null) {
					knockbackCommandViewProjectiles(player, profile);
					return true;
				}
				player.sendMessage("§cThis profile doesn't exist.");
				break;
			}
			default: {
				knockbackCommandMain(player);
			}
			}
			break;
		}
		case 3: {
			switch (args[0].toLowerCase()) {
			case "set": {
				KnockbackProfile profile = KnockbackConfig.getKbProfileByName(args[1]);
				if (profile == null) {
					sender.sendMessage("§cA profile with that name could not be found.");
					return false;
				}
				Player target = Bukkit.getPlayer(args[2]);
				if (target == null) {
                    sender.sendMessage("§cCould not find the player '§e" + args[0] + "§c' on the server.");
					return false;
				}
				target.setKnockbackProfile(profile);
				break;
			}
			}
			break;
		}
		case 4: {
			if ("edit".equalsIgnoreCase(args[0])) {
				KnockbackProfile profile = KnockbackConfig.getKbProfileByName(args[1].toLowerCase());
				if (profile == null) {
					player.sendMessage("§cThis profile doesn't exist.");
					return false;
				}
				switch (args[2].toLowerCase()) {
				case "friction-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setFrictionHorizontal(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "friction-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setFrictionVertical(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setHorizontal(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setVertical(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "extra-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						sender.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setExtraHorizontal(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "extra-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setExtraVertical(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "vertical-max": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setVerticalMax(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "vertical-min": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setVerticalMin(value);
					profile.save();
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "stop-sprint": {
					if ("true".equalsIgnoreCase(args[3]) || "false".equalsIgnoreCase(args[3])) {
						profile.setStopSprint(Boolean.parseBoolean(args[3]));
						profile.save();
						knockbackCommandView(player, profile);
						player.sendMessage("§aValue edited and saved.");
						return true;
					} else {
						player.sendMessage("§4" + args[3] + " §cis not a boolean.");
					}
					break;
				}
				case "rod-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setRodHorizontal(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "rod-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setRodVertical(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "arrow-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setArrowHorizontal(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "arrow-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setArrowVertical(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "pearl-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setPearlHorizontal(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "pearl-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setPearlVertical(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "snowball-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setSnowballHorizontal(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "snowball-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setSnowballVertical(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "egg-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setEggHorizontal(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "egg-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setEggVertical(value);
                    profile.save(true);
					knockbackCommandViewProjectiles(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "wtap-extra-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setWTapExtraHorizontal(value);
				
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "wtap-extra-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setWTapExtraVertical(value);
					
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "add-horizontal": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setAddHorizontal(value);
					
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				case "add-vertical": {
					if (!NumberUtils.isNumber(args[3])) {
						player.sendMessage("§cAmount must be an Integer. " + "§7(INVALID: " + args[3] + ")");
						return false;
					}
					double value = Double.parseDouble(args[3]);
					profile.setAddVertical(value);
					
					knockbackCommandView(player, profile);
					player.sendMessage("§aValue edited and saved.");
					break;
				}
				}
			}
			break;
		}
		default: {
			knockbackCommandMain(player);
		    }
		}
		return false;
	}

	private void knockbackCommandMain(Player player) {
		player.sendMessage(separator + "\n" + "§6Knockback Profiles " + "§7(" + KnockbackConfig.getKbProfiles().size() + " profiles):" + "\n");
		for (KnockbackProfile profile : KnockbackConfig.getKbProfiles()) {
			boolean current = KnockbackConfig.getCurrentKb().getName().equals(profile.getName());

			TextComponent line = new ClickableBuilder("§e[SET] ")
					.setHover("§eClick to apply this profile to a player.")
					.setClick("/kb set " + profile.getName() + " ", ClickEvent.Action.SUGGEST_COMMAND).build();

            TextComponent edit = new ClickableBuilder("§f " + profile.getName() + " ")
                    .setHover("§aClick to edit §e" + profile.getName() + "§a.")
                    .setClick("/kb view " + profile.getName(), ClickEvent.Action.RUN_COMMAND).build();

			TextComponent load = new ClickableBuilder(current ? "§a[LOADED] " : "§7[LOAD] ")
					.setHover(current ? "§aThis profile is already loaded." : "§eClick here to load this profile.")
					.setClick("/kb load " + profile.getName(), ClickEvent.Action.RUN_COMMAND).build();

			TextComponent delete = new ClickableBuilder("§c[DELETE]")
					.setHover("§c§lClick to permanently delete this profile.")
					.setClick("/kb delete " + profile.getName(), ClickEvent.Action.RUN_COMMAND).build();

			player.spigot().sendMessage(edit, line, load, delete);
		}

		player.spigot()
				.sendMessage(new ClickableBuilder("\n§a§lCREATE")
						.setHover("§aClick to create a new profile.")
						.setClick("/kb create ", ClickEvent.Action.SUGGEST_COMMAND).build());
		player.sendMessage(separator);
	}

	private void knockbackCommandView(Player player, KnockbackProfile profile) {
		player.sendMessage(separator + "\n§b" + profile.getName() + ":" + "\n");
		for (String values : profile.getKnockbackValues()) {
			TextComponent value = new TextComponent("§7 * §f" + values);
			TextComponent edit = new ClickableBuilder(" §e[EDIT]")
					.setHover("Click to edit §e" + values + "§f.")
					.setClick("/kb edit " + profile.getName() + " " + values.replace("§7: ", " "),
							ClickEvent.Action.SUGGEST_COMMAND)
					.build();
			player.spigot().sendMessage(value, edit);
		}
		TextComponent page = new ClickableBuilder("\n§c[BACK]").setHover("§eClick to view all profiles.")
				.setClick("/kb", ClickEvent.Action.RUN_COMMAND).build();
		TextComponent projectiles = new ClickableBuilder(" §a[PROJECTILES]")
				.setClick("/kb projectile " + profile.getName(), ClickEvent.Action.RUN_COMMAND)
				.setHover("§eClick to edit projectile values.").build();
		player.spigot().sendMessage(page, projectiles);
		player.sendMessage(separator);
	}

	private void knockbackCommandViewProjectiles(Player player, KnockbackProfile profile) {
		player.sendMessage(separator + "\n§b" + profile.getName() + "§7 (Projectiles):" + "\n");
		for (String values : profile.getProjectilesValues()) {
			TextComponent value = new TextComponent("§7 * §f" + values);
			TextComponent edit = new ClickableBuilder(" §e[EDIT]")
                    .setHover("Click to edit §e" + values + "§f.")
					.setClick("/kb edit " + profile.getName() + " " + values.replace("§7: ", " "),
							ClickEvent.Action.SUGGEST_COMMAND)
					.build();
			player.sendMessage(value, edit);
		}
        TextComponent page = new ClickableBuilder("\n§c[BACK]").setHover("§eClick to view all profiles.")
                .setClick("/kb", ClickEvent.Action.RUN_COMMAND).build();
        TextComponent projectiles = new ClickableBuilder(" §a[KNOCKBACK]")
                .setClick("/kb view " + profile.getName(), ClickEvent.Action.RUN_COMMAND)
                .setHover("§eClick to edit projectile values.").build();
        player.spigot().sendMessage(page, projectiles);
		player.sendMessage(separator);
	}

	private boolean isProfileName(String name) {
		for (KnockbackProfile profile : KnockbackConfig.getKbProfiles()) {
			if (profile.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}
