package com.gucardev.tapucase.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gucardev.tapucase.exception.ExceptionMessages;
import com.gucardev.tapucase.exception.UserNotMatchedException;
import com.gucardev.tapucase.service.ShortUrlService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Map;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class CheckOwnerAspect {

    private final ShortUrlService shortUrlService;

    @Around("@annotation(CheckOwner)")
    public Object checkOwner(ProceedingJoinPoint joinPoint) throws Throwable {
        Long userId;
        Long userIdOfShortUrl;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        var fields = parameters(request);

        userId = Long.valueOf(fields.get("user_id"));
        userIdOfShortUrl = shortUrlService.getUrlById(Long.valueOf(fields.get("url_id"))).getUser().getId();


        if (!userId.equals(userIdOfShortUrl)) {
            log.error(userId + " is different than " + userIdOfShortUrl);
            throw new UserNotMatchedException(ExceptionMessages.USER_NOT_MATCHED.getValue());
        } else {
            log.info("Users are matching");
        }

        return joinPoint.proceed();
    }


    // http servlet parameters processing
    public static Map<String, String> parameters(HttpServletRequest request) throws JsonProcessingException {
        var raw = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        ObjectMapper mapper = new ObjectMapper();
        String data = null;
        try {
            data = mapper.writeValueAsString(raw);
            log.info("Parameters: " + data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ObjectMapper().readValue(data, Map.class);
    }

}
