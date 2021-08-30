package top.snowphonix.toolsetusercenter.model;

public enum AuthLevel {
    ADMIN(2, "admin"),
    USER(1, "user"),
    PASSERBY(0, "passerby");

    private final int num;
    private final String name;

    AuthLevel(int num, String name) {
        this.num = num;
        this.name = name;
    }

    public int toNum() {
        return num;
    }

    @Override
    public String toString() {
        return name;
    }
}
