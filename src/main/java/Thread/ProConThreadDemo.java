package Thread;
/**
 * 2个消费者，3个生产者
 */

import java.util.LinkedList;

public class ProConThreadDemo {
    public static void main(String[] args) {
        Respository res = new Respository();

        //定义2个消费者，3个生产者
        Worker p1 = new Worker(res,"手机");
        Worker p2 = new Worker(res,"电脑");
        Worker p3 = new Worker(res,"鼠标");
        Constomer c1 = new Constomer(res);
        Constomer c2 = new Constomer(res);

        Thread t1 = new Thread(p1,"甲");
        Thread t2 = new Thread(p2,"乙");
        Thread t3 = new Thread(p3,"丙");
        Thread t4 = new Thread(c1,"aaa");
        Thread t5 = new Thread(c2,"bbb");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

    }



}
//仓库类
class Respository{

    private LinkedList<Product> store = new LinkedList<Product>();

    //生产者的方法，用于向仓库存货
    //最多只能有一个线程同时访问该方法.
    public synchronized void push(Product p,String ThreadName){
        //设置仓库库存最多能存5个商品
        /* 仓库容量最大值为5,当容量等于5的时候进入等待状态.等待其他线程唤醒
         * 唤醒后继续循环，等到仓库的存量小于5时，跳出循环继续向下执行准备生产产品.
         */
        while (store.size()==5){
            try {
                System.out.println(ThreadName+" 发现：仓库已满，赶紧叫人运走");
                //因为仓库容量已满，无法继续生产，进入等待状态，等待其他线程唤醒.
                this.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        this.notifyAll();
        store.addLast(p);
        System.out.println(ThreadName+" 给仓库添加 "+p.Name+p.Id+"号名称为 "+" 当前库存量为："+store.size());
        //为了方便观察运行结果,每次生产完后等待0.1秒
//        try {
//            Thread.sleep(100);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }

    }


    //消费者的方法，用于仓库出货
    //最多只能有一个线程同时访问该方法.
    public synchronized void pop(String ThreadName){
        /* 当仓库没有存货时,消费者需要进行等待.等待其他线程来唤醒
         * 唤醒后继续循环，等到仓库的存量大于0时，跳出循环继续向下执行准备消费产品.
         */
        while (store.size()==0){
            try {
                System.out.println(ThreadName+" 发现：仓库空了，赶紧安排生产");
                //因为仓库容量已空，无法继续消费，进入等待状态，等待其他线程唤醒.
                this.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        this.notifyAll();
        //定义对象。存放pollFirst()方法删除的对象，
        Product p = store.pollFirst();
        System.out.println(ThreadName+"买走 "+p.Name+p.Id+" 当前库存量为："+store.size());
        //为了方便观察运行结果,每次取出后等待0.1秒
//        try {
//            Thread.sleep(100);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }

    }


}


//产品类
class Product{
    //产品的唯一标识Id
    public int Id;
    //产品的名称
    public String Name;

    public Product(String name, int id) {
        Name = name;
        Id = id;
    }

}

//生产者
class Worker implements Runnable{
    //关键字volatile 是为了保持 Id 的可见性，一旦Id被修改，其他任何线程用到Id的地方，都会相应修改
    //否则下方run方法容易出问题，生产商品的Id和名称 与到时候消费者取出商品的Id和名称不一致
    public volatile Integer Id = 0;

    public volatile String name;

    //引用一个产品
    private Product p;
    //引用一个仓库
    Respository res;

    boolean flag = true;

    public Worker(Respository res,String name) {
        this.res = res;
        this.name = name;
    }

    @Override
    public void run() {
        while (flag){
            p  = new Product(name,Id);
            res.push(new Product(this.p.Name,Id++),Thread.currentThread().getName());
        }
    }
}

class Constomer implements Runnable{
    boolean flag = true;

    //引用一个仓库
    Respository res;

    public Constomer(Respository res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (flag) {

            res.pop(Thread.currentThread().getName());


        }

    }
}