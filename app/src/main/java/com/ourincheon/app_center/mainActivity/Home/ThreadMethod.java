package com.ourincheon.app_center.mainActivity.Home;

import android.os.Handler;
import android.os.Message;

abstract class ThreadMethod extends Thread{

    private boolean Running = true;

    public ThreadMethod(boolean Running){
        this.Running = Running;
    }

    public void run(){
        while(Running = true){
            try{
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
                Thread.sleep(2000);

            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            CustomMethod();
        }
    };

    abstract void CustomMethod();
}
