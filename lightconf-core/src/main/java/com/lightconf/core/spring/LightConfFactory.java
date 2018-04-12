package com.lightconf.core.spring;

import com.lightconf.core.LightConfClient;
import com.lightconf.core.annotaion.LightConf;
import com.lightconf.core.core.LightConfLocalCacheConf;
import com.lightconf.core.exception.LightConfException;
import com.lightconf.core.exception.XxlConfException;
import com.lightconf.core.listener.LightConfListenerFactory;
import com.lightconf.core.listener.impl.BeanRefreshLightConfListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * rewrite PropertyPlaceholderConfigurer.
 *
 * @author wuhaifei
 */
public class LightConfFactory extends PropertySourcesPlaceholderConfigurer {
	private static Logger logger = LoggerFactory.getLogger(LightConfFactory.class);

	public void init() {
		// listener all key change
		LightConfListenerFactory.addListener(null, new BeanRefreshLightConfListener());
	}

	public void destroy(){
		LightConfLocalCacheConf.destroy();
//		lightConfZkClient.destroy();
	}

	private static final String placeholderPrefix = "${";
	private static final String placeholderSuffix = "}";
	private static boolean xmlKeyValid(String originKey){
		boolean start = originKey.startsWith(placeholderPrefix);
		boolean end = originKey.endsWith(placeholderSuffix);
		if (start && end) {
			return true;
		}
		return false;
	}

	private static String xmlKeyParse(String originKey){
		if (xmlKeyValid(originKey)) {
			// replace by xxl-conf
			String key = originKey.substring(placeholderPrefix.length(), originKey.length() - placeholderSuffix.length());
			return key;
		}
		return null;
	}

	public static void refreshBeanField(BeanRefreshLightConfListener.BeanField beanField, String value) {
		Object bean = beanFactory.getBean(beanField.getBeanName());
		if (bean != null) {
			BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

			// property descriptor
			PropertyDescriptor propertyDescriptor = null;
			PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
			if (propertyDescriptors!=null && propertyDescriptors.length>0) {
				for (PropertyDescriptor item: propertyDescriptors) {
					if (beanField.getProperty().equals(item.getName())) {
						propertyDescriptor = item;
					}
				}
			}

			// refresh field: set or field
			if (propertyDescriptor!=null && propertyDescriptor.getWriteMethod() != null) {
				beanWrapper.setPropertyValue(beanField.getProperty(), value);
				logger.info(">>>>>>>>>>> xxl-conf, refreshBeanField[set] success, {}#{}:{}",
						beanField.getBeanName(), beanField.getProperty(), value);
			} else {
				Field[] beanFields = bean.getClass().getDeclaredFields();
				if (beanFields!=null && beanFields.length>0) {
					for (Field fieldItem: beanFields) {
						if (beanField.getProperty().equals(fieldItem.getName())) {
							fieldItem.setAccessible(true);
							try {
								fieldItem.set(bean, value);
								logger.info(">>>>>>>>>>> xxl-conf, refreshBeanField[field] success, {}#{}:{}",
										beanField.getBeanName(), beanField.getProperty(), value);
							} catch (IllegalAccessException e) {
								throw new LightConfException(e);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws BeansException {

		// visit bean definition
		String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
		if (beanNames != null && beanNames.length > 0) {
			for (final String beanName : beanNames) {
				if (!(beanName.equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {

					// 1、XML('${...}')：resolves placeholders + watch
					BeanDefinition beanDefinition = beanFactoryToProcess.getBeanDefinition(beanName);

					MutablePropertyValues pvs = beanDefinition.getPropertyValues();
					PropertyValue[] pvArray = pvs.getPropertyValues();
					for (PropertyValue pv : pvArray) {
						if (pv.getValue() instanceof TypedStringValue) {
							String propertyName = pv.getName();
							String typeStringVal = ((TypedStringValue) pv.getValue()).getValue();
							if (xmlKeyValid(typeStringVal)) {

								// object + property
								String confKey = xmlKeyParse(typeStringVal);
								String confValue = LightConfClient.get(confKey, "");

								// resolves placeholders
								pvs.add(pv.getName(), confValue);

								// watch
								BeanRefreshLightConfListener.BeanField beanField = new BeanRefreshLightConfListener.BeanField(beanName, propertyName);
								BeanRefreshLightConfListener.addBeanField(confKey, beanField);
							}
						}
					}

					// 2、Annotation('@XxlConf')：resolves conf + watch
					if (beanDefinition.getBeanClassName() == null) {
						continue;
					}
					Class beanClazz = null;
					try {
						beanClazz = Class.forName(beanDefinition.getBeanClassName());
					} catch (ClassNotFoundException e) {
						logger.error(">>>>>>>>>>> xxl-conf, annotation bean class invalid, error msg:{}", e.getMessage());
					}
					if (beanClazz == null) {
						continue;
					}
					ReflectionUtils.doWithFields(beanClazz, new ReflectionUtils.FieldCallback() {
						@Override
						public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
							if (field.isAnnotationPresent(LightConf.class)) {
								String propertyName = field.getName();
								LightConf lightConf = field.getAnnotation(LightConf.class);

								String confKey = lightConf.value();
								String confValue = LightConfClient.get(confKey, lightConf.defaultValue());


								// resolves placeholders
								BeanRefreshLightConfListener.BeanField beanField = new BeanRefreshLightConfListener.BeanField(beanName, propertyName);
								refreshBeanField(beanField, confValue);

								// watch
								if (lightConf.callback()) {
									BeanRefreshLightConfListener.addBeanField(confKey, beanField);
								}
							}
						}
					});
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

	private static BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		LightConfFactory.beanFactory = beanFactory;
	}

	@Override
	public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
		super.setIgnoreUnresolvablePlaceholders(true);
	}
}
