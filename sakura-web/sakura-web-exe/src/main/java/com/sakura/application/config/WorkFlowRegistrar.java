package com.sakura.application.config;

import com.sakura.farme.annotation.EnableSakuraMybatis;
import com.sakura.farme.tool.BeanRegistryUtils;
import com.sakura.farme.tool.ClassUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;


/**
 * @author: bi
 * @date: 2021/12/23 14:07
 */
public class WorkFlowRegistrar implements ImportBeanDefinitionRegistrar {

    private String classPackage;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {

        try {
            AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableWorkFlow.class.getName()));
            assert annotationAttributes != null;
            classPackage =  annotationAttributes.getString("scanBasePackage");
            if (!classPackage.equals("")) {
                Set<Class<?>> classes = ClassUtils.getClasses(classPackage, false);
                for (Class<?> aClass : classes) {
                    //通过反射获取类中的 getKey方法的返回值 当作beanName
                    Object o = aClass.newInstance();
                    Method getKey = aClass.getMethod("getKey");
                    Object invoke = getKey.invoke(o);
                    BeanDefinitionBuilder bean = BeanDefinitionBuilder.rootBeanDefinition(aClass);
                    //反射获取属性
                    Field[] declaredFields = aClass.getDeclaredFields();
                    for (Field declaredField : declaredFields) {
                        declaredField.setAccessible(true);

                    }
                    BeanRegistryUtils.registerBeanDefinition(registry, bean.getBeanDefinition(), invoke.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
