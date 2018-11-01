package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        Player p = (Player) cs;
        Location loc = p.getLocation();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        int max = 0;
        for (int i = 100; 0 < i; i++) {
            if (p.hasPermission("masuitehomes.home.limit." + i) || p.hasPermission("masuitehomes.home.limit.unlimited")) {
                max = i - 1;
                break;
            }
        }
        switch (args.length) {
            case (0):
                out.writeUTF("SetHomeCommand");
                out.writeUTF(p.getName());
                out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch());
                out.writeUTF("home");
                out.writeInt(max);
                p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                break;
            case (1):
                out.writeUTF("SetHomeCommand");
                out.writeUTF(p.getName());
                out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch());
                out.writeUTF(args[0]);
                out.writeInt(max);
                p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                break;
            default:
                p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.set")));
                break;
        }
        return false;
    }
}
