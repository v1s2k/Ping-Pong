package pinh;

public class PingPong {
    public static void main(String[] args) {
        Object LOCK_OBJECT = new Object();//объект LOCK_OBJECT, чтобы вызвать  методы
        Thread ping = new Thread(new PingPongThread(LOCK_OBJECT, "Ping"));//2  потока с именами
        Thread pong = new Thread(new PingPongThread(LOCK_OBJECT, "Pong"));
        ping.start();
        pong.start();
    }
}

class PingPongThread implements Runnable{ // реализует интерфейс Thread - это абстракция над физическим потоком.
    // Runnable - это абстракция над выполняемой задачей.

    private Object LOCK_OBJECT;
    private String name;

    public PingPongThread(Object LOCK_OBJECT, String name) {
        this.LOCK_OBJECT = LOCK_OBJECT;
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (LOCK_OBJECT) { //позволяет заблокировать доступ к методу или части кода,
            // если его уже использует другой поток. Если метод или объект "свободен" - поток может с ним работать:
            while(true) {
                System.out.println(name);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    System.out.println("Ошибка в - ");
                }
                LOCK_OBJECT.notify();

                try {
                    LOCK_OBJECT.wait(500);//wait(): освобождает монитор и переводит вызывающий поток в состояние ожидания до тех пор, пока другой поток не вызовет метод notify()
                } catch (InterruptedException e2) {
                    System.out.println("Ошибка в - ");
                }
                LOCK_OBJECT.notify();//notify(): продолжает работу потока, у которого ранее был вызван метод wait()
            }
        }
    }
}