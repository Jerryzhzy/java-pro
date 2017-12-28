package designmode.singleton;

/**
 * 单例对象（Singleton）是一种常用的设计模式。在Java应用中，单例对象能保证在一个JVM中，该对象只有一个实例存在。这样的模式有几个好处：
 1、某些类创建比较频繁，对于一些大型的对象，这是一笔很大的系统开销。
 2、省去了new操作符，降低了系统内存的使用频率，减轻GC压力。
 3、有些类如交易所的核心交易引擎，控制着交易流程，如果该类可以创建多个的话，系统完全乱了。
 （比如一个军队出现了多个司令员同时指挥，肯定会乱成一团），所以只有使用单例模式，才能保证核心交易服务器独立控制整个流程。
 */

/**
 * Created by ziyu.zhang on 2017/12/5.
 * Description 简单的单例类
 */
public class Singleton {

    //私有静态实例，防止被引用，赋null实现延迟加载
    private static Singleton instance = null;

    //私有构造，防止被外部实例化
    public Singleton() {
    }

    //创建实例
    public static Singleton getInstance(){
        if(null == instance){
            instance = new Singleton();
        }
        return instance;
    }
    /**
     * 这个类可以满足基本要求，但是，像这样毫无线程安全保护的类，如果我们把它放入多线程的环境下，肯定就会出现问题了，如何解决？
     * 我们首先会想到对getInstance方法加synchronized关键字
     */
    public static synchronized Singleton getInstance1(){
        if(null == instance){
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * 但是，synchronized关键字锁住的是这个对象，这样的用法，在性能上会有所下降，因为每次调用getInstance()，都要对对象上锁，
     * 事实上，只有在第一次创建对象的时候需要加锁，之后就不需要了，所以改进为：
     */
    public static Singleton getInstance2(){
        if(null == instance){
            synchronized(instance){
                if(null == instance){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }


    /**
     备注：

     似乎解决了之前提到的问题，将synchronized关键字加在了内部，也就是说当调用的时候是不需要加锁的，
     只有在instance为null，并创建对象的时候才需要加锁，性能有一定的提升。但是，这样的情况，还是有可能有问题的，看下面的情况：
     在Java指令中创建对象和赋值操作是分开进行的，也就是说instance = new Singleton();语句是分两步执行的。但是JVM并不保证这两个操作的先后顺序，
     也就是说有可能JVM会为新的Singleton实例分配空间，然后直接赋值给instance成员，然后再去初始化这个Singleton实例。这样就可能出错了，
     我们以A、B两个线程为例：

     a>A、B线程同时进入了第一个if判断

     b>A首先进入synchronized块，由于instance为null，所以它执行instance = new Singleton();

     c>由于JVM内部的优化机制，JVM先画出了一些分配给Singleton实例的空白内存，并赋值给instance成员（注意此时JVM没有开始初始化这个实例），
       然后A离开了synchronized块。

     d>B进入synchronized块，由于instance此时不是null，因此它马上离开了synchronized块并将结果返回给调用该方法的程序。

     e>此时B线程打算使用Singleton实例，却发现它没有被初始化，于是错误发生了。

     所以程序还是有可能发生错误，其实程序在运行过程是很复杂的，从这点我们就可以看出，尤其是在写多线程环境下的程序更有难度，
     有挑战性。我们对该程序做进一步优化：见 Singleton2.java
     */

    //如果该对象被用于序列化，可以保证对象在序列化前后保持一致
    public Object readResolve(){
        return instance;
    }
}
