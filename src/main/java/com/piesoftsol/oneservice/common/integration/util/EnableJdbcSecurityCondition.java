package com.piesoftsol.oneservice.common.integration.util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.piesoftsol.oneservice.common.integration.annotations.EnableJdbc;
import com.piesoftsol.oneservice.common.integration.annotations.EnableJdbcSecurity;
import com.piesoftsol.oneservice.common.integration.annotations.IgnoreSecurity;
import com.piesoftsol.oneservice.common.integration.annotations.EnablePropSecurity;

import static com.piesoftsol.oneservice.common.integration.config.OneServiceInit.oneServiceBootClass;

public class EnableJdbcSecurityCondition implements Condition {

	private static final String METHOD = "EnableSecurityCondition";
	
	private static final AppLogger LOGGER = new AppLogger(Condition.class.getName());
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		
		String message = "";
		if(oneServiceBootClass == null) {
			return false;
		}

		if (oneServiceBootClass.isAnnotationPresent(EnableJdbcSecurity.class)) {
			try {
				if (!oneServiceBootClass.isAnnotationPresent(EnableJdbc.class)) {
					message = "EnableJdbcSecurity Requires JDBC enable. Please add @EnableJdbc";
					LOGGER.error(METHOD, message);
					throw new IllegalAccessException(message);
				}else {
					if (oneServiceBootClass.isAnnotationPresent(IgnoreSecurity.class) || oneServiceBootClass.isAnnotationPresent(EnablePropSecurity.class)) {
						message = "IgnoreSecurity can't combined with Anyother security methods. Please removed other security Methods.";
						LOGGER.error(METHOD, message);
						throw new IllegalAccessException(message);
					}else {
						return true;
					}
				}
			}catch(IllegalAccessException e) 
	        {
	            try {
					throw e;
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.exit(-100);
				} // rethrowing the exception 
	        } 
		}
		
		return false;
	}

}