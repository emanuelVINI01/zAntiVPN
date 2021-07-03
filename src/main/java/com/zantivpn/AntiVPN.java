package com.zantivpn;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import vpn.detection.VPNDetection;

import java.io.IOException;

public class AntiVPN implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        String ip = e.getAddress().getHostAddress();
        ip = ip.replace(" ","");
        boolean isWhitelistIp = false;
        boolean inCache = false;
        if(Main.getPlugin(Main.class).getConfig().contains("cache"+ip+"vpn")){
            inCache = true;
            if(Main.getPlugin(Main.class).getConfig().getBoolean("cache"+ip+"vpn")){
                String cmdparse = "";
                if(Main.ban){
                    cmdparse = Main.comando.replace("%player%",e.getPlayer().getName()).replace("%duracao%",String.valueOf(Main.duracao)).replace("%motivo%",Main.motivo);
                }
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER,Main.vpnkick.replace("%ip%",e.getAddress().toString().substring(1,e.getAddress().toString().length()).split(":")[0]));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmdparse);
            }
            return;
        }
        for(String bp : Main.whitelistips){
            if(bp.equalsIgnoreCase(ip)){
                isWhitelistIp = true;
            }
        }
        if(isWhitelistIp){
            e.allow();
            return;
        }
        Boolean isHostingorVPN = null;
        try {
            isHostingorVPN = new VPNDetection().getResponse(ip).hostip;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if(isHostingorVPN){
            String cmdparse = "";
            if(Main.ban){
                cmdparse = Main.comando.replace("%player%",e.getPlayer().getName()).replace("%duracao%",String.valueOf(Main.duracao)).replace("%motivo%",Main.motivo);
            }
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER,Main.vpnkick.replace("%ip%",e.getAddress().toString().substring(1,e.getAddress().toString().length()).split(":")[0]));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmdparse);
        }
        boolean isProxy = false;
        try {
            isProxy = ProxyDetector.detectProxy(ip);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if(isProxy){
            String cmdparse = "";
            if(Main.ban){
                cmdparse = Main.comando.replace("%player%",e.getPlayer().getName()).replace("%duracao%",String.valueOf(Main.duracao)).replace("%motivo%",Main.motivo);
            }
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER,Main.vpnkick.replace("%ip%",e.getAddress().toString().substring(1,e.getAddress().toString().length()).split(":")[0]));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmdparse);
        }
        Main.getPlugin(Main.class).getConfig().set("cache."+ip+"."+"vpn",isHostingorVPN || isProxy);
    }
}
