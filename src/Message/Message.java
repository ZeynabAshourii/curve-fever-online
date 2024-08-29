package src.Message;

import java.io.Serializable;
public class Message implements Serializable {
    private boolean endType;
    private MyString otherSideClient;
    private MessageTypes type;
    public enum MessageTypes {
        YES , NO , CLEAR , END;
    }
    public Message(MessageTypes type , MyString myString) {
        this.type = type;
        this.otherSideClient = myString;
    }
    public Message(MessageTypes type){
        this.type = type;
    }
    public Message(MessageTypes type , boolean end){
        this.type = type;
        this.endType = end;
    }

    public boolean isEndType() {
        return endType;
    }
    public MyString getOtherSideClient() {
        return otherSideClient;
    }
    public MessageTypes getType() {
        return type;
    }
}
