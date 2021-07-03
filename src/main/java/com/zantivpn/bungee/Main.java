package com.zantivpn.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.List;

public class Main extends Plugin {
    public static List<String> whitelistips;
    public static String vpnkick = "";
    public static boolean ban = false;
    public static String motivo = "";
    public static int duracao = 0;
    public static String comando = "";
    public static Configuration config = null;
    public static Main main;
    public static String readInputStreamAsString(InputStream in)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            byte b = (byte)result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }
    public void saveDefaultConfig(){
        File pluginDir = new File(getDataFolder().getPath());
        if(!pluginDir.exists()){
            pluginDir.mkdirs();
        }
        File configFile = new File(getDataFolder(),"config.yml");
        if(!configFile.exists()){
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                InputStream is = getClass().getClassLoader().getResourceAsStream("config.yml");
                FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.append(readInputStreamAsString(is));
                fileWriter.close();
            } catch (IOException e) {
                getLogger().info(e.getMessage());
            }
        }
    }
    public void deleteConfig(){
        File configFile = new File(getDataFolder(),"config.yml");
        configFile.delete();
    }
    @Override
    public void onEnable() {
        super.onEnable();
        main = this;
        saveDefaultConfig();
        getLogger().info("Ligando AntiVPN...");
        

        getLogger().info("\nAutor: emanuel VINI\nVersão: 1.1\nSuporte: emanuel VINI#8000\nChangelog:\nAgora o plugin conta com um Anti Proxy também!");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        whitelistips = config.getStringList("ipswhitelist");
        getLogger().info("IPs na whitelist: ");
        for(String ip : whitelistips){
            getLogger().info("    "+ip);
        }
        for(String line : config.getStringList("vpnkick")){
            vpnkick = vpnkick + line + "\n";
        }
        ban = config.getBoolean("ban");
        motivo = config.getString("motivo");
        duracao = config.getInt("duracao");
        comando = config.getString("comando");
        getProxy().getPluginManager().registerListener(this, new AntiVPN());
        getLogger().info("AntiVPN ligado com sucesso!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.main.config, new File(Main.main.getDataFolder(), "config.yml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
