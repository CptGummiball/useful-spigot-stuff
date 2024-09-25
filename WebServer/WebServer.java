package com.example.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebServer extends AbstractHandler implements Runnable {

    private final JavaPlugin plugin;
    private final int port;
    private final File webDir;
    private Server server;

    public WebServer(JavaPlugin plugin) {
        this.plugin = plugin;

        // Port aus der config.yml laden
        FileConfiguration config = plugin.getConfig();
        this.port = config.getInt("webserver.port", 8080);  // Standardport ist 8080

        // Verzeichnis für statische Dateien (/web im Pluginordner)
        this.webDir = new File(plugin.getDataFolder(), "web");
    }

    // Startet den Webserver in einem separaten Thread
    @Override
    public void run() {
        server = new Server(port);  // Server mit konfiguriertem Port starten
        server.setHandler(this);  // Handler setzen

        try {
            server.start();
            plugin.getLogger().info("Webserver started on port " + port);
            server.join();  // Wartet auf den Server-Thread
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to start web server!");
            e.printStackTrace();
        }
    }

    // Beendet den Webserver
    public void stopServer() {
        if (server != null && server.isRunning()) {
            try {
                server.stop();
                plugin.getLogger().info("Webserver stopped.");
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to stop web server!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Pfad zu statischen Dateien anpassen
        File requestedFile = new File(webDir, target);

        // Prüfen, ob die Datei existiert und lesbar ist
        if (requestedFile.exists() && requestedFile.isFile()) {
            // Setze den richtigen MIME-Typ basierend auf der Dateierweiterung
            String mimeType = Files.probeContentType(Paths.get(requestedFile.getPath()));
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setStatus(HttpServletResponse.SC_OK);

            // Dateiinhalt ausgeben
            try (FileInputStream fileInputStream = new FileInputStream(requestedFile)) {
                fileInputStream.transferTo(response.getOutputStream());
            }

            // Anfrage als abgeschlossen markieren
            baseRequest.setHandled(true);
        } else {
            // Standardantwort, wenn keine Datei gefunden wird
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from Jetty!</h1>");
            baseRequest.setHandled(true);
        }
    }
}
