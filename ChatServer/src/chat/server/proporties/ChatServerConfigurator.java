package chat.server.proporties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class ChatServerConfigurator {

    private Properties configChatServer;
    private String serverIp;
    private int serverPort;
    private int serverMaxUsers;

    @SuppressWarnings("rawtypes")
    public void readConfigFile(final File file) {
	configChatServer = new Properties();
	try {
	    configChatServer.load(new FileInputStream(file));
	    Enumeration en = configChatServer.keys();
	    while (en.hasMoreElements()) {
		String key = (String) en.nextElement();
		switch (ChatServerEnums.valueOf(key)) {
		case IP:
		    setServerIp((String) configChatServer.get(key));
		    break;
		case PORT:
		    setServerPort(Integer.valueOf(configChatServer.get(key)
			    .toString()));
		    break;
		case MAX_USERS:
		    setServerMaxUsers(Integer.valueOf(configChatServer.get(key)
			    .toString()));
		    break;
		default:
		    break;
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public String getServerIp() {
	return serverIp;
    }

    public void setServerIp(String serverIp) {
	this.serverIp = serverIp;
    }

    public int getServerPort() {
	return serverPort;
    }

    public void setServerPort(int serverPort) {
	this.serverPort = serverPort;
    }

    public int getServerMaxUsers() {
	return serverMaxUsers;
    }

    public void setServerMaxUsers(int serverMaxUsers) {
	this.serverMaxUsers = serverMaxUsers;
    }
}
