package hello;

public class HelloMessage {

    private String name;

    private String to;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public HelloMessage(String name, String to) {
        this.name = name;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
