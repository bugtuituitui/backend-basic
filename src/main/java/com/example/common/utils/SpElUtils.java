package com.example.common.utils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * spring el 工具类
 */
public class SpElUtils {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 获取方法参数的唯一标识
     * @param method
     * @param args
     * @param spEl
     * @return
     */
    public static String parseSpEl(Method method, Object[] args, String spEl) {
        String[] params = parameterNameDiscoverer.getParameterNames(method);//解析参数名
        EvaluationContext context = new StandardEvaluationContext();//el解析需要的上下文对象
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);//所有参数都作为原材料扔进去
        }
        Expression expression = parser.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }

    /**
     * 获取Method唯一标识
     * @param method
     * @return
     */
    public static String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }

//    public static void main(String[] args) throws NoSuchMethodException {
//
//        Class clazz = SpElUtils.class;
//
//        Method method = clazz.getDeclaredMethod("test", String.class);
//
//        Object[] objects = {"a"};
//
//        System.out.println(parseSpEl(method, objects, "#user"));
//    }
//
//    public static void test(String name) {
//        System.out.println("test");
//    }
}