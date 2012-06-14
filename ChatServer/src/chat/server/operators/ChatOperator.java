package chat.server.operators;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JTextArea;
import chat.server.clients.Client;
import chat.server.gui.ChatMainFrame;

public class ChatOperator {

    /**
     * public static Map<Integer,Client> allClients = new
     * HashMap<Integer,Client>(); ConcurentHashMap:
     * http://ria101.wordpress.com/2011
     * /12/12/concurrenthashmap-avoid-a-common-misuse/
     * 
     */
    public static ConcurrentHashMap<Integer, Client> allClients = new ConcurrentHashMap<Integer, Client>(
	    8, 0.9f, 1);
    private Client client;

    /**
     * Motoda pozwala na wyslanie wiadomosci do wszystkich dostepnych
     * uzytkownikow.
     * 
     */
    public void broadcastMsg(final String message) {
	for (Client client : allClients.values()) {
	    client.sendMsg(message);
	}
	notifyGuiMessage(message, ChatMainFrame.jta_messageArea);

    }

    /**
     * Motoda uruchumienia Servera
     * 
     */

    public void initialiseServer() {
	final ChatServer server = new ChatServer();
	server.loadConfig();
	server.listenSocket();
    }

    /**
     * Inicjalizacja GUI
     * 
     */
    public void initialiseGUI() {
	final ChatMainFrame mainWindow = new ChatMainFrame();
	mainWindow.setVisible(true);
    }

    /**
     * metoda zwracajaca mape, wykorzystanie: HashMap wiadomosc =
     * przerob(tekst_do_przerobienia); wykorzystujesz np
     * wiadomosc.get(MsgTags.HOST)
     * 
     * @param tekst
     * @return HashMap
     */
    @SuppressWarnings("rawtypes")
    public HashMap<Enum, String> messagePerser(String tekst) {
	String[] tabela = tekst.split("#<-ENDTAG->#");
	HashMap<Enum, String> wiadomosc = new HashMap<Enum, String>();

	for (TypTags enumTag : TypTags.values()) {
	    for (String zdanie : tabela) {
		if (zdanie.startsWith(enumTag.tag.toString())) {
		    wiadomosc.put(TypTags.TYPE, enumTag.tag.toString());
		}
	    }
	}

	for (MsgTags enumTag : MsgTags.values()) {
	    for (String zdanie : tabela) {
		if (zdanie.startsWith(enumTag.tag.toString())) {
		    wiadomosc.put(enumTag,
			    zdanie.replaceFirst(enumTag.tag.toString(), ""));
		}
	    }
	}

	return wiadomosc;

    }

    /**
     * tu duzo nie musze mowic podajesz parametry do funkcji zwraca ci string
     * ulozonej wiadomosci gotowej do parsowania
     * 
     * @param p_typ
     * @param p_host
     * @param p_time
     * @param p_msg
     * @return
     */
    public String stringToMessage(String p_typ, String p_host, String p_usr,
	    String p_time, String p_msg) {
	String wiadomosc = "$<typ>$#<-ENDTAG->#HOST=$<host>$#<-ENDTAG->#TIME=$<time>$#<-ENDTAG->#MSG=$<msg>$#<-ENDTAG->#";

	wiadomosc = wiadomosc.replace("$<typ>$", p_typ);
	wiadomosc = wiadomosc.replace("$<host>$", p_host);
	wiadomosc = wiadomosc.replace("$<time>$", p_time);
	wiadomosc = wiadomosc.replace("$<msg>$", p_msg);
	wiadomosc = wiadomosc.replace("$<user>$", p_usr);

	return wiadomosc;
    }

    /**
     * Motoda pozwala na wylasnie wiadomosci do wybranego uzytkownika.
     * 
     */
    public void sendMsg(final String clientName, final String message) {
	for (Client client : allClients.values()) {
	    if (client.getClientName().equals(clientName)) {
		client.sendMsg(message);
	    }
	}
	notifyGuiMessage(message, ChatMainFrame.jta_messageArea);
    }

    /**
     * Motoda pozwala na dodanie nowego Klienta;
     * 
     */
    public void addClient(final Client client) {
	final Integer clientId = allClients.size();
	allClients.put(clientId == null ? allClients.size() : clientId + 1,
		client);
	notifyNewClientConnection(client, ChatMainFrame.jta_messageArea);
    }

    /**
     * Metoda wyswietla komunikat o nowym podlaczonym Kliencie w JTextArea
     * 
     */
    public void notifyNewClientConnection(final Client client,
	    final JTextArea jTextArea) {
	jTextArea.append("[ " + DateTimeOperator.getCurrentDateTime() + " ] "
		+ "New Client: "
		+ client.clientSocket.getInetAddress().toString() + "\n");
    }

    /**
     * Metoda wyswietla przeslane wiadomosci w JTextArea
     * 
     */
    public void notifyGuiMessage(final String message, final JTextArea jTextArea) {
	jTextArea.append("[ BROADCAST: " + DateTimeOperator.getCurrentDateTime() + " ] "
		+ message + "\n");
    }

    /**
     * Motoda pozwala na usuniecie wybranego Klienta;
     * 
     */
    public void removeClient(final Client client) {
	allClients.values().remove(client);
    }

    /**
     * Motoda zwraca liczbe podlaczonych klientow
     * 
     */

    public int getNumberOfClients() {
	return allClients.size();
    }

    /**
     * Metoda zwraca Stringa z lista dostepnych Klientow;
     * 
     */
    public String getClientsList() {
	StringBuilder sBuilder = new StringBuilder();
	for (Client client : allClients.values()) {
	    sBuilder.append(client.getClientName());
	    sBuilder.append(", ");
	}

	return sBuilder.toString();
    }

    @SuppressWarnings("rawtypes")
    public void serverDo(Client client, HashMap<Enum, String> wiadomosc) {
	if (wiadomosc.get(TypTags.TYPE).equals("BRC_")) {
	    broadcastMsg(wiadomosc.get(MsgTags.MSG));
	} else if (wiadomosc.get(TypTags.TYPE).equals("NOT_CON")) {

	} else if (wiadomosc.get(TypTags.TYPE).equals("REQ_HOSTS")) {

	} else if (wiadomosc.get(TypTags.TYPE).equals("REQ_USRS")) {

	} else if (wiadomosc.get(TypTags.TYPE).equals("REQ_SET_NAME=")) {
	    client.setClientName(wiadomosc.get(MsgTags.REQ_SET_NAME));

	} else if (wiadomosc.get(TypTags.TYPE).equals("UNC_NAME=")) {
	    sendMsg(wiadomosc.get(MsgTags.USER), wiadomosc.get(MsgTags.MSG));
	} else if (wiadomosc.get(TypTags.TYPE).equals("REQ_DIS")) {

	} else if (wiadomosc.get(TypTags.TYPE).equals("NOT_ERR_HOST_NOTFOUND")) {

	} else if (wiadomosc.get(TypTags.TYPE).equals("NOT_ERR_USER_NOTFOUND")) {

	}

    }

    public Client getClient() {
	return client;
    }

    public void setClient(Client client) {
	this.client = client;
    }

}
