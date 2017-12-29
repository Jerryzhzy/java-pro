package thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by ziyu.zhang on 2017/12/28.
 * Description  多线程读大文件
 */
public class ThreadReadFileDemo {
    protected final static Logger error_logger = LoggerFactory.getLogger("error_log");
    @org.junit.Test
    public void test() {
        Thread t1 = new Thread(new MultiThread(), "Thread Name A");
        Thread t2 = new Thread(new MultiThread(), "Thread Name A");
        t1.start();
        t2.start();
    }

}

class MultiThread implements Runnable{

        private static BufferedReader br = null;
        private List<String> list;

        static{
            try {
                br = new BufferedReader(new FileReader("C://offline_FtnInfo.txt"),10);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                ThreadReadFileDemo.error_logger.error("File read error",e);

            }
        }

        public void run() {
            String line = null;
            int count = 0;
            while(true) {
                //System.out.println(Thread.currentThread().getName());
                this.list = new ArrayList<String>();
                synchronized(br) {
                    try {
                        while((line = br.readLine()) != null) {
                            if(count<15) {
                                list.add(line);
                                count++;
                            }else {
                                list.add(line);
                                count = 0;
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1);
                    display(this.list);
                } catch (InterruptedException e) {
                    ThreadReadFileDemo.error_logger.error("File read error",e);
                }
                if(line == null)
                    break;
            }

        }

        public void display(List<String> list) {
            for(String str:list) {
                System.out.println(str);
            }
            System.out.println(list.size());
        }

    }