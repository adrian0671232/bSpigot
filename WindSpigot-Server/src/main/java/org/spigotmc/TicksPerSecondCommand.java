package org.spigotmc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

public class TicksPerSecondCommand extends Command {

	public TicksPerSecondCommand(String name) {
		super(name);
		this.description = "Gets the current ticks per second for the server";
		this.usageMessage = "/tps";
		this.setPermission("bukkit.command.tps");
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender)) {
			return true;
		}

		// PaperSpigot start - Further improve tick handling
		double[] tps = org.bukkit.Bukkit.spigot().getTPS();
		String[] tpsAvg = new String[tps.length];

		for (int i = 0; i < tps.length; i++) {
			tpsAvg[i] = format(tps[i]);
		}
		
		// WindSpigot - more detailed tps cmd
		
		int entityCount = 0;
		
		for (WorldServer world : MinecraftServer.getServer().worlds) {
			entityCount = entityCount + world.entityList.size();
		}
		
		int tileEntityCount = 0;
		
		for (WorldServer world : MinecraftServer.getServer().worlds) {
			tileEntityCount = tileEntityCount + world.tileEntityList.size();
		}
		
		sender.sendMessage(ChatColor.AQUA + "Performance:");
		sender.sendMessage("TPS (1m, 5m, 15m): "
				+ org.apache.commons.lang.StringUtils.join(tpsAvg, ", "));
		sender.sendMessage("Memory: " + ChatColor.GREEN
				+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/"
				+ (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " MB " + ChatColor.GRAY + "(Max: "
				+ (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB)");
		sender.sendMessage("Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size()  + "/" + Bukkit.getMaxPlayers());
		sender.sendMessage("Entities: " + ChatColor.GREEN + entityCount + ChatColor.GRAY + " (" + tileEntityCount + " Tile Entities)");
		sender.sendMessage("Last Tick Time: " + ChatColor.GREEN + Math.round(MinecraftServer.getServer().getLastMspt() * 100.0) / 100.0 + "ms");
		
		return true;
	}

	private static String format(double tps) // PaperSpigot - made static
	{
		return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
				+ ((tps > 21.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0); // Paper - only print * at 21, we commonly peak to 20.02 as the tick sleep is not accurate enough, stop the noise
	}
}
