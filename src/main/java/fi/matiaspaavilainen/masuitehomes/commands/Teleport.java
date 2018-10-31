package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Teleport implements CommandExecutor {
    private MaSuiteHomes plugin;

    public Teleport(MaSuiteHomes p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if (!(cs instanceof Player)) {
            return false;
        }
        Player p = (Player) cs;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        switch (args.length) {
            case (0):
                if (checkCooldown(p)) {
                    sendLastLoc(p);
                    out.writeUTF("HomeCommand");
                    out.writeUTF(p.getName());
                    out.writeUTF("home");
                    p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                }
                break;
            case (1):
                if (checkCooldown(p)) {
                    sendLastLoc(p);
                    out.writeUTF("HomeCommand");
                    out.writeUTF(p.getName());
                    out.writeUTF(args[0]);
                    p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                }
                break;
            default:
                p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.teleport")));
                break;
        }
        return false;
    }

    private Boolean checkCooldown(Player p) {
        if (plugin.getConfig().getInt("cooldown") > 0) {
            if (MaSuiteHomes.cooldowns.containsKey(p.getUniqueId())) {
                if (System.currentTimeMillis() - MaSuiteHomes.cooldowns.get(p.getUniqueId()) > plugin.getConfig().getInt("cooldown") * 1000) {
                    MaSuiteHomes.cooldowns.remove(p.getUniqueId());
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getMessages().getString("in-cooldown").replace("%time%", plugin.getConfig().getString("cooldown"))));
                    return false;
                }
            } else {
                return true;
            }
        }
        return true;
    }

    private void sendLastLoc(Player p) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteTeleports");
            out.writeUTF("GetLocation");
            out.writeUTF(p.getName());
            Location loc = p.getLocation();
            out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch());
            out.writeUTF("DETECTSERVER");
            p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }
}
