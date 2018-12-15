package fi.matiaspaavilainen.masuitehomes.commands;

import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
                cs.sendMessage(plugin.colorize(plugin.config.getMessages().getString("on-active-command")));
                return;
            }

            plugin.in_command.add(cs);

            Player p = (Player) cs;
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                switch (args.length) {
                    case (0):
                        out.writeUTF("DelHomeCommand");
                        out.writeUTF(p.getName());
                        out.writeUTF("home");
                        p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                        break;
                    case (1):
                        out.writeUTF("DelHomeCommand");
                        out.writeUTF(p.getName());
                        out.writeUTF(args[0]);
                        p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                        break;
                    case (2):
                        if (p.hasPermission("masuitehomes.home.delete.other")) {
                            out.writeUTF("DelHomeOtherCommand");
                            out.writeUTF(p.getName());
                            out.writeUTF(args[0]);
                            out.writeUTF(args[1]);
                            p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                        } else {
                            p.sendMessage(plugin.colorize(plugin.config.getMessages().getString("no-permission")));
                        }
                        break;
                    default:
                        p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.delete")));
                        break;
                }

                plugin.in_command.remove(cs);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return true;
    }
}
