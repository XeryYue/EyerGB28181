package com.zzsin.eyer.demo;

import com.zzsin.eyer.gb28181.CInterface;
import com.zzsin.eyer.gb28181.Device;
import com.zzsin.eyer.gb28181.GBServer;
import com.zzsin.eyer.gb28181.LoadCallback;
import com.zzsin.eyer.gb28181.callback.TestCallback;

import java.util.List;

class MyLoadCallback extends LoadCallback
{
    public int load(String libName) {
        if("EyerGB28181Jni".equals(libName)){
            System.load("/Users/yuqiaomiao/GitHub/EyerGB28181/EyerGB28181/cmake-build-debug/EyerGB28181Jni/libEyerGB28181Jni.dylib");
            // System.load("/Users/lichi/EyerGB28181/EyerGB28181/cmake-build-debug/EyerGB28181Jni/libEyerGB28181Jni.dylib");
            // System.load("/root/EyerGB28181/EyerGB28181/cmake-build-debug/EyerGB28181Jni/libEyerGB28181Jni.so");
        }
        return 0;
    }
}

class MyTestCallback extends TestCallback
{
    @Override
    public int callback(int a) {
        return 0;
    }
}

public class Main {
    public static void main(String[] args){
        GBServer.load(new MyLoadCallback());

        long a = CInterface.eyer_jni_test_set_callback_start(new MyTestCallback());
        CInterface.eyer_jni_test_set_callback_stop(a);

        System.out.println(GBServer.getVersion());

        GBServer gbServer = new GBServer(5060);
        DemoSIPPassiveCallback demoSIPPassiveCallback = new DemoSIPPassiveCallback(gbServer);
        gbServer.setPassiveCallback(demoSIPPassiveCallback);
        gbServer.start();

        for(int i=0;i<60 * 2;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gbServer.stop();
        gbServer.destory();

        demoSIPPassiveCallback.destory();
    }
}
