package Timer;

import Main.Main;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class TpaChecker {
    public static Integer Cas = Main.plugin.getConfig().getInt("messages.main.settings.PlayerPostdelay");
    public static void TpaPlayerChecker(){
        for(String i : new HashSet<String>(Main.NapicuPlayersTpaTime.keySet())){
            Date PlayerTime = Main.NapicuPlayersTpaTime.get(i);
            Calendar c = Calendar.getInstance();
            c.setTime(PlayerTime);
            c.add(Calendar.SECOND, Cas);
            if(new Date().after(c.getTime())){
                Main.NapicuPlayersTpa.remove(i);
                Main.NapicuPlayersTpaTime.remove(i);
            }

        }
    }
}
