package Thread;

//生產者消費者更新版

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProConThread {

    public static void main(String[] args) {

       Respositorys res = new Respositorys();

       Workers p1 = new Workers(res,"手机");
       Workers p2 = new Workers(res,"电脑");
       Workers p3 = new Workers(res,"鼠标");

       Constomers c1 = new Constomers(res);
       Constomers c2 = new Constomers(res);

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
class Respositorys{

    private Lock lock = new ReentrantLock();

    private LinkedList<Products> store = new LinkedList<Products>();

    private Condition condition_pro = lock.newCondition();
    private Condition condition_con = lock.newCondition();

    public LinkedList<Products> getStore() {
        return store;
    }

    public void setStore(LinkedList<Products> store) {
        this.store = store;
    }
    //向仓库存货
    public  void push(Products p, String ThreadName) throws InterruptedException{
        lock.lock();
        try {
            //设置仓库库存最多能存5个商品
            while (store.size()==5){
                    System.out.println(ThreadName+" 发现：仓库已满，赶紧叫人运走");
                    condition_pro.await();
            }
            condition_con.signalAll();
            store.addLast(p);
            System.out.println(ThreadName+" 给仓库添加 "+p.Name+p.Id+"号名称为 "+" 当前库存量为："+store.size());

        }finally {
            lock.unlock();
        }
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


    //仓库出货
    public void pop(String ThreadName) throws InterruptedException
    {
        lock.lock();
        try{
            while (store.size()==0){

                    System.out.println(ThreadName+" 发现：仓库空了，赶紧安排生产");
                    condition_con.await();
            }
            condition_pro.signalAll();
            Products p = store.pollFirst();
            System.out.println(ThreadName+"买走 "+p.Name+p.Id+" 当前库存量为："+store.size());

        }
        finally {
            lock.unlock();
        }
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}


class Products{
    public int Id;

    public String Name;

    public Products(String name, int id) {
        Name = name;
        Id = id;
    }

}

class Workers implements Runnable{

    public volatile Integer Id = 0;

    public volatile String name;

    //引用一个产品
    private Products p;
    //引用一个仓库
    Respositorys res;

    boolean flag = true;

    public Workers(Respositorys res, String name) {
        this.res = res;
        this.name = name;
    }

    @Override
    public void run(){
        while (flag){
                p  = new Products(name,Id);
                try {
                    res.push(new Products(this.p.Name,Id++),Thread.currentThread().getName());
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
    }
}

class Constomers implements Runnable{
    boolean flag = true;

    //引用一个仓库
    Respositorys res;

    public Constomers(Respositorys res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (flag) {
            try {
                res.pop(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
