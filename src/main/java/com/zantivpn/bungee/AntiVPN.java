package com.zantivpn.bungee;

import com.zantivpn.ProxyDetector;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import vpn.detection.VPNDetection;


import java.io.IOException;

public class AntiVPN implements Listener {
    @EventHandler
    public void onPreLogin(PreLoginEvent e){
        String ip = e.getConnection().getAddress().toString().substring(1,e.getConnection().getAddress().toString().length()).split(":")[0];
        boolean isWhitelistIp = false;
        boolean inCache = false;
        if(Main.main.config.contains("cache"+ip+"vpn")){
            inCache = true;
            if(Main.main.config.getBoolean("cache"+ip+"vpn")){
                e.setCancelReason(new TextComponent(Main.vpnkick.replace("%ip%",ip)));
                e.setCancelled(true);
            }
            return;
        }
        for(String bp : Main.whitelistips){
            if(bp.equalsIgnoreCase(ip)){
                isWhitelistIp = true;
            }
        }
        if(isWhitelistIp){
            return;
        }
        Boolean isHostingorVPN = null;
        try {
            isHostingorVPN = new VPNDetection().getResponse(ip).hostip;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
		boolean isProxy = false;
        if(isHostingorVPN){
            e.setCancelReason(new TextComponent(Main.vpnkick.replace("%ip%",ip)));
            e.setCancelled(true);
        }
		try{
			isProxy = ProxyDetector.detectProxy(ip);
		}catch(Exception e2){
			isProxy = false;
		}
        Main.main.config.set("cache."+ip+".vpn",isHostingorVPN || isProxy);
    }
}
