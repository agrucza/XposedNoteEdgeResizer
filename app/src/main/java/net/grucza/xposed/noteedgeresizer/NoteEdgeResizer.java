package net.grucza.xposed.noteedgeresizer;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook;

import android.app.Activity;
import android.view.WindowManager;
import android.view.Display;
import android.view.Window;
import android.view.Surface;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.hardware.SensorManager;
import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import de.robv.android.xposed.*;
import android.graphics.*;
import android.view.*;
import android.inputmethodservice.*;
import java.lang.reflect.*;
import android.util.*;
import android.view.View.*;
import android.content.res.Configuration;
import android.content.res.*;
import android.widget.RadioGroup.*;

public class NoteEdgeResizer implements IXposedHookLoadPackage{
	private static final String CLASS_DECOR_VIEW = "com.android.internal.policy.DecorView";
	
	int statusbarheight  = 0;
	int edgespace        = 160;
	int spacewithoutedge = 1440;
	
	OrientationEventListener orientationEventListener;
	
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		if(
			!lpparam.packageName.equals("com.android.systemui")
		){
			//return;
		}
		
		XposedBridge.log(lpparam.packageName);
		
		try{
			/*
			 * For Keyboard width in portrait mode
			 */
			XposedHelpers.findAndHookMethod("android.content.res.ResourcesImpl", lpparam.classLoader, "getDisplayMetrics", new XC_MethodHook(){
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable{
					DisplayMetrics dm = (DisplayMetrics)param.getResult();
					if(dm.widthPixels == 1600){
						dm.widthPixels = 1440;
						param.setResult(dm);
					}
				}
				});
		} catch(Throwable t){
			//XposedBridge.log("could not hook " + cl);
		}
		
		try{
			/*
			 * This will set the bounds of initialized Views
			 * was at least working for statusbarview!
			 */
			
			XposedHelpers.findAndHookMethod(View.class, "measure", int.class, int.class, new XC_MethodHook(){
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable{
					/*if(
						param.thisObject.getClass().getName().equals("com.android.internal.policy.DecorView") 
						|| param.thisObject.getClass().getName().equals("com.android.systemui.statusbar.phone.StatusBarWindowView")
					){*/
						// Check for width of 1600
						if(View.MeasureSpec.getSize(param.args[0]) == 1600){
							param.args[0] = View.MeasureSpec.makeMeasureSpec(1440, View.MeasureSpec.getMode(param.args[0]));
						} else if(View.MeasureSpec.getSize(param.args[1]) == 1600){
							param.args[1] = View.MeasureSpec.makeMeasureSpec(1440, View.MeasureSpec.getMode(param.args[1]));
							XposedBridge.log("height: " + View.MeasureSpec.getSize(param.args[1]));
							XposedBridge.log("mode: " + View.MeasureSpec.getMode(param.args[1]));
						}
					//}
				}
			});
			/*
			 * This will set the layout of initialized Views
			 * for a rotation of 90° to start at a top of 160
			 */

			XposedHelpers.findAndHookMethod(View.class, "layout", int.class, int.class, int.class, int.class, new XC_MethodHook(){
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable{
						if(
							param.thisObject.getClass().getName().equals("com.android.internal.policy.DecorView") 
							|| param.thisObject.getClass().getName().equals("com.android.systemui.statusbar.phone.StatusBarWindowView")
						){
							Display display = ((WindowManager)((View)param.thisObject).getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
							int rotation = display.getRotation();
							if(rotation == 1){
								param.args[1] = 160;
								param.args[3] = (int)param.args[3] + 160;
							}
							XposedBridge.log("rotation: " + rotation);
							XposedBridge.log(param.args[0] + "x" + param.args[1] + "x" + param.args[2] + "x" + param.args[3]);
						}
					}
				});
		} catch(Throwable t){
			//XposedBridge.log("could not hook " + classDecorView.getSuperclass().getName());
		}
	};
}
