# WebServer

## Description

The `WebServer` class provides a simple way to start an embedded Jetty web server within a Spigot plugin. This server can serve static files from a designated `/web` directory located in the plugin's data folder and responds with a default message when the requested file is not found.

## Features

- Configurable port via `config.yml`.
- Serves static files from the `/web` subdirectory of the plugin folder.
- Runs on a separate thread, ensuring the main server remains responsive.
- Provides a default response if the requested file is not found.

## Configuration

Add the following entry to your `config.yml` file to specify the port for the web server:

```yaml
webserver:
  port: 8080 # Port number on which the web server will run
````

## Usage
1. Add the WebServer class to your project.
2. Implement the WebServer in your main plugin class:
```java
public class MyPlugin extends JavaPlugin {

    private Thread webServerThread;
    private WebServer webServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();  // Ensure config.yml exists

        // Initialize and start the web server
        webServer = new WebServer(this);
        webServerThread = new Thread(webServer);
        webServerThread.start();
    }

    @Override
    public void onDisable() {
        // Stop the web server
        if (webServer != null) {
            webServer.stopServer();
        }

        // Interrupt the web server thread if it is still running
        if (webServerThread != null && webServerThread.isAlive()) {
            try {
                webServerThread.join();  // Wait for the thread to finish
            } catch (InterruptedException e) {
                getLogger().severe("Failed to stop web server thread!");
                e.printStackTrace();
            }
        }
    }
}
````
3. **Create a** ``/web`` **directory** in your plugin's data folder and add static files (e.g., HTML, CSS, JS) there.
4. **Access the web server** by navigating to ``http://localhost:<port>/filename`` in your web browser. Replace ``<port>`` with the configured port from ``config.yml``.

## Example of a Static HTML File
Create an index.html file in your /web directory:
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
</head>
<body>
    <h1>Welcome to the Jetty Web Server!</h1>
</body>
</html>
````
