package Main;

import Listener.OnDisable;
import Timer.TpaTimer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
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
                        if(args[0].equalsIgnoreCase("cancel")){
                            NapicuPlayersTpa.remove(hrac.getName());
                            NapicuPlayersTpaTime.remove(hrac.getName());
                        }else{
                            Player TargetPlayer = Bukkit.getPlayer(args[0]);
                            if(TargetPlayer != null){
                                if(NapicuPlayersTpa.get(hrac.getName()) == null){
                                    NapicuPlayersTpa.put(hrac.getName(), TargetPlayer.getName()); //<Target, User>
                                    NapicuPlayersTpaTime.put(hrac.getName(), new Date()); //<User, Date>
                                    String messagesent = this.getConfig().getString("messages.commands.tpa.TpaSendPlayer");
                                    String cancel = this.getConfig().getString("messages.commands.tpa.cancel");
                                    IChatBaseComponent c = IChatBaseComponent.ChatSerializer
                                            .a("{\"text\":\"" + NapicuPrefix + messagesent.replace("{playertarget}", TargetPlayer.getName()) + "\", \"extra\":[{\"text\":\"" + cancel + "\", \"hoverEvent\":{\"action\":\"show_text\", \"value\":\"/tpa cancel\"}, \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/tpa cancel\"}}]}");
                                    PacketPlayOutChat p = new PacketPlayOutChat(c);
                                    ((CraftPlayer)hrac).getHandle().playerConnection.sendPacket(p);


                                    String message = this.getConfig().getString("messages.commands.tpa.TpaSend");
                                    String accept = this.getConfig().getString("messages.commands.tpa.accept");
                                    IChatBaseComponent comp = IChatBaseComponent.ChatSerializer
                                            .a("{\"text\":\"" + NapicuPrefix + message.replace("{playertarget}", hrac.getName()) + "\", \"extra\":[{\"text\":\"" + accept + "\", \"hoverEvent\":{\"action\":\"show_text\", \"value\":\"/tpa accept " + hrac.getName() + "\"}, \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/tpa accept " + hrac.getName() + "\"}}]}");
                                    PacketPlayOutChat pk = new PacketPlayOutChat(comp);
                                    ((CraftPlayer)TargetPlayer).getHandle().playerConnection.sendPacket(pk);


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
