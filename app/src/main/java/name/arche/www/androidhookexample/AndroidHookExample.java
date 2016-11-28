package name.arche.www.androidhookexample;

import android.hardware.Sensor;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by Arche on 2016/11/28.
 */

public class AndroidHookExample implements IXposedHookLoadPackage {

    static int WechatStepCount = 1;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

//        XposedBridge.hookAllMethods(TelephonyManager.class,"getDeviceId",new XC_MethodHook(){
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                param.setResult("000000000000000");
//            }
//        });

        if (!loadPackageParam.packageName.equals("com.tencent.mm"))
            return;
        try {
            final Class<?> sensorEL = findClass("android.hardware.SystemSensorManager$SensorEventQueue",
                    loadPackageParam.classLoader);
            XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent", new
                    XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws
                                Throwable {
                            ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] + 1000 * WechatStepCount;
                            WechatStepCount += 1;
                        }
                    });
        } catch (Throwable t) {
            throw t;
        }
    }
}
