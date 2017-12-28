package designmode.singleton;

import java.util.Vector;

/**
 *
 * Created by ziyu.zhang on 2017/12/5.
 * 我们只需要在创建类的时候进行同步，所以只要将创建和getInstance()分开，单独为创建加synchronized关键字，也是可以的：
 */
public class Singleton3 {

    private static Singleton3 instance = null;
    private Vector properties = null;

    public Singleton3() {

    }

    public Vector getProperties(){
        return properties;
    }

    public static synchronized void syncInit(){
        if(null == instance){
            instance = new Singleton3();

        }
    }

    public static Singleton3 getInstance(){

        if(null == instance){
            syncInit();
        }

        return instance;
    }

    public void updateProperties(){
        Singleton3 shadow = new Singleton3();
        properties = shadow.getProperties();

    }


}
