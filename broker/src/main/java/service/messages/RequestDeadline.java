package service.messages;

/**
 * @author Yuanyuan Lu
 * @create on 2022-10-27-21:00
 */
public class RequestDeadline {
    public int getIdentifier() {
        return identifier;
    }

    int identifier;
    public RequestDeadline(int identifier){
        this.identifier = identifier;
    }
}
