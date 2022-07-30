package moe.ahao.util.commons.lang.time;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.*;
import java.time.DateTimeException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiDateFormat extends DateFormat {
    private static final Logger logger = LoggerFactory.getLogger(MultiDateFormat.class);

    private final List<DateFormat> dateFormatList;

    public MultiDateFormat() {
        this(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new StdDateFormat()
        ));
    }

    public MultiDateFormat(List<DateFormat> dateFormatList) {
        this(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), dateFormatList);
    }

    public MultiDateFormat(DateFormat defaultDateFormat, List<DateFormat> dateFormatList) {
        this.dateFormatList = new ArrayList<>();
        this.dateFormatList.add(defaultDateFormat);
        this.dateFormatList.addAll(dateFormatList);

        this.calendar = defaultDateFormat.getCalendar();
        this.numberFormat = defaultDateFormat.getNumberFormat();
    }

    public <R> R execute(Function<DateFormat, R> function) {
        for (DateFormat dateFormat : dateFormatList) {
            R result = function.apply(dateFormat);
            if(result != null) {
                return result;
            }
        }
        throw new DateTimeException("所有DateFormat都处理失败");
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return this.execute(s -> s.format(date, toAppendTo, fieldPosition));
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return this.execute(s -> s.parse(source, pos));
    }

    @Override
    public void setCalendar(Calendar newCalendar) {
        this.execute(dateFormat -> {
            dateFormat.setCalendar(newCalendar);
            return null;
        });
    }

    @Override
    public Calendar getCalendar() {
        return dateFormatList.stream().findFirst().map(DateFormat::getCalendar).orElse(null);
    }

    @Override
    public void setNumberFormat(NumberFormat newNumberFormat) {
        this.execute(dateFormat -> {
            dateFormat.setNumberFormat(newNumberFormat);
            return null;
        });
    }

    @Override
    public NumberFormat getNumberFormat() {
        return dateFormatList.stream().findFirst().map(DateFormat::getNumberFormat).orElse(null);
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        this.execute(dateFormat -> {
            dateFormat.setTimeZone(zone);
            return null;
        });
    }

    @Override
    public TimeZone getTimeZone() {
        return dateFormatList.stream().findFirst().map(DateFormat::getTimeZone).orElse(TimeZone.getDefault());
    }

    @Override
    public void setLenient(boolean lenient) {
        this.execute(dateFormat -> {
            dateFormat.setLenient(lenient);
            return null;
        });
    }

    @Override
    public boolean isLenient() {
        return dateFormatList.stream().findFirst().map(DateFormat::isLenient).orElse(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MultiDateFormat that = (MultiDateFormat) o;
        return Objects.equals(dateFormatList, that.dateFormatList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateFormatList);
    }

    @Override
    public Object clone() {
        return new MultiDateFormat(this.dateFormatList.stream()
            .map(DateFormat::clone)
            .map(df -> (DateFormat) df)
            .collect(Collectors.toList()));
    }
}
