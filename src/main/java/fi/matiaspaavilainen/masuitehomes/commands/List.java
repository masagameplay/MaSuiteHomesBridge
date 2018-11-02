package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class List implements CommandExecutor {
	private MaSuiteHomes plugin;

	public List(MaSuiteHomes p) {
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
			if (args.length == 0) {
				out.writeUTF("ListHomeCommand");
				out.writeUTF(p.getName());
				p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
			} else {
				p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.list")));
			}

			plugin.in_command.remove(cs);

		});

		return true;
	}
}
