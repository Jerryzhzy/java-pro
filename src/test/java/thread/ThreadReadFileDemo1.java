package thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ziyu.zhang on 2017/12/28.
 * Description 多线程 schedule、scheduleAtFixedRate、scheduleWithFixedDelay的使用
 */
public class ThreadReadFileDemo1 {
    protected final static Logger analyze_logger = LoggerFactory.getLogger("analyze_log");
    protected final static Logger error_logger = LoggerFactory.getLogger("error_log");
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @org.junit.Test
    public void test(){
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(10);//允许最多同时执行10个进程
        //方式一：一次性线程
        newScheduledThreadPool.schedule(
                new MultiThread1("C://offline_FtnInfo.txt", 0,1000),
                0,//启动工程后多少时间后开始执行该线程
                TimeUnit.SECONDS);

        //方式二：固定的频率来执行某项计划，它不受执行时间的影响。到周期时间，它就执行。
        newScheduledThreadPool.scheduleAtFixedRate(
                new MultiThread1("C://offline_FtnInfo.txt", 0,1000),
                0,   //启动工程后多少时间后开始执行该线程
                500, //执行周期，到时间就执行(500s一个周期)
                TimeUnit.SECONDS);

        //方式二：固定的频率来执行某项计划，受执行时间的影响，只有上个任务执行完毕，才会开启下次执行倒计时。
        newScheduledThreadPool.scheduleWithFixedDelay(
                new MultiThread1("C://offline_FtnInfo.txt", 0,1000),
                0,   //启动工程后多少时间后开始执行该线程
                500, //执行周期，上一个任务执行完开始进入倒计时，500s后再次执行任务
                TimeUnit.SECONDS);
    }
}

class MultiThread1 implements Runnable {


    private long spointer = 0;                  //文件起始位点
    private long epointer = 0;                  //文件结束位点
    private String path;                        //文件路径
    public  MultiThread1(String path,long spointer,long epointer)
    {
        this.path = path;
        this.spointer = spointer;
        this.epointer = epointer;
    }

    @Override
    public void run() {
        //获得变化部分
        try {
            File logFile = new File(path);
            if(!logFile.exists())
                logFile.createNewFile();

            long len = logFile.length();
            //此线程的结束位置
            if(len < spointer){
                ThreadReadFileDemo1.analyze_logger.info("\n Log file was reset. Restarting logging from start of file.");
            }else{
                //指定文件可读可写
                RandomAccessFile randomFile= new RandomAccessFile(logFile,"rw");
                randomFile.seek(spointer);//移动文件指针位置
                String tmp = "";
                String readLine = "";
                while((tmp = randomFile.readLine()) != null) {
                    spointer = randomFile.getFilePointer();//获取RandomAccessFile对象文件指针的位置
                    //非最后线程，读取部分后退出
                    if(spointer >= epointer){
                        break;
                    }
                    readLine = new String(tmp.getBytes("ISO-8859-1"), "UTF-8");

                    //TODO 业务处理
                    System.out.println("\n [" +path+"==文件]当前已同步位点:"+spointer+",同步行: \n"+readLine);
                    ThreadReadFileDemo1.analyze_logger.info("\n [" +path+"==文件]当前已同步位点:"+spointer+",同步行: \n"+readLine);
                }
            }

        } catch (Exception e) {
            //实时读取日志异常，需要记录时间和lastTimeFileSize 以便后期手动补充
            System.out.println("\n [" +path +" 文件]异常 "+ ThreadReadFileDemo1.dateFormat.format(new Date())  + " File read error, pointer: "+spointer);
            ThreadReadFileDemo1.error_logger.error("\n [" +path +" 文件]异常 "+ ThreadReadFileDemo1.dateFormat.format(new Date())  + " File read error, pointer: "+spointer,e);
            ThreadReadFileDemo1.analyze_logger.info("\n [" +path +" 文件]异常 "+ ThreadReadFileDemo1.dateFormat.format(new Date())  + " File read error, pointer: "+spointer,e);
        } finally {
            System.out.println("\n [" +path+"==文件]位点落地:"+spointer);
            ThreadReadFileDemo1.analyze_logger.info("\n [" +path+"==文件]位点落地:"+spointer);
        }

    }
}
