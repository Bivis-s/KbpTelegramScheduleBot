package bot.db.objects;

public enum NotifyTime {
    EARLY("12:00"),
    MEDIUM("15:00"),
    LATE("18:00");

    private final String value;

    NotifyTime(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
