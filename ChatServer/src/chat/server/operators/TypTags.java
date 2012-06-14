package chat.server.operators;

public enum TypTags {

    TYPE(""), BRC("BRC_"), NOT("NOT_CON"), REQ_HOSTS("REQ_HOSTS"), REQ_USRS(
	    "REQ_USRS"), REQ_SET_NAME("REQ_SET_NAME="), UNC_HOST("UNC_HOST="), UNC_NAME(
	    "UNC_NAME="), REQ_DIS("REQ_DIS"), NOT_ERR_HOST_NOTFOUND(
	    "NOT_ERR_HOST_NOTFOUND"), NOT_ERR_USER_NOTFOUND(
	    "NOT_ERR_USER_NOTFOUND");
    String tag;

    TypTags(String tag) {
	this.tag = tag;
    }

    public String tag() {
	return this.tag;
    }

}
