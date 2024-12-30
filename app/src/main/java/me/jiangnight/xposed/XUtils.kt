package me.jiangnight.xposed

import android.text.TextUtils
import java.lang.reflect.Method

object XUtils {
    var classLoader: ClassLoader = ClassLoader.getSystemClassLoader()

    /**
     * 通过类名查找并加载类
     * @param className 需要加载的类的完整类名（包括包名）
     * @return 如果成功加载类，则返回类对象；否则返回 null。
     */
    fun findClass(className: String): Class<*>? {
        return try {
            classLoader.loadClass(className)
        } catch (throwable: Throwable) {
            null
        }
    }

    /**
     * 获取参数的字符串表示形式
     * @param clazzes 需要转化为字符串的类对象数组
     * @return 返回表示类对象数组的字符串，格式为 "(Class1,Class2,...)"，若为空则返回 "(null)"
     */
    fun getParametersString(vararg clazzes: Class<*>?): String {
        if (clazzes.isEmpty()) return "(null)"
        return clazzes.joinToString(
            prefix = "(",
            postfix = ")",
            separator = ","
        ) { it?.canonicalName ?: "null" }
    }

    fun findMethodIfExists(
        clazz: Class<*>,
        returnType: Class<*>?,
        methodName: String?,
        vararg parameterTypes: Class<*>?
    ): Method? {
        clazz.declaredMethods.forEach {
            if (returnType != null && returnType != it.returnType) return@forEach
            if (!TextUtils.isEmpty(methodName) && !it.name.equals(methodName)) return@forEach
            val methodParameterTypes = it.parameterTypes
            if (parameterTypes.size != methodParameterTypes.size) return@forEach
            var match = true
            for (i in parameterTypes.indices) {
                if (parameterTypes[i] != methodParameterTypes[i]) {
                    match = false
                    break
                }
            }
            if (match) {
                it.isAccessible = true
                return it
            }
        }
        return null
    }

}