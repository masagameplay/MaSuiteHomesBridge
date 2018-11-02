package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                if (args.length == 0) {
                    out.writeUTF("ListHomeCommand");
                    out.writeUTF(p.getName());
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                } else {
                    p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.list")));
                }

                plugin.in_command.remove(cs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return true;
    }
}
