package fi.matiaspaavilainen.masuitehomes;

import fi.matiaspaavilainen.masuitehomes.commands.Delete;
import fi.matiaspaavilainen.masuitehomes.commands.List;
import fi.matiaspaavilainen.masuitehomes.commands.Set;
import fi.matiaspaavilainen.masuitehomes.commands.Teleport;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class MaSuiteHomes extends JavaPlugin {
    public static HashMap<UUID, Long> cooldowns = new HashMap<>();
    public Config config = new Config(this);

    @Override
    public void onEnable() {
        super.onEnable();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new HomeMessageListener());
        saveDefaultConfig();
        config.createConfigs();

        getCommand("sethome").setExecutor(new Set());
        getCommand("delhome").setExecutor(new Delete());
        getCommand("home").setExecutor(new Teleport(this));
        getCommand("homes").setExecutor(new List());
    }

    public String colorize(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
