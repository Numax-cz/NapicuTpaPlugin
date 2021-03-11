package Main;

import Listener.OnDisable;
import Timer.TpaTimer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.HashMap;

public class Main extends JavaPlugin {
    public static Main plugin;
    public static String NapicuPrefix;
    public static String NapicuPrefixWarn;
    public static HashMap<String, String> NapicuPlayersTpa = new HashMap<String, String>();
    public static HashMap<String, Date> NapicuPlayersTpaTime = new HashMap<String, Date>();

    @Override
    public void onEnable() {
        //Plugin
        plugin = this;
        getServer().getPluginManager().registerEvents(new OnDisable(), plugin);

        TpaTimer.RunTpaTimer();
        //Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Prefix
        NapicuPrefix = this.getConfig().getString("messages.main.prefix.NapicuPrefix");
        NapicuPrefixWarn = this.getConfig().getString("messages.main.prefix.NapicuPrefixWARN");


        //Enable plugin
        getLogger().info(this.getConfig().getString("messages.main.console.onEnable"));
    }

    @Override
    public void onDisable() {
        //Disable plugin
        getLogger().info(this.getConfig().getString("messages.main.console.onDisable"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player hrac = (Player) sender;

            if(label.equalsIgnoreCase("tpa")){
                if(hrac.hasPermission("napicu.tpa")){
                    if(args.length == 0){
                        hrac.sendMessage(NapicuPrefixWarn + this.getConfig().getString("messages.commands.tpa.Parms2"));
                    }
                    else if(args.length == 1){ //TPA <PLAYER>
                        Player TargetPlayer = Bukkit.getPlayer(args[0]);
                        if(TargetPlayer != null){
                            if(NapicuPlayersTpa.get(hrac.getName()) == null){
                                NapicuPlayersTpa.put(hrac.getName(), TargetPlayer.getName()); //<Target, User>
                                NapicuPlayersTpaTime.put(hrac.getName(), new Date()); //<User, Date>
                                String message = this.getConfig().getString("messages.commands.tpa.TpaSend");
                                TargetPlayer.sendMessage(NapicuPrefix + message.replace("{playertarget}", hrac.getName()));
                                TargetPlayer.playSound(TargetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 20);
                            }else{
                                String message = this.getConfig().getString("messages.commands.tpa.TpaCantSend");
                                hrac.sendMessage(NapicuPrefixWarn + message);
                            }
                        }else{
                            String message = this.getConfig().getString("messages.commands.tpa.PlayerNotFound");
                            hrac.sendMessage(NapicuPrefixWarn + message);
                        }
                    }
                    else if(args.length == 2){//TPA <ACCEPT> <PLAYER>
                        String CMD = args[0]; // <ACCEPT>
                        Player TargetPlayer = Bukkit.getPlayer(args[1]); // <PLAYER> (User)
                        if(CMD.equalsIgnoreCase("accept")){
                            if (TargetPlayer != null && NapicuPlayersTpa.get(TargetPlayer.getName()) != null){
                                NapicuPlayersTpaTime.remove(TargetPlayer.getName());
                                NapicuPlayersTpa.remove(TargetPlayer.getName());
                                hrac.teleport(TargetPlayer.getLocation());
                                String message = this.getConfig().getString("messages.commands.tpa.Teleport");
                                TargetPlayer.sendMessage(NapicuPrefix + message.replace("{playertarget}", hrac.getName()));
                            }else{
                                hrac.sendMessage(NapicuPrefixWarn + this.getConfig().getString("messages.commands.tpa.TpaNotFound"));
                            }
                        }else{
                            hrac.sendMessage(NapicuPrefixWarn + this.getConfig().getString("messages.commands.tpa.Parms1"));
                        }
                    }else {
                        return false;
                    }
                }else{
                    hrac.sendMessage(NapicuPrefixWarn + this.getConfig().getString("messages.commands.tpa.Permission"));
                }
            }
        }
        return false;
    }
}
