package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Delete implements CommandExecutor {

	private MaSuiteHomes plugin;

	public Delete(MaSuiteHomes p) {
		plugin = p;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
		if (!(cs instanceof Player)) {
			return false;
		}

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			if (plugin.in_command.contains(cs)) { // this function is not really necessary, but safety first
				cs.sendMessage(plugin.colorize(plugin.config.getMessages().getString("on_active_command")));
				return;
			}

			plugin.in_command.add(cs);

			Player p = (Player) cs;
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			switch (args.length) {
			case (0):
				out.writeUTF("DelHomeCommand");
				out.writeUTF(p.getName());
				out.writeUTF("home");
				p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
				break;
			case (1):
				out.writeUTF("DelHomeCommand");
				out.writeUTF(p.getName());
				out.writeUTF(args[0]);
				p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
				break;
			default:
				p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.delete")));
				break;
			}

			plugin.in_command.remove(cs);

		});

		return true;
	}
}
