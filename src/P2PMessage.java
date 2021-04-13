/**
 * This class holds incoming messages to this node.
 */
public class P2PMessage {

    private String sMessage;

    // Used for queue.
    public P2PMessage next = null;



    public void setMessage(String sMessage){

        this.sMessage = sMessage;
    }

    public String getMessage(){

        return sMessage;
    }
}
