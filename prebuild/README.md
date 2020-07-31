
prebuild apk file for arm64 arch   
push the file into the adroid system via cmd 'adb push ./prebuild/app-debug.apk /data/local/tmp'  
set root via cmd 'adb shell chmod 777 /data/local/tmp/app-debug.apk'  
launch the project via cmd 'adb shell CLASSPATH=/data/local/tmp/app-debug.apk app_process / com.armchina.cph.Main'  
