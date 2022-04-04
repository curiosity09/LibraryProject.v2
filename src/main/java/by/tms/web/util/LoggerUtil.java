package by.tms.web.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggerUtil {

    public static final String ENTITY_WAS_SAVED_IN_CONTROLLER = "Entity was saved in controller :{}";
    public static final String ENTITY_WAS_UPDATED_IN_CONTROLLER = "Entity was updated in controller :{}";
    public static final String ENTITY_WAS_DELETED_IN_CONTROLLER = "Entity was deleted in controller :{}";
    public static final String ENTITY_WAS_FOUND_IN_CONTROLLER_BY = "Entity was found in controller :{}, :{}";
    public static final String USER_WAS_BLOCKED_IN_CONTROLLER = "User was blocked in controller :{}";
}
