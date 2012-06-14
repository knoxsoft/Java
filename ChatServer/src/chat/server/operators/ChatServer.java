package chat.server.operators;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import chat.server.clients.Client;
import chat.server.proporties.ChatServerConfigurator;

public class ChatServer {

    @SuppressWarnings("unused")
    private String serverIp;
    private int serverPort;

    private final static File DEF_CONFIG_FILE = new File("./config/app.config");

    private ChatServerConfigurator configMenager;
    private ChatOperator operator;
    ServerSocket server = null;

    public void loadConfig() {
	configMenager = new ChatServerConfigurator();
	configMenager.readConfigFile(DEF_CONFIG_FILE);

	this.serverIp = configMenager.getServerIp();
	this.serverPort = configMenager.getServerPort();
    }

    public void listenSocket() {
	try {
	    server = new ServerSocket(serverPort);
	} catch (IOException e) {
	    System.out.println("Could not listen on port: " + serverPort
		    + "...Exception: " + e);
	    System.exit(-1);
	}
	try {
	    operator = new ChatOperator();
	    Client klient;

	    while (true) {
		klient = new Client();
		klient.clientSocket = server.accept();
		operator.broadcastMsg("#INFO: New Client connected: "
			+ klient.clientSocket.getInetAddress().toString());

		operator.addClient(klient);

		Client.listenClients listenClient = klient.new listenClients();
		Thread watekListening = new Thread(listenClient);
		watekListening.setName("Watek nasluchiwania: " + klient.clientSocket.getInetAddress());
		watekListening.start();
	    }

	} catch (IOException e2) {
	    System.out.println(e2);
	    System.exit(-1);
	}
    }
}