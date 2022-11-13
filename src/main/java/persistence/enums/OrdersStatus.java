package persistence.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum OrdersStatus implements Enum {
    HOLD(0, "접수대기"),
    CANCEL(1, "취소"),
    IN_DELIVERY(2, "배달중"),
    COMPLETE(3, "배달완료");

    private static final Map<Integer, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(OrdersStatus::getCode, OrdersStatus::name))
    );

    public static OrdersStatus of(int code) {
        return OrdersStatus.valueOf(CODE_MAP.get(code));
    }

    private final int code;
    private final String title;

    OrdersStatus(int code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getName() {
        return name();
    }
}