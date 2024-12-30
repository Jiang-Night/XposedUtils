package me.jiangnight.xposed

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookEntry : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam?.packageName.equals("me.jiangnight.xposed")) {
            XposedHelpers.findAndHookMethod(
                Application::class.java,
                "attachBaseContext",
                Context::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        super.afterHookedMethod(param)
                        val application = param?.thisObject as Application
                        XUtils.classLoader = application.classLoader
                        doHook()
                    }
                })
        }
    }

    private fun doHook() {
        "me.jiangnight.xposed.Test".hookMethod("a", String::class.java,
            after = {
                val s = it.args[0] as String
                it.result = s
            }
        )
        Activity::class.java.hookMethod("onCreate", Bundle::class.java,
            after = {
                val bundle = it.args[0] as Bundle
                it.result = bundle
            })

        XMethod.create("me.jiangnight.xposed.Test").name("onCreate")
            .returnType(Void::TYPE::class.java).hook(
            after = {
                val s = it.args[0] as String
                it.result = s
            },
            before = {
                val s = it.args[0] as String
                it.result = s
            }
        )
    }
}