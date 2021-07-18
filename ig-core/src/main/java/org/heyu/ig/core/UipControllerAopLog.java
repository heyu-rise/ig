package org.heyu.ig.core;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.heyu.ig.core.service.UipRecordService;
import org.heyu.ig.core.util.DateTimeUtil;
import org.heyu.ig.core.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author heyu
 */
@Aspect
@Component
public class UipControllerAopLog {

	private static final String UNKNOWN = "unknown";

	@Autowired
	private UipRecordService uipRecordService;

	/**
	 * 切入点
	 */
	@Pointcut("@annotation(org.heyu.ig.core.UipControllerLog)")
	public void log() {

	}


	/**
	 * 环绕操作
	 *
	 * @param point
	 *            切入点
	 * @return 原方法返回值
	 * @throws Throwable
	 *             异常信息
	 */
	@Around("log()")
	public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
		UipRequestRecord uipRequestRecord = new UipRequestRecord();
		long start = System.currentTimeMillis();
		aroundBefore(uipRequestRecord, point);
		Object result = null;
		try {
			result = point.proceed();
			uipRequestRecord.setSuccess(true);
		} finally {
			try {
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
				if (result != null) {
					if (result instanceof String) {
						uipRequestRecord.setResponse((String) result);
					} else {
						uipRequestRecord.setResponse(JsonUtil.obj2json(result));
					}
				}
				long end = System.currentTimeMillis();
				uipRequestRecord.setMilli(end - start);
				uipRequestRecord.setResponseTime(DateTimeUtil.date2dateTimeStr(new Date(end)));
				String header = request.getHeader("User-Agent");
				uipRequestRecord.setUserAgent(header);
				uipRecordService.add(uipRequestRecord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private void aroundBefore(UipRequestRecord uipRequestRecord, JoinPoint point) {
		try {
			uipRequestRecord.setRequestTime(DateTimeUtil.date2dateTimeStr(new Date()));
			uipRequestRecord.setType(UipRequestTypeEnum.CONTROLLER.getCode());
			Class<?> clazz = Class.forName(point.getSignature().getDeclaringTypeName());
			Method[] methods = clazz.getDeclaredMethods();
			Integer requestBodyPlace = null;
			for (Method method : methods) {
				if (!Objects.equals(method.getName(), point.getSignature().getName())) {
					continue;
				}
				UipControllerLog uipControllerLog = method.getAnnotation(UipControllerLog.class);
				if (uipControllerLog == null) {
					continue;
				}
				Parameter[] parameters = method.getParameters();
				for (int i = 0; i < parameters.length; i++) {
					Parameter parameter = parameters[i];
					RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
					if (requestBody == null) {
						continue;
					}
					requestBodyPlace = i;
				}
				uipRequestRecord.setCode(uipControllerLog.code());
				break;
			}
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
			Map<String, String[]> parameterMap = request.getParameterMap();
			String requestId = UUID.randomUUID().toString();
			uipRequestRecord.setRequestId(requestId);
			uipRequestRecord.setMethod(request.getMethod());
			uipRequestRecord.setUrl(request.getRequestURL().toString());
			uipRequestRecord.setIp(getIp(request));
			if (requestBodyPlace != null && point.getArgs().length > requestBodyPlace) {
				Object a = point.getArgs()[requestBodyPlace];
				if (a != null) {
					String body = null;
					if (a instanceof String) {
						body = (String) a;
					} else {
						body = JsonUtil.obj2json(a);
					}
					uipRequestRecord.setBody(body);
				}
			}
			uipRequestRecord.setParam(JsonUtil.obj2json(parameterMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
