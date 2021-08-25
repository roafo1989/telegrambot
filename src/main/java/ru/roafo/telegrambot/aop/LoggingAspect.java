package ru.roafo.telegrambot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.roafo.telegrambot.command.Command;
import ru.roafo.telegrambot.domain.TelegramUser;

import java.lang.reflect.Method;

@Component
@Aspect
public class LoggingAspect {

    public final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* sendMessage(..))")
    public void sendMessagePointcut() {
    }

    @Pointcut("execution(* getTelegramUser(..))")
    public void getTelegramUserPointcut() {
    }

    @Before("sendMessagePointcut() || getTelegramUserPointcut()")
    public void beforeSendMessageAndGetTelegramUser(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("<<====================");
        log.info("START method {}", methodSignature);
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            log.info("{} = {}", parameterNames[i], args[i]);
        }
        log.info("====================>>");
    }

    @Before("execution(* onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update))")
    public void beforeOnUpdateReceived(JoinPoint joinPoint) {
        log.info("************************before execute************************");
    }

    @Before("execution(* findNewArticles())")
    public void beforeFindNewArticles(JoinPoint joinPoint) {
        log.info("************************BEFORE FIND_NEW_ARTICLE************************");
    }



    @Around("execution(* retrieveCommand(*))")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        log.info("<<====================");
        log.info("START method {}", methodSignature);
        Object[] args = proceedingJoinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            log.info("{} = {}", parameterNames[i], args[i]);
        }

        Object target = proceedingJoinPoint.proceed();

        log.info("RETURN COMMAND {}", target);
        log.info("====================>>");
        return target;
    }




    @AfterReturning(pointcut = "execution(* getTelegramUser(*))", returning = "telegramUser")
    public void afterGetTelegramUser(TelegramUser telegramUser) {
        log.info("RETURN USER {}", telegramUser);
        log.info("====================>>");
    }

    @AfterReturning(pointcut = "execution(* getUserName(*))", returning = "userName")
    public void afterGetUserName(String userName) {
        log.info("RETURN USER {}", userName);
        log.info("====================>>");
    }

    @AfterThrowing(pointcut = "execution(* execute(..))", throwing = "exception")
    public void afterThrowingException(JoinPoint joinPoint, Throwable exception) {
        log.info("<<====================");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.error("METHOD: {} EXCEPTION: {}", methodSignature.getName(), exception.getMessage());
        for (StackTraceElement e : exception.getStackTrace()) {
            log.error("{}", e);
        }
        log.info("====================>>");
    }


}
