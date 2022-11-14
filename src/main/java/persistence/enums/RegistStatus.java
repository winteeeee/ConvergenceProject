package persistence.enums;

import lombok.Getter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum RegistStatus implements Enum {
    HOLD(0, "보류"),
    ACCEPT(1, "수락"),
    REJECT(2, "거절");

    private static final Map<Integer, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(RegistStatus::getCode, RegistStatus::name))
    );

    public static RegistStatus of(int code) {
        return RegistStatus.valueOf(CODE_MAP.get(code));
    }

    private final int code;
    private final String title;

    RegistStatus(int code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getName() {
        return name();
    }
}