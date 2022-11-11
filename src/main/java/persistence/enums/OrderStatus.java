package persistence.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum OrderStatus implements Enum {
    HOLD(0, "접수대기"),
    CANCEL(1, "취소"),
    IN_DELIVERY(2, "배달중"),
    COMPLETE(3, "배달완료");

    private static final Map<Integer, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(OrderStatus::getCode, OrderStatus::name))
    );

    public static OrderStatus of(int code) {
        return OrderStatus.valueOf(CODE_MAP.get(code));
    }

    private final int code;
    private final String title;

    OrderStatus(int code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getName() {
        return name();
    }
}