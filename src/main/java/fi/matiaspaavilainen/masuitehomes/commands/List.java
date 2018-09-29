package fi.matiaspaavilainen.masuitehomes.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;
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
        Player p = (Player) cs;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        if(args.length == 0){
            out.writeUTF("ListHomeCommand");
            out.writeUTF(p.getName());
            p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }else{
            p.sendMessage(plugin.colorize(plugin.config.getSyntaxes().getString("home.list")));
        }
        return false;
    }
}
