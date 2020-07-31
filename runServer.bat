adb push ./prebuild/app-debug.apk /data/local/tmp
adb forward tcp:8080 tcp:8080
adb shell chmod 777 /data/local/tmp/app-debug.apk
adb shell CLASSPATH=/data/local/tmp/app-debug.apk app_process / com.armchina.cph.Main
