package me.jiangnight.xposed

import de.robv.android.xposed.XC_MethodHook

/**
 * @receiver String
 * @param parameterTypes Array<out Any?>
 * @param before [@kotlin.ExtensionFunctionType] Function2<T, [@kotlin.ParameterName] MethodHookParam, Unit>
 * @param after [@kotlin.ExtensionFunctionType] Function2<T, [@kotlin.ParameterName] MethodHookParam, Unit>
 * @return XC_MethodHook.Unhook
 */
fun String.hookMethod(
    methodName: String, vararg parameterTypes: Any,
    before: (param: XC_MethodHook.MethodHookParam) -> Unit = {},
    after: (param: XC_MethodHook.MethodHookParam) -> Unit = {}
): XC_MethodHook.Unhook? {
    return XMethod.create(this).name(methodName).parameterTypes(parameterTypes).hook(before, after)
}


/**
 * @receiver Class<*> 类
 * @param methodName String 方法名
 * @param parameterTypes Array<out Any> 方法参数类型
 * @param before [@kotlin.ExtensionFunctionType] Function2<T, [@kotlin.ParameterName] MethodHookParam, Unit>
 * @param after [@kotlin.ExtensionFunctionType] Function2<T, [@kotlin.ParameterName] MethodHookParam, Unit>
 * @return XC_MethodHook.Unhook
 */
fun <T> Class<T>.hookMethod(
    methodName: String, vararg parameterTypes: Any,
    before: (param: XC_MethodHook.MethodHookParam) -> Unit = {},
    after: (param: XC_MethodHook.MethodHookParam) -> Unit = {}
): XC_MethodHook.Unhook? {
    return XMethod.create(this).name(methodName).parameterTypes(parameterTypes).hook(before, after)
}

/**
 * @receiver String 类名
 * @param methodName String 方法名
 * @param parameterTypes Array<out Any> 方法的参数类型
 * @param replace Function1<[@kotlin.ParameterName] MethodHookParam, Any>
 * @return XC_MethodHook.Unhook
 */

fun String.replaceMethod(
    methodName: String,
    vararg parameterTypes: Any?,
    replace: (param: XC_MethodHook.MethodHookParam) -> Any? = {}
): XC_MethodHook.Unhook? {
    return XMethod.create(this).name(methodName).parameterTypes(parameterTypes).replace(replace)
}

/**
 * @receiver Class<*> 类
 * @param methodName String 方法名
 * @param parameterTypes Array<out Any> 方法参数类型
 * @param replace [@kotlin.ExtensionFunctionType] Function2<T, [@kotlin.ParameterName] MethodHookParam, Any?>
 * @return XC_MethodHook.Unhook
 */
fun <T> Class<T>.replaceMethod(
    methodName: String,
    vararg parameterTypes: Any?,
    replace: (param: XC_MethodHook.MethodHookParam) -> Any? = {}
): XC_MethodHook.Unhook? {
    return XMethod.create(this).name(methodName).parameterTypes(parameterTypes).replace(replace)
}


/**
 * @receiver String 类名
 * @param methodName String 方法名
 * @param parameterTypes Array<out Any> 方法的参数类型
 * @return XC_MethodHook.Unhook
 */

fun String.replaceNullMethod(
    methodName: String,
    vararg parameterTypes: Any?,
) {
    XMethod.create(this).name(methodName).parameterTypes(parameterTypes).replaceNull()
}

/**
 * @receiver Class<*> 类
 * @param methodName String 方法名
 * @param parameterTypes Array<out Any> 方法参数类型
 * @return XC_MethodHook.Unhook
 */
fun <T> Class<T>.replaceNullMethod(
    methodName: String,
    vararg parameterTypes: Any?,
) {
    XMethod.create(this).name(methodName).parameterTypes(parameterTypes).replaceNull()
}