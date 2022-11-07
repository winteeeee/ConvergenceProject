package persistence.enums;

import lombok.Getter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Status implements Enum {
    ACCEPT(0, "수락"),
    REJECT(1, "거절"),
    HOLD(2, "보류");

    private static final Map<Integer, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Status::getCode, Status::name))
    );

    public static Status of(int code) {
        return Status.valueOf(CODE_MAP.get(code));
    }

    private final int code;
    private final String title;

    Status(int code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getName() {
        return name();
    }
}