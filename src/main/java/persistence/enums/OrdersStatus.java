package persistence.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum OrdersStatus implements Enum {
    HOLD( "접수대기"),
    IN_DELIVERY("배달중"),
    CANCEL("취소"),
    COMPLETE("배달완료");

    private static final Map<String, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(OrdersStatus::getCode, OrdersStatus::name))
    );

    public static OrdersStatus of(String code) {
        return OrdersStatus.valueOf(CODE_MAP.get(code));
    }

    private final String code;

    OrdersStatus(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name();
    }
}