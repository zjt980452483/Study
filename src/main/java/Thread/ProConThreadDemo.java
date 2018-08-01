package Thread;

import java.util.LinkedList;

public class ProConThreadDemo {
    public static void main(String[] args) {
        Respository res = new Respository();

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

class Respository{

    private LinkedList<Product> store = new LinkedList<Product>();

    public LinkedList<Product> getStore() {
        return store;
    }

    public void setStore(LinkedList<Product> store) {
        this.store = store;
    }
    //向仓库存货
    public synchronized void push(Product p,String ThreadName){
        //设置仓库库存最多能存5个商品
        while (store.size()==5){
            try {
                System.out.println(ThreadName+" 发现：仓库已满，赶紧叫人运走");
                this.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        this.notifyAll();
        store.addLast(p);
        System.out.println(ThreadName+" 给仓库添加 "+p.Name+p.Id+"号名称为 "+" 当前库存量为："+store.size());
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


    //仓库出货
    public synchronized void pop(String ThreadName){
        while (store.size()==0){
            try {
                System.out.println(ThreadName+" 发现：仓库空了，赶紧安排生产");
                this.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        this.notifyAll();
        Product p = store.pollFirst();
        System.out.println(ThreadName+"买走 "+p.Name+p.Id+" 当前库存量为："+store.size());
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}



class Product{
    public int Id;

    public String Name;

    public Product(String name, int id) {
        Name = name;
        Id = id;
    }

}

class Worker implements Runnable{

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