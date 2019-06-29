package tk.jasonho.reee.support;

import tk.jasonho.shortcuts.everything.log.LoggerManager;
import tk.jasonho.shortcuts.everything.util.ConsoleColors;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DiscordLoggerManager extends LoggerManager {

    private String getLoggerPrefix(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.systemDefault());

        return "[" + formatter.format(instant) + "]";
    }

    private String buildLogInfo(Instant instant, Level level, String message) {
        return this.getLoggerPrefix(instant) + "[" + level.toString() + "]: " + message;
    }

    private void out(String i) {
        System.out.println(i);
    }

    @Override
    public void info(String s) {
        out(buildLogInfo(Instant.now(), Level.INFO, s));
    }

    @Override
    public void warn(String s) {
        out(buildLogInfo(Instant.now(), Level.WARN, s));
    }

    @Override
    public void error(String s) {
        out(buildLogInfo(Instant.now(), Level.ERROR, s));
    }

    @Override
    public void debug(String s) {
        out(buildLogInfo(Instant.now(), Level.DEBUG, s));
    }

    enum Level {
        INFO(ConsoleColors.GREEN),
        WARN(ConsoleColors.YELLOW),
        ERROR(ConsoleColors.RED),
        DEBUG(ConsoleColors.CYAN),
        CUSTOM(ConsoleColors.MAGENTA);

        private ConsoleColors color;

        Level(ConsoleColors color) {
            this.color = color;
        }

        public String getDisplay() {
            return "LVL:" + color.toString() + this.toString() + ConsoleColors.RESET;
        }
    }
}
