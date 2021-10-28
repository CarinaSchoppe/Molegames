package network.util;

public enum Packets {
    ;


    private final int id;
    private final String name;
    private final String content;

    Packets(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
}
