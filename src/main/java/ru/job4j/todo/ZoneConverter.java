package ru.job4j.todo;

import org.springframework.stereotype.Component;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Component
public class ZoneConverter {

    public Collection<Task> convertToUserTimeZone(ZoneId userZone, Collection<Task> tasks) {
        for (Task task : tasks) {
            ZonedDateTime utcTime = task.getCreated().atZone(ZoneOffset.UTC);
            ZonedDateTime userTime = utcTime.withZoneSameInstant(userZone);
            task.setUserCreationTime(userTime);
        }
        return tasks;
    }

    public LocalDateTime convertFromLocalTimeToUtc(ZoneId userZone) {
        ZonedDateTime userZoned = ZonedDateTime.now(userZone);
        System.out.println(userZoned);
        ZonedDateTime utcZoned = userZoned.withZoneSameInstant(ZoneOffset.UTC);
        System.out.println(utcZoned);
        System.out.println(utcZoned.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")));
        return utcZoned.toLocalDateTime();
    }
}
