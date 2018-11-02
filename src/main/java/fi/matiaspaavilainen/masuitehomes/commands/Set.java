package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Set implements CommandExecutor {

	private MaSuiteHomes plugin;

	public Set(MaSuiteHomes p) {
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
			Location loc = p.getLocation();
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			int max = 0;
			for (PermissionAttachmentInfo permInfo : p.getEffectivePermissions()) {
				String perm = permInfo.getPermission();
				if (perm.startsWith("masuitehomes.home.limit.")) {
					String amount = perm.replace("masuitehomes.home.limit.", "");
					if (amount.equalsIgnoreCase("*")) {
						max = -1;
						break;
					}
					try {
						max = Integer.parseInt(amount);
					} catch (NumberFormatException ex) {
						System.out.println(
								"[MaSuite] [Homes] Please check your home limit permissions (Not a integer or *) ");
					}
				}
			}
			switch (args.length) {
			case (0):
				out.writeUTF("SetHomeCommand");
				out.writeUTF(p.getName());
				out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":"
						+ loc.getYaw() + ":" + loc.getPitch());
				out.writeUTF("home");
				out.writeInt(max);
				p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
				break;
			case (1):
				out.writeUTF("SetHomeCommand");
				out.writeUTF(p.getName());
				out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":"
						+ loc.getYaw() + ":" + loc.getPitch());
				out.writeUTF(args[0]);
				out.writeInt(max);
				p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
				break;
			default:
				p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.set")));
				break;
			}

			plugin.in_command.remove(cs);

		});

		return true;
	}
}
