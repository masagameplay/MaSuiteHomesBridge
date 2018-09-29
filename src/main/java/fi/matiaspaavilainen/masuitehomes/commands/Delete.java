package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Delete implements CommandExecutor {

    private MaSuiteHomes plugin;

    public Delete(MaSuiteHomes p){
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
        return false;
    }
}
