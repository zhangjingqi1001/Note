package com.zhangjingqi.imports;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class MyImportSelector implements ImportSelector {
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
//       参数是一个注解的全限定名
//        annotationMetadata.getAnnotationAttributes("org.springframework.context.annotation.ComponentScan");
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(ComponentScan.class.getName());
        annotationAttributes.forEach((attrName,attrValue)->{
            System.out.println(attrName+"="+attrValue);
        });

        // 返回的是一个数组，封装的是需要被注册到Spring容器中的Bean的全限定名（全包名）
//        return new String[0];
        return new String[]{OtherBean2.class.getName()};
    }
}
