package by.tms.model.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggerUtil {

    public static final String ENTITY_WAS_SAVED_IN_DAO = "Entity was saved in dao layer :{}";
    public static final String ENTITY_WAS_UPDATED_IN_DAO = "Entity was updated in dao layer :{}";
    public static final String ENTITY_WAS_DELETED_IN_DAO = "Entity was deleted in dao layer :{}";
    public static final String USER_WAS_BLOCKED_IN_DAO = "User was blocked in dao layer :{}";
    public static final String USER_WAS_UNBLOCKED_IN_DAO = "User was unblocked in dao layer :{}";
    public static final String ENTITY_WAS_FOUND_IN_DAO_BY = "Entity was found in dao layer :{}, :{}";
    public static final String ENTITY_WAS_FOUND_IN_DAO = "Entity was found in dao layer :{}";
    public static final String COUNT_ROW_WAS_FOUND_IN_DAO = "Count row was found in dao layer :{}";
    public static final String ENTITY_IS_EXIST_IN_DAO = "Entity is exist in dao layer :{}";
    public static final String ENTITY_WAS_FOUND_IN_SERVICE = "Entity was found in service layer :{}";
    public static final String ENTITY_WAS_FOUND_IN_SERVICE_BY = "Entity was found in service layer :{}, :{}";
    public static final String ENTITY_WAS_SAVED_IN_SERVICE = "Entity was saved in service layer :{}";
    public static final String ENTITY_WAS_UPDATED_IN_SERVICE = "Entity was updated in service layer :{}";
    public static final String ENTITY_WAS_DELETED_IN_SERVICE = "Entity was deleted in service layer :{}";
    public static final String COUNT_ROW_WAS_FOUND_IN_SERVICE = "Count row was found in service layer :{}";
    public static final String ENTITY_IS_EXIST_IN_SERVICE_BY = "Entity is exist in service layer :{}, :{}";
    public static final String USER_WAS_BLOCKED_IN_SERVICE = "User was blocked in service layer :{}";
    public static final String USER_WAS_UNBLOCKED_IN_SERVICE = "User was unblocked in service layer :{}";
}
