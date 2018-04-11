package com.lightconf.core.spring;

import com.lightconf.core.LightConfClient;
import com.lightconf.core.annotaion.LightConf;
import com.lightconf.core.core.LightConfLocalCacheConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Field;

/**
 * rewrite PropertyPlaceholderConfigurer.
 *
 * @author wuhaifei
 */
public class LightConfFactory extends PropertySourcesPlaceholderConfigurer {
	private static Logger logger = LoggerFactory.getLogger(LightConfFactory.class);

	public void init() {
	}

	public void destroy(){
		LightConfLocalCacheConf.destroy();
//		lightConfZkClient.destroy();
	}

	/**
	 * light conf BeanDefinitionVisitor
	 *
	 * @return
	 */
	private static BeanDefinitionVisitor getLightConfBeanDefinitionVisitor() {
		// light conf StringValueResolver
		StringValueResolver lightConfValueResolver = new StringValueResolver() {
			String placeholderPrefix = "${";
			String placeholderSuffix = "}";
			@Override
			public String resolveStringValue(String strVal) {
				StringBuffer buf = new StringBuffer(strVal);
				// loop replace by light-conf, if the value match '${***}'
				boolean start = strVal.startsWith(placeholderPrefix);
				boolean end = strVal.endsWith(placeholderSuffix);
				while (start && end) {
					// replace by light-conf
					String key = buf.substring(placeholderPrefix.length(), buf.length() - placeholderSuffix.length());
					String value = LightConfClient.get(key, "");
					buf = new StringBuffer(value);
					logger.info(">>>>>>>>>>> light-conf, resolved placeholder success, [{}={}]", key, value);
					start = buf.toString().startsWith(placeholderPrefix);
					end = buf.toString().endsWith(placeholderSuffix);
				}
				return buf.toString();
			}
		};

		// light conf BeanDefinitionVisitor
		BeanDefinitionVisitor lightConfVisitor = new BeanDefinitionVisitor(lightConfValueResolver);
		return lightConfVisitor;
	}

	/**
	 * refresh bean with light conf
	 *
	 * @param beanWithlightConf
	 */
	public static void refreshBeanWithLightConf(final Object beanWithlightConf, final String key){

		ReflectionUtils.doWithFields(beanWithlightConf.getClass(), new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				if (field.isAnnotationPresent(LightConf.class)) {
                    LightConf lightConf = field.getAnnotation(LightConf.class);
					String confKey = lightConf.value();

					// key not match, not allow refresh
					if (key!=null && !key.equals(confKey)) {
						return;
					}

					String confValue = LightConfClient.get(confKey, lightConf.defaultValue());

					field.setAccessible(true);
					field.set(beanWithlightConf, confValue);
					logger.info(">>>>>>>>>>> light-conf, refreshBeanWithlightConf success, [{}={}]", confKey, confValue);
//					if (lightConf.callback()) {
//						AnnoRefreshXxlConfListener.addKeyObject(confKey, beanWithlightConf);
//					}
				}
			}
		});
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws BeansException {
		//super.processProperties(beanFactoryToProcess, propertyResolver);

		// light conf BeanDefinitionVisitor
		BeanDefinitionVisitor lightConfVisitor = getLightConfBeanDefinitionVisitor();

		// visit bean definition
		String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
		if (beanNames != null && beanNames.length > 0) {
			for (String beanName : beanNames) {
				if (!(beanName.equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {

					// XML and @Value：resolves '${...}' placeholders within bean definition property values
					BeanDefinition beanDefinition = beanFactoryToProcess.getBeanDefinition(beanName);
                    lightConfVisitor.visitBeanDefinition(beanDefinition);

					// Annotation：resolves '@LightConf' annotations within bean definition fields
//					final Object beanWithLightConf = beanFactoryToProcess.getBean(beanName);
                    // refresh bean with light conf
//                    refreshBeanWithLightConf(beanWithLightConf, null);
				}
			}
		}

		logger.info(">>>>>>>>>>> light-conf, lightConfFactory process success");
	}


    @Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	private String beanName;

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	private BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
		super.setIgnoreUnresolvablePlaceholders(true);
	}



}
