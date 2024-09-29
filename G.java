package com.example.sshcheck;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class SSHCheckPlugin extends JavaPlugin {

    // Discord Webhook URL
    private static final String DISCORD_WEBHOOK_URL = "DEINE_DISCORD_WEBHOOK_URL";

    // SSH Server Informationen
    private static final String SSH_HOST = "dein-ssh-server";
    private static final int SSH_PORT = 22;
    private static final String SSH_USER = "dein-ssh-benutzername";
    private static final String SSH_PASSWORD = "dein-ssh-passwort";

    @Override
    public void onEnable() {
        // Starte den wiederholten Task alle 60 Minuten (20 Ticks = 1 Sekunde, 60 Minuten = 72000 Ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                checkSSH();
            }
        }.runTaskTimerAsynchronously(this, 0L, 72000L); // 72000 Ticks = 60 Minuten
    }

    private void checkSSH() {
        try {
            // Initialisiere JSch für SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);

            // Setze SSH Passwort
            session.setPassword(SSH_PASSWORD);

            // Vermeide Host Key Überprüfung (nicht empfohlen für Produktion!)
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // Verbinde mit SSH Server
            session.connect(5000);  // Timeout auf 5 Sekunden gesetzt

            // Wenn Verbindung erfolgreich
            getLogger().info("SSH Server " + SSH_HOST + " ist erreichbar.");

            // Schließe die Sitzung
            session.disconnect();
        } catch (Exception e) {
            // Wenn die Verbindung fehlschlägt, sende eine Nachricht an den Discord-Webhook
            sendDiscordMessage("Fehler: SSH Server " + SSH_HOST + " ist nicht erreichbar.\nFehlermeldung: " + e.getMessage());
        }
    }

    private void sendDiscordMessage(String message) {
        try {
            // URL Verbindung herstellen
            URL url = new URL(DISCORD_WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // JSON-Daten für Discord
            String jsonPayload = "{\"content\":\"" + message + "\"}";

            // Daten senden
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Überprüfe die Antwort
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                getLogger().info("Fehlermeldung erfolgreich an Discord gesendet.");
            } else {
                getLogger().warning("Fehler beim Senden der Nachricht: " + responseCode);
            }

        } catch (Exception e) {
            getLogger().warning("Fehler beim Senden der Discord-Nachricht: " + e.getMessage());
        }
    }
}
