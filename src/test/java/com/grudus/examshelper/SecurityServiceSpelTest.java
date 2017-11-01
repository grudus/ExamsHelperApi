package com.grudus.examshelper;

import com.grudus.examshelper.exams.ExamsSecurityService;
import com.grudus.examshelper.subjects.SubjectSecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityServiceSpelTest extends SpringBasedTest {

    private final static Map<Class<?>, Pattern> classToPattern = new HashMap<>();

    static {
        classToPattern.put(SubjectSecurityService.class, Pattern.compile("@subjectSecurityService\\.hasAccessToSubject\\(#(\\w+.?)+,\\s+#(\\w+.?)+\\)"));
        classToPattern.put(ExamsSecurityService.class, Pattern.compile("@examsSecurityService\\.hasAccessToExam\\(#(\\w+.?)+,\\s+#(\\w+.?)+\\)"));
    }


    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void standardSpelShouldBeValid() {
        Map<String, Object> restControllers = applicationContext.getBeansWithAnnotation(RestController.class);

        restControllers.forEach((name, bean) -> {
            Method[] methods = AopUtils.getTargetClass(bean).getMethods();

            for (Method method : methods) {
                PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);

                if (preAuthorize != null) {
                    String securityExpression = preAuthorize.value();
                    checkExpression(securityExpression);
                }

            }
        });
    }

    private void checkExpression(String securityExpression) {
        Pattern pattern = classToPattern.entrySet().stream().filter(entry -> expressionStartsWith(securityExpression, entry.getKey()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find valid pattern for expression [" + securityExpression + "]"))
                .getValue();

        assertTrue(pattern.matcher(securityExpression).matches(), format("[%s] expression doesn't match pattern [%s]", securityExpression, pattern.pattern()));
    }

    private boolean expressionStartsWith(String securityExpression, Class<?> aClass) {
        return securityExpression.toLowerCase().startsWith("@" + aClass.getSimpleName().toLowerCase());
    }

}
