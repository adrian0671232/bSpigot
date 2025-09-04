package ga.windpvp.windspigot.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobCommand extends Command {
	public SpawnMobCommand(String name) {
		super(name);
		this.description = "Spawn mobs";
		this.usageMessage = "/spawnmob <mob name> <amount>";
		setPermission("bughaspigot.command.spawnmob");
	}

	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender))
			return true;
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Player only.");
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /spawnmob <mob name> <amount>");
			return false;
		}

		int amount = 1;
		Player player = (Player) sender;
		Location location = player.getLocation();

		EntityType entityType;
		try {
			entityType = EntityType.valueOf(args[0]);
		} catch (IllegalArgumentException ex) {
			StringBuilder entityListStr = new StringBuilder();
			for (EntityType entityVal : EntityType.values())
				entityListStr.append(ChatColor.GREEN).append(entityVal.name()).append(ChatColor.WHITE).append(", ");
			entityListStr.setLength(entityListStr.length() - 2);

			player.sendMessage(ChatColor.RED + "Unknown entity type of '" + ChatColor.YELLOW + args[0] + ChatColor.RED + "'.");
			player.sendMessage(entityListStr.toString());
			return false;
		}

		if (args.length == 2) {
			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception ex) {
                player.sendMessage(ChatColor.RED + "Amount must be an Integer. " + ChatColor.GRAY + "(INVALID: " + args[1] + ")");
				return false;
			}
		}

		for (int i = 0; i < amount; i++)
			location.getWorld().spawnEntity(location, entityType);

		player.sendMessage(ChatColor.GREEN + "Spawned " + amount + "x " + entityType.name() + ".");
		return false;
	}
}