import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// NumberPrinter class (provided)
class NumberPrinter {
    public void printZero() {
        System.out.print("0");
    }

    public void printEven(int num) {
        System.out.print(num);
    }

    public void printOdd(int num) {
        System.out.print(num);
    }
}

// ThreadController class
class ThreadController {
    private final NumberPrinter printer;
    private final int n;
    private int count = 1;
    private boolean isZeroTurn = true;
    private final Lock lock = new ReentrantLock();
    private final Condition zeroCondition = lock.newCondition();
    private final Condition evenCondition = lock.newCondition();
    private final Condition oddCondition = lock.newCondition();

    public ThreadController(NumberPrinter printer, int n) {
        this.printer = printer;
        this.n = n;
    }

    public void printZero() throws InterruptedException {
        lock.lock();
        try {
            while (count <= n) {
                if (!isZeroTurn) {
                    zeroCondition.await();
                }
                if (count > n) break; // Exit if we've printed all numbers
                printer.printZero();
                isZeroTurn = false;
                if (count % 2 == 1) {
                    oddCondition.signal(); // Signal OddThread
                } else {
                    evenCondition.signal(); // Signal EvenThread
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void printEven() throws InterruptedException {
        lock.lock();
        try {
            while (count <= n) {
                if (isZeroTurn || count % 2 == 1) {
                    evenCondition.await();
                }
                if (count > n) break; // Exit if we've printed all numbers
                printer.printEven(count);
                count++;
                isZeroTurn = true;
                zeroCondition.signal(); // Signal ZeroThread
            }
        } finally {
            lock.unlock();
        }
    }

    public void printOdd() throws InterruptedException {
        lock.lock();
        try {
            while (count <= n) {
                if (isZeroTurn || count % 2 == 0) {
                    oddCondition.await();
                }
                if (count > n) break; // Exit if we've printed all numbers
                printer.printOdd(count);
                count++;
                isZeroTurn = true;
                zeroCondition.signal(); // Signal ZeroThread
            }
        } finally {
            lock.unlock();
        }
    }
}

// ZeroThread class
class ZeroThread extends Thread {
    private final ThreadController controller;

    public ZeroThread(ThreadController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            controller.printZero();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// EvenThread class
class EvenThread extends Thread {
    private final ThreadController controller;

    public EvenThread(ThreadController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            controller.printEven();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// OddThread class
class OddThread extends Thread {
    private final ThreadController controller;

    public OddThread(ThreadController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            controller.printOdd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        NumberPrinter printer = new NumberPrinter();
        int n = 5; // Specify the limit
        ThreadController controller = new ThreadController(printer, n);

        ZeroThread zeroThread = new ZeroThread(controller);
        EvenThread evenThread = new EvenThread(controller);
        OddThread oddThread = new OddThread(controller);

        zeroThread.start();
        evenThread.start();
        oddThread.start();

        try {
            zeroThread.join();
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}