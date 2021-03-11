package Timer;

import Main.Main;
import org.bukkit.Bukkit;

public class TpaTimer {
    public static Integer CasTimer = Main.plugin.getConfig().getInt("messages.main.settings.PlayerPostdelay");
    public static void RunTpaTimer(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            @Override
            public void run() {
                TpaChecker.TpaPlayerChecker();
            }
        }, 0, 20L * CasTimer);
    }
}
