package moe.ahao.exception;

import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExceptionCodeChecker implements CommandLineRunner {
    private final Reflections reflections = new Reflections();
    @Override
    public void run(String... args) throws Exception {
        List<? extends BizExceptionEnum> enums = reflections.getSubTypesOf(BizExceptionEnum.class).stream()
            .map(Class::getEnumConstants)
            .map(Arrays::asList)
            .flatMap(List::stream)
            .collect(Collectors.toList());
        Map<Integer, BizExceptionEnum> map = new HashMap<>();
        for (BizExceptionEnum value : enums) {
            if(map.containsKey(value.getCode())) {
                String formatStr = "异常编码code:%s同时存在于 %s 和 %s 中";
                throw new IllegalArgumentException(String.format(formatStr, value.getCode(), map.get(value.getCode()).getClass(), value.getClass()));
            } else {
                map.put(value.getCode(), value);
            }
        }
    }
}
