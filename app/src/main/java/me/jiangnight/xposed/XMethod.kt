package me.jiangnight.xposed

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Method


class XMethod private constructor(base: Any?) {

    private var mClass: Class<*>? = null
    private var mMethodName: String? = null
    private lateinit var mParameterTypes: Array<Class<*>?>
    private var hasParameterTypes = true
    private var mReturnType: Class<*>? = null


    init {
        if (base != null) {
            if (base is Class<*>)
                mClass = base
            else
                mClass = base.javaClass
        }
    }

    companion object {
        private val sMethodCache: HashMap<String, Method> = HashMap()

        fun create(clazz: Class<*>): XMethod {
            return XMethod(clazz)
        }

        fun create(className: String): XMethod {
            return XMethod(XUtils.findClass(className))
        }
    }

    fun name(methodName: String): XMethod {
        this.mMethodName = methodName
        return this
    }

    fun parameterTypes(vararg parameterTypes: Any): XMethod {
        hasParameterTypes = true
        mParameterTypes = arrayOfNulls(parameterTypes.size)
        for (i in parameterTypes.indices) {
            val o = parameterTypes[i]
            if (o is String) {
                mParameterTypes[i] = XUtils.findClass(o)!!
            } else if (o is Class<*>) {
                mParameterTypes[i] = o
            }
        }
        return this
    }

    fun hasNoParameterTypes(): XMethod {
        hasParameterTypes = false
        return this
    }

    fun returnType(returnType: Class<*>): XMethod {
        mReturnType = returnType
        return this
    }

    fun returnType(returnType: String): XMethod {
        mReturnType = XUtils.findClass(returnType)
        return this
    }

    fun hook(
        before: (param: XC_MethodHook.MethodHookParam) -> Unit = {},
        after: (param: XC_MethodHook.MethodHookParam) -> Unit = {}
    ): XC_MethodHook.Unhook? {
        val method = get()
        if (method != null) {
            return XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    before.invoke(param!!)
                }

                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    after.invoke(param!!)
                }
            })
        }
        return null
    }

    fun replace(replace: (param: XC_MethodHook.MethodHookParam) -> Any? = {}): XC_MethodHook.Unhook? {
        val method = get()
        if (method != null) {
            return XposedBridge.hookMethod(method, object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    return replace.invoke(param!!)
                }
            })
        }
        return null
    }

    fun replaceNull(){
        val method = get()
        if (method != null) {
             XposedBridge.hookMethod(method, object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    return DO_NOTHING
                }
            })
        }
    }

    fun get(): Method? {
        if (mClass != null) return null
        val fullMethodName = buildString {
            append(mClass!!.name)
            append('#')
            append(mMethodName)
            append(XUtils.getParametersString(*mParameterTypes))
            append("#return(")
            append(mReturnType?.name ?: "null")
            append(')')
        }

        // 尝试从缓存中获取方法
        val cachedMethod = sMethodCache[fullMethodName]
        if (cachedMethod != null) return cachedMethod

        // 查找方法
        val method = if (hasParameterTypes) {
            XUtils.findMethodIfExists(mClass!!, mReturnType, mMethodName, *mParameterTypes)
        } else {
            XUtils.findMethodIfExists(mClass!!, mReturnType, mMethodName)
        }

        method?.let {
            sMethodCache[fullMethodName] = it
            if (BuildConfig.DEBUG) {
                XposedBridge.log("getMethod success, name: $fullMethodName")
            }
        } ?: throw NullPointerException("getMethod fail, name: $fullMethodName")
        return method
    }

}