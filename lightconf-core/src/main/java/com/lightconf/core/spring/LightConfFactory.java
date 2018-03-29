package com.lightconf.core.spring;

import com.lightconf.core.LightConfClient;
import com.lightconf.core.XxlConfClient;
import com.lightconf.core.annotaion.XxlConf;
import com.lightconf.core.core.LightConfLocalCacheConf;
import com.lightconf.core.listener.impl.AnnoRefreshXxlConfListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * rewrite PropertyPlaceholderConfigurer
 *
 * @author xuxueli 2015-9-12 19:42:49
 */
public class LightConfFactory extends PropertySourcesPlaceholderConfigurer {
	private static Logger logger = LoggerFactory.getLogger(LightConfFactory.class);

	public void init() {
	}

	public void destroy(){
		LightConfLocalCacheConf.destroy();
//		XxlConfZkClient.destroy();
	}

	/**
	 * xxl conf BeanDefinitionVisitor
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
				// loop replace by xxl-conf, if the value match '${***}'
				boolean start = strVal.startsWith(placeholderPrefix);
				boolean end = strVal.endsWith(placeholderSuffix);
				while (start && end) {
					// replace by xxl-conf
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
		BeanDefinitionVisitor xxlConfVisitor = new BeanDefinitionVisitor(lightConfValueResolver);
		return xxlConfVisitor;
	}

	/**
	 * refresh bean with xxl conf
	 *
	 * @param beanWithXxlConf
	 */
	public static void refreshBeanWithXxlConf(final Object beanWithXxlConf, final String key){

		ReflectionUtils.doWithFields(beanWithXxlConf.getClass(), new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				if (field.isAnnotationPresent(XxlConf.class)) {
					XxlConf xxlConf = field.getAnnotation(XxlConf.class);
					String confKey = xxlConf.value();

					// key not match, not allow refresh
					if (key!=null && !key.equals(confKey)) {
						return;
					}

					String confValue = XxlConfClient.get(confKey, xxlConf.defaultValue());

					field.setAccessible(true);
					field.set(beanWithXxlConf, confValue);
					logger.info(">>>>>>>>>>> light-conf, refreshBeanWithXxlConf success, [{}={}]", confKey, confValue);
					if (xxlConf.callback()) {
						AnnoRefreshXxlConfListener.addKeyObject(confKey, beanWithXxlConf);
					}
				}
			}
		});
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws BeansException {
		//super.processProperties(beanFactoryToProcess, propertyResolver);

		// xxl conf BeanDefinitionVisitor
		BeanDefinitionVisitor lightConfVisitor = getLightConfBeanDefinitionVisitor();

		// visit bean definition
		String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
		if (beanNames != null && beanNames.length > 0) {
			for (String beanName : beanNames) {
				if (!(beanName.equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {

					// XML：resolves '${...}' placeholders within bean definition property values
					BeanDefinition beanDefinition = beanFactoryToProcess.getBeanDefinition(beanName);
                    lightConfVisitor.visitBeanDefinition(beanDefinition);

					// Annotation：resolves '@XxlConf' annotations within bean definition fields
					final Object beanWithLightConf = beanFactoryToProcess.getBean(beanName);
					refreshBeanWithXxlConf(beanWithLightConf, null);	// refresh bean with xxl conf

                    // Annotation：resolves '@Value' annotations within bean definition fields
                    final Object springAnnotation = beanFactoryToProcess.getBean(beanName);
                    refreshBeanWithSpringAnnotation(springAnnotation, null);	// refresh bean with xxl conf
				}
			}
		}

		logger.info(">>>>>>>>>>> light-conf, XxlConfFactory process success");
	}

    private static void refreshBeanWithSpringAnnotation(final Object springAnnotation, Object object) {
	    ReflectionUtils.doWithFields(springAnnotation.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

            }
        });
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
