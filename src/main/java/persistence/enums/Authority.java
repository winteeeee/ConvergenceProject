package persistence.enums;

import lombok.Getter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Authority implements Enum {
    ADMIN(0, "관리자"),
    OWNER(1, "점주"),
    USER(2, "사용자");

    private static final Map<Integer, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Authority::getCode, Authority::name))
    );

    public static Authority of(int code) {
        return Authority.valueOf(CODE_MAP.get(code));
    }

    private final int code;
    private final String title;

    Authority(int code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getName() {
        return name();
    }
}