package persistence.enums;

import lombok.Getter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Authority implements Enum{
    ADMIN("관리자"),
    OWNER("점주"),
    USER("사용자");

    private static final Map<String, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Authority::getCode, Authority::name))
    );

    public static Authority of(String code) {
        return Authority.valueOf(CODE_MAP.get(code));
    }

    private final String code;

    Authority(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name();
    }
}