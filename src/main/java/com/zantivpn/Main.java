package com.zantivpn;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.zloader.Loader;
import org.spigotmc.SpigotConfig;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Main extends JavaPlugin {
    public static List<String> whitelistips;
    public static String vpnkick = "";
    public static boolean ban = false;
    public static String motivo = "";
    public static int duracao = 0;
    public static String comando = "";
    public static boolean byLoader = false;
    private boolean hasBungee(){
        boolean bungee = SpigotConfig.bungee;
        boolean onlineMode = Bukkit.getServer().getOnlineMode();
        if(bungee && (!(onlineMode))){
            return true;
        }
        return false;
    }
    @Override
    public void onEnable() {
        super.onEnable();

        getLogger().info("Ligando AntiVPN...");
        if(hasBungee()){
            getLogger().info("Para usar o zAntiVPN em sua rede BungeeCord, coloque o mesmo jar na pasta plugins do bungeecord e remova do seu servidor.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info(String.format("\nAutor: emanuel VINI\nVersão: %s\nSuporte: emanuel VINI#8000\nChangelog:\nAgora o plugin conta com um Anti Proxy também!", getDescription().getVersion()));
        saveDefaultConfig();
        if(!getConfig().contains("versao")){
            getLogger().info("Configuração antes da 1.2 detectada! Apagando configuração...");
            new File(getConfig().getDefaults().getCurrentPath()).delete();
            saveDefaultConfig();
        }
        if(!getConfig().getString("versao").equalsIgnoreCase(getDescription().getVersion())){
            getLogger().info(String.format("Configuração da versão %s detectada! Atualizando configuração e deletando atinga...", getConfig().getString("versao")));
            new File(getConfig().getDefaults().getCurrentPath()).delete();
            saveDefaultConfig();
        }

        whitelistips = getConfig().getStringList("ipswhitelist");
        getLogger().info("IPs na whitelist: ");
        for(String ip : whitelistips){
            getLogger().info("    "+ip);
        }
        for(String line : getConfig().getStringList("vpnkick")){
            vpnkick = vpnkick + line + "\n";
        }
        ban = getConfig().getBoolean("ban");
        motivo = getConfig().getString("motivo");
        duracao = getConfig().getInt("duracao");
        comando = getConfig().getString("comando");
        getServer().getPluginManager().registerEvents(new AntiVPN(), this);
        getLogger().info("AntiVPN ligado com sucesso!");
    }
}
