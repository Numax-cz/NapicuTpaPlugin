package Listener;

import Main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnDisable implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player hrac = e.getPlayer();
        Main.NapicuPlayersTpa.remove(hrac.getName());
        Main.NapicuPlayersTpaTime.remove(hrac.getName());
    }

}
