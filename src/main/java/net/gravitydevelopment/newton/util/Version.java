package net.gravitydevelopment.newton.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gravitydevelopment.newton.Newton;
import net.gravitydevelopment.newton.channel.AntiCheat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.pircbotx.Colors;

public class Version {
    private String gameVersion = "";
    private String version = "";
    private String link = "";

    private static final String API_NAME_VALUE = "name";
    private static final String API_LINK_VALUE = "downloadUrl";
    private static final String API_GAME_VERSION_VALUE = "gameVersion";
    private static final String API_QUERY = "/servermods/files?projectIds=";
    private static final String API_HOST = "https://api.curseforge.com";
    private static final int PROJECT_ID = 38723;
    
    public Version(final Newton newton) {
        query();
        
        Runnable run = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    final String old = version;
                    query();
                    
                    if(!old.equals(version)) {
                        ((AntiCheat)newton.getChannels()[0]).updateVersion(old, version, link);
                    }
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Version.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        
        new Thread(run).start();
    }
    
    public String[] format() {
        return new String[] {
            "Latest version: AntiCheat v" + 
                Colors.GREEN + 
                version +
                Colors.BLACK +
                " for CB " + 
                Colors.BOLD + 
                gameVersion + 
                Colors.NORMAL + 
                " posted " + 
                Colors.BLACK,
                
            "Download: " + link
        };        
    }

    public void query() {
        URL url = null;

        try {
            url = new URL(API_HOST + API_QUERY + PROJECT_ID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        try {
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent", "Newton (by Gravity)");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() > 0) {
                JSONObject latest = (JSONObject) array.get(array.size() - 1);
                version = (String) latest.get(API_NAME_VALUE);
                link = (String) latest.get(API_LINK_VALUE);
                gameVersion = (String) latest.get(API_GAME_VERSION_VALUE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
    
    public String getVersion() {
        return version;
    }
}
