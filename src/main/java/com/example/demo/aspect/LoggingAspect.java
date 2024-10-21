package com.example.demo.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// Este ponto de corte abrange todos os métodos públicos de qualquer classe na camada de serviço, repositório, e controlador. A expressão "execution(* <pacote>.Classe.Método(..))" especifica que todos os métodos dentro desses pacotes serão interceptados.
	@Pointcut("execution(* com.example.demo.service..*(..))"
			+ " || execution(* com.example.demo.repository..*(..))"
			+ " || execution(* com.example.demo.controller..*(..))")
	public void springBeanPointcut() {
	}

	@AfterThrowing(pointcut = "springBeanPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		logger.error("Exceção em {}.{}() com causa = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	@Around("springBeanPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		if (logger.isDebugEnabled()) {
			logger.debug("Entrando: {}.{}() com argumento[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		}
		Object result = joinPoint.proceed();
		if (logger.isDebugEnabled()) {
			logger.debug("Saindo: {}.{}() com resultado = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), result);
		}
		return result;
	}
}
