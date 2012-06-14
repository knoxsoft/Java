package chat.server.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import chat.server.gui.ChatMainFrame;
import chat.server.operators.ChatOperator;
import chat.server.operators.DateTimeOperator;

public class Client {

    private String clientName;
    private String clientHostName;
    public Socket clientSocket;

    public BufferedReader in = null;
    public PrintWriter out = null;

    public final static String DEF_CLIENT_NAME = "N/A";
    private final static String SUCCESSFUL_CONNECTED = "*** SERVER INFO: *** \n"
	    + "Polaczyles sie z FlashChat Server";

    public Client() {
	setClientName(DEF_CLIENT_NAME);
    }

    public Client(final Socket clientSocket) {
	this.clientSocket = clientSocket;
    }

    @Deprecated
    public String messagePatternWithDate(final String msg) {
	return "[ " + DateTimeOperator.getCurrentDateTime() + " ] " + msg;
    }

    public void sendMsg(final String message) {
	if (!message.isEmpty()) {
	    out.println(message);
	    ChatMainFrame.jtf_send_textField.setText("");
	}
    }

    public String getClientName() {
	return clientName;
    }

    public void setClientName(String clientNamee) {
	this.clientName = clientNamee;
    }

    public String getClientHostName() {
	return clientHostName;
    }

    public void setClientHostName(String clientHostName) {
	this.clientHostName = clientHostName;
    }

    public class listenClients implements Runnable {
	ChatOperator operator = new ChatOperator();

	@Override
	public void run() {
	    try {
		in = new BufferedReader(new InputStreamReader(
			clientSocket.getInputStream()));
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		out.println(SUCCESSFUL_CONNECTED);

		while (true) {
		    String input;

		    /*
		     * null - oznacza ze zerwane polaczenie
		     */
		    while ((input = in.readLine()) != null) {
			ChatMainFrame.jta_messageArea.append("RECEIVED: " + input + "\n");
			@SuppressWarnings("rawtypes")
			HashMap<Enum, String> wiadomosc = operator
				.messagePerser(input);

			/***
			 * Broadcastuje odtrzymana wiadomosc
			 * 
			 */
			operator.serverDo(Client.this, wiadomosc);
			//
			// metoda persowania
			//
		    }

		    if (ChatOperator.allClients.contains(Client.this)) {
			operator.broadcastMsg("#INFO : Client: "
				+ Client.this.clientSocket.getInetAddress()
					.toString() + " disconnected.");
			System.out.println("#Klient usuniety: "
				+ Client.this.clientSocket.getInetAddress()
					.toString()
				+ " :) # See line 95 Class.Client");
			operator.removeClient(Client.this);
			Thread.currentThread().interrupt();
		    }
		    break;
		}

	    } catch (IOException e) {
		System.out.println(e + " #Usunieto Klienta: "
			+ Client.this.clientSocket.getInetAddress().toString()
			+ " :) # see line 103 Class.Client");
		operator.broadcastMsg("#INFO : Client: "
			+ Client.this.clientSocket.getInetAddress().toString()
			+ " disconnected.");
		operator.removeClient(Client.this);
		Thread.currentThread().interrupt();

	    }
	}
    }
}
