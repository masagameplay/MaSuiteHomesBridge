package fi.matiaspaavilainen.masuitehomes;

import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteHomes extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new HomeMessageListener());
    }
}
