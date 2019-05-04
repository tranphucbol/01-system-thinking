import java.util.Date;

public class MyMessage {
    private String name;
    private Date sentAt;
    private String message;

    public MyMessage() {

    }

    public MyMessage(String name, String message) {
        this.name = name;
        this.message = message;
        this.sentAt = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return name + " " + sentAt.toString();
    }

    @Override
    public String toString() {
        return name + ": " + message;
    }
}
