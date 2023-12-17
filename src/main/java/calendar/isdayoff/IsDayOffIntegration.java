package calendar.isdayoff;

import com.groupstp.isdayoff.*;
import com.groupstp.isdayoff.enums.DayType;

import java.io.File;
import java.util.*;

import static calendar.MainApplication.*;
import static java.lang.Boolean.*;

/**
 * Класс для взаимодействия с библиотекой isDayOff
 */
public class IsDayOffIntegration {
    /**
     * Папка, в которой лежит кэш библиотеки isDayOff
     */
    private static final File cacheDirectory = new File("cache");

    /**
     * Глобальная сущность библиотеки isDayOff
     */
    private static IsDayOff isDayOff;

    /**
     * Загружает библиотеку isDayOff и создает папку для ее кэша, если эта папка не существует
     */
    public static void load() {
        // Создаем папку для кэша
        boolean created = cacheDirectory.mkdirs();
        if (created)
            logger.info("Created the cache directory for IsDayOff: {}", cacheDirectory.getAbsolutePath());

        try {
            isDayOff = IsDayOff.Builder().setCacheDir("cache").build();
            logger.info("Loaded the IsDayOff cache from: {}", cacheDirectory.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Failed to load the IsDayOff cache! Error: {}", e.getMessage());
        }
    }

    /**
     * @param year год
     * @param month месяц
     * @return список выходных для конкретного месяца конкретного года
     */
    public static List<IsDayOffDateType> getHolidays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 0);

        return isDayOff.daysTypeByMonth(calendar.getTime());
    }

    /**
     * @param year год
     * @param month месяц
     * @param day день
     * @return является ли конкретный день конкретного месяца конкретного года выходным днем
     */
    public static boolean isHoliday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        return isDayOff.dayType(calendar.getTime()) == DayType.NOT_WORKING_DAY;
    }

    /**
     * @param year год
     * @return является ли этот год високосным
     */
    public static boolean isLeap(int year) {
        return TRUE.equals(isDayOff.checkIsLeap(new Date(year, 1, 1)));
    }
}