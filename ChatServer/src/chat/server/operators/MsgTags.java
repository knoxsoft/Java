package chat.server.operators;

public enum MsgTags {

    MSG("MSG="), HOST("HOST="), USER("USER="), TIME("TIME="), REQ_SET_NAME(
	    "REQ_SET_NAME=");
    String tag;

    MsgTags(String tag) {
	this.tag = tag;
    }

    public String tag() {
	return this.tag;
    }

}
