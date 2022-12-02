package persistence.enums;

import lombok.Getter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum RegistStatus implements Enum {
    HOLD("보류"),
    ACCEPT("수락"),
    REJECT("거절");

    private static final Map<String, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(RegistStatus::getCode, RegistStatus::name))
    );

    public static RegistStatus of(String code) {
        return RegistStatus.valueOf(CODE_MAP.get(code));
    }

    private final String code;

    RegistStatus(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name();
    }
}