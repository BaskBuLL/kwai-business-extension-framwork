package com.kuaishou.business.core.utils;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;

import com.kuaishou.business.core.annotations.KExtPoint;
import com.kuaishou.business.core.exception.IllegalExtPointException;

public final class ExtPointUtils {

	/**
	 *
	 * @param clazz class
	 * @return true/false
	 */
    public static boolean isExt(Class<?> clazz) {
        return Objects.nonNull(clazz) && clazz.isInterface() && Objects.nonNull(clazz.getAnnotation(KExtPoint.class));
    }

	/**
	 *
	 * @param clazz class
	 */
    public static void checkExt(Class<?> clazz) {
        if (!isExt(clazz)) {
            throw new IllegalExtPointException(clazz.getCanonicalName());
        }
        final List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(clazz);
        if (Objects.isNull(allSuperclasses) || allSuperclasses.isEmpty()) {
            return;
        }
        //扩展点不支持相互继承
        if (allSuperclasses.stream().anyMatch(ExtPointUtils::isExt)) {
            throw new IllegalExtPointException(clazz.getCanonicalName());
        }
    }
}
