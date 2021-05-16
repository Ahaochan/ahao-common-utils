package moe.ahao.util.commons.lang.time;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

public class MultiDateFormat extends DateFormat {
    private static final Logger logger = LoggerFactory.getLogger(MultiDateFormat.class);

    private Collection<DateFormat> dateFormatList;
    private DateFormat defaultDateFormat;


    public MultiDateFormat() {
        this(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new StdDateFormat()
        ));
    }

    public MultiDateFormat(Collection<DateFormat> dateFormatList) {
        this(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), dateFormatList);
    }

    public MultiDateFormat(DateFormat defaultDateFormat, Collection<DateFormat> dateFormatList) {
        this.defaultDateFormat = defaultDateFormat;
        this.dateFormatList = dateFormatList;

        this.calendar = defaultDateFormat.getCalendar();
        this.numberFormat = defaultDateFormat.getNumberFormat();
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return this.execute(d -> d.format(date, toAppendTo, fieldPosition));
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return this.execute(d -> d.parse(source, pos));
    }

    public <R> R execute(Function<DateFormat, R> function) {
        R defaultResult = function.apply(defaultDateFormat);
        if(defaultResult != null) {
            return defaultResult;
        }

        for (DateFormat dateFormat : dateFormatList) {
            R result = function.apply(dateFormat);
            if(result != null) {
                return result;
            }
        }
        throw new DateTimeException("");
    }
}
