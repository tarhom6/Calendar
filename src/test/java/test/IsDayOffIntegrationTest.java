package test;

import calendar.isdayoff.IsDayOffIntegration;
import com.groupstp.isdayoff.IsDayOffDateType;
import com.groupstp.isdayoff.enums.DayType;
import org.junit.*;

import java.util.List;

import static junit.framework.TestCase.*;

public class IsDayOffIntegrationTest {
    @Before
    public void setUp() {
        IsDayOffIntegration.load();
    }

    @Test
    public void testGetHolidays() {
        List<IsDayOffDateType> allDays = IsDayOffIntegration.getHolidays(2023, 12);
        assertEquals(31, allDays.size());

        List<IsDayOffDateType> holidays = allDays
                .stream()
                .filter(date -> date.getDayType() == DayType.NOT_WORKING_DAY)
                .toList();

        assertFalse(holidays.isEmpty());
    }

    @Test
    public void testIsHoliday() {
        assertTrue(IsDayOffIntegration.isHoliday(2024, 1, 1));
        assertFalse(IsDayOffIntegration.isHoliday(2024, 1, 9));
    }

    @Test
    public void testIsLeap() {
        assertTrue(IsDayOffIntegration.isLeap(2024));
        assertFalse(IsDayOffIntegration.isLeap(2025));
    }
}
