package vip.zhouxin.ureport.core.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xinxingzhou
 * @since 2022/1/25
 */
public class ServletUtils {

    public static ServletRequestAttributes getServletRequestAttributes(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }
}
