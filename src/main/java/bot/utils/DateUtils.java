package bot.utils;

public class DateUtils {
    private DateUtils() {
    }

    public static long getCurrentUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static long getTimeDifferenceInSecs(long time) {
        return getCurrentUnixTime() - time;
    }

    public static String getDayNameByDayNumber(int dayNumber) {
        switch (dayNumber) {
            case 1:
            case 7:
                return "Понедельник";
            case 2:
            case 8:
                return "Вторник";
            case 3:
            case 9:
                return "Среда";
            case 4:
            case 10:
                return "Четверг";
            case 5:
            case 11:
                return "Пятница";
            case 6:
            case 12:
                return "Суббота";
            default:
                throw new IllegalArgumentException("Incorrect day number was provided");
        }
    }
}
