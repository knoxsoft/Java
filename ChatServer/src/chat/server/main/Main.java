package chat.server.main;

import chat.server.operators.ChatOperator;

public class Main {

    /**
     * @param args
     */
    public static void main(final String[] args) {
	final ChatOperator chat = new ChatOperator();
	chat.initialiseGUI();
	chat.initialiseServer();
    }

}
