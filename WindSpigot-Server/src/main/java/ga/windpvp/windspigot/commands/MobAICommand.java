package ga.windpvp.windspigot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

// Implements a Mob AI toggle command
public class MobAICommand extends Command {
	
	private boolean globalAI = true;

	public MobAICommand(String name) {
		super(name);
		this.description = "Toggles Mob AI";
		this.usageMessage = "/mobai";
		this.setPermission("bughaspigot.command.mobai");
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender)) {
			return true;
		}

		globalAI = !globalAI;
		
		for (WorldServer world : MinecraftServer.getServer().worlds) {
			world.nachoSpigotConfig.enableMobAI = globalAI;
		}
		
		String status = globalAI ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled";
		sender.sendMessage("Mob AI is now " + status + ChatColor.RESET + " in all worlds.");

		return true;
	}

}
