package org.heyu.ig.core;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.heyu.ig.core.service.UipRecordService;
import org.heyu.ig.core.util.DateTimeUtil;
import org.heyu.ig.core.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author heyu
 */
@Aspect
@Component
public class UipRequestAopLog {

	@Autowired
	private UipRecordService uipRecordService;

	/**
	 * 切入点
	 */
	@Pointcut("@annotation(org.heyu.ig.core.UipRequestLog)")
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
				long end = System.currentTimeMillis();
				if (result != null) {
					if (result instanceof String) {
						uipRequestRecord.setResponse((String) result);
					} else {
						uipRequestRecord.setResponse(JsonUtil.obj2json(result));
					}
				}
				uipRequestRecord.setMilli(end - start);
				uipRequestRecord.setResponseTime(DateTimeUtil.date2dateTimeStr(new Date(end)));
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
			uipRequestRecord.setType(UipRequestTypeEnum.REQUEST.getCode());
			Class<?> clazz = Class.forName(point.getSignature().getDeclaringTypeName());
			Method[] methods = clazz.getDeclaredMethods();
			Map<Integer, String> paramMap = new HashMap<>(10);
			HttpMethod httpMethod = HttpMethod.GET;
			Integer urlPlace = null;
			Integer bodyPlace = null;
			for (Method method : methods) {
				if (!Objects.equals(method.getName(), point.getSignature().getName())) {
					continue;
				}
				UipRequestLog uipRequestLog = method.getAnnotation(UipRequestLog.class);
				if (uipRequestLog == null) {
					continue;
				}
				uipRequestRecord.setCode(uipRequestLog.code());
				Parameter[] parameters = method.getParameters();
				for (int i = 0; i < parameters.length; i++) {
					Parameter parameter = parameters[i];
					UipRequestUrl uipRequestUrl = parameter.getAnnotation(UipRequestUrl.class);
					if (uipRequestUrl != null) {
						httpMethod = uipRequestUrl.value();
						urlPlace = i;
					}
					UipRequestBody uipRequestBody = parameter.getAnnotation(UipRequestBody.class);
					if (uipRequestBody != null) {
						bodyPlace = i;
					}
					String name = parameter.getName();
					paramMap.put(i, name);
				}
				break;
			}
			Map<String, Object> paramMap2 = new HashMap<>(10);
			Object[] a = point.getArgs();
			if (urlPlace != null && a[urlPlace] != null) {
				uipRequestRecord.setUrl(a[urlPlace].toString());
			}
			if (bodyPlace != null && a[bodyPlace] != null) {
				Object body = a[bodyPlace];
				if (body instanceof String) {
					uipRequestRecord.setBody((String) body);
				} else {
					uipRequestRecord.setBody(JsonUtil.obj2json(body));
				}

			}
			for (int i = 0; i < a.length; i++) {
				if (Objects.equals(i, urlPlace)) {
					continue;
				}
				if (Objects.equals(i, bodyPlace)) {
					continue;
				}
				Object obj = a[i];
				String name = paramMap.get(i);
				paramMap2.put(name, obj);
			}
			String requestId = UUID.randomUUID().toString();
			uipRequestRecord.setMethod(httpMethod.name());
			uipRequestRecord.setParam(JsonUtil.obj2json(paramMap2));
			uipRequestRecord.setRequestId(requestId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
