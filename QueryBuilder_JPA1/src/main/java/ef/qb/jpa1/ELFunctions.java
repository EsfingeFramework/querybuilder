package ef.qb.jpa1;

import java.util.Map;

public class ELFunctions {

    public static boolean onlyNullValues(Map<String, Object> map) {
        for (var key : map.keySet()) {
            if (map.get(key) != null) {
                return false;
            }
        }
        return true;
    }

}
