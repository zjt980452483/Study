package Thread;

public class CodingTask implements Runnable {

    private final  int employeeId;

    public CodingTask(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public void run() {
        System.out.println("Employee "+employeeId+" start Writing Code.");
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        System.out.println("Employee "+ employeeId+" successful write");

    }
}
