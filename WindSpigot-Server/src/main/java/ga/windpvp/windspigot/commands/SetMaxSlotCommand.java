package ga.windpvp.windspigot.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.cobblesword.nachospigot.CC;

//[Nacho-0021] Add setMaxPlayers within Bukkit.getServer() and SetMaxSlot Command
public class SetMaxSlotCommand extends Command {
	public SetMaxSlotCommand(String name) {
		super(name);
		this.description = "Set the max slots for the server";
		this.usageMessage = "/sms <amount>";
		this.setAliases(Arrays.asList("smp", "setslots"));
		setPermission("bughaspigot.command.sms");
	}

	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender))
			return true;
		if (args.length != 1) {
			sender.sendMessage(
					"There are currently " + CC.aqua + Bukkit.getMaxPlayers() + CC.reset + " slots!");
			sender.sendMessage(
					ChatColor.RED + "Usage: /setslots <amount>");
			return false;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			sender.sendMessage(ChatColor.RED + "Amount must be an Integer. " + ChatColor.GRAY + "(" + args[0] + ")");
			return false;
		}
		Bukkit.getServer().setMaxPlayers(amount);
		sender.sendMessage("Set player slots to " + ChatColor.AQUA + amount + ChatColor.RESET + ".");
		return false;
	}
}
