package com.company;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.System.*;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class Main {

            //Режим работы перекрестка
    private static boolean crossroadMode;

            //Интервал обновления статуса в мс
    private static final int STATUS_UPDATE_INTERVAL=1000;

            //Количество статусов
    private static final int STATUS_NUMBER=100;


    public static void main(String[] args) throws InterruptedException {

        // Светофоры для авто
        TrafficLight FirstTl = new TrafficLight(1, "cars", 'r', 0);
        TrafficLight SecondTl = new TrafficLight(2,"cars",'r',0);
        TrafficLight ThirdTl = new TrafficLight(3,"cars",'r', 0);
        TrafficLight FourthTl = new TrafficLight(4,"cars",'r', 0);

        // Светофоры для пешеходов
        TrafficLight WalkerFirstTl = new TrafficLight(5,"walkers",'r', 0);
        TrafficLight WalkerSecondTl = new TrafficLight(6,"walkers",'r', 0);
        TrafficLight WalkerThirdTl = new TrafficLight(7,"walkers",'r', 0);
        TrafficLight WalkerFourthTl = new TrafficLight(8,"walkers",'r', 0);

        //Массив для хранения величин очередей пешеходов
        ArrayList<Integer> listOfWalkersQueue = new ArrayList<>();
        listOfWalkersQueue.add(WalkerFirstTl.getQueue());
        listOfWalkersQueue.add(WalkerSecondTl.getQueue());
        listOfWalkersQueue.add(WalkerThirdTl.getQueue());
        listOfWalkersQueue.add(WalkerFourthTl.getQueue());

        //Режим работы перекрестка
        //AtomicBoolean crossroadMode = new AtomicBoolean(false);

        //Поток 1 светофора
        final Runnable FirstTLChanger=()->{
            try {
                int count = 0; //Кол-во проехавших объектов
                int k; //Коэффициент нагруженности
                while (!currentThread().isInterrupted()){
                    k = getLoadFactor(FirstTl.getQueue(), SecondTl.getQueue(), ThirdTl.getQueue(), FourthTl.getQueue());
                while (FirstTl.getCondition() != 'r') {
                    if (count > 4+k || FirstTl.getQueue() == 0) {
                        FirstTl.setCondition('y');
                        sleep(1000);
                        FirstTl.setCondition('r');
                        sleep(2000);
                        count = 0;
                        break;
                    }
                    synchronized (FirstTl){FirstTl.setQueue(FirstTl.getQueue() - 1);}
                    sleep(1000);
                    count += 1;
                }

            }
            }
            catch (InterruptedException e){
            out.print("1 TL is stopped\n");
            }
        };
        final Thread FirstTLThread = new Thread(FirstTLChanger);


        //Поток 2 светофора
        final Runnable SecondTLChanger=()->{
            try {
                int count = 0; //Кол-во проехавших объектов
                int k; //Коэффициент нагруженности
                while (!currentThread().isInterrupted()){
                    k = getLoadFactor(SecondTl.getQueue(),FirstTl.getQueue(),  ThirdTl.getQueue(), FourthTl.getQueue());
                    while (SecondTl.getCondition() != 'r') {

                        if (count > 4+k || SecondTl.getQueue() == 0) {
                            SecondTl.setCondition('y');
                            sleep(1000);
                            SecondTl.setCondition('r');
                            sleep(2000);
                            count = 0;
                            break;
                        }
                        synchronized (SecondTl){SecondTl.setQueue(SecondTl.getQueue() - 1);}
                        sleep(1000);
                        count += 1;
                    }
                }
            }
            catch (InterruptedException e){
                out.print("2 TL is stopped\n");
            }
        };
        final Thread SecondTLThread = new Thread(SecondTLChanger);


        //Поток 3 светофора
        final Runnable ThirdTLChanger=()->{
            try {
                int count = 0; //Кол-во проехавших объектов
                int k; //Коэффициент нагруженности
                while (!currentThread().isInterrupted()){
                    k = getLoadFactor(  ThirdTl.getQueue(), SecondTl.getQueue(),FirstTl.getQueue(),FourthTl.getQueue());
                    while (ThirdTl.getCondition() != 'r') {

                        if (count > 4+k || ThirdTl.getQueue() == 0) {
                            ThirdTl.setCondition('y');
                            sleep(1000);
                            ThirdTl.setCondition('r');
                            sleep(2000);
                            count = 0;
                            break;
                        }
                        synchronized (ThirdTl){ThirdTl.setQueue(ThirdTl.getQueue() - 1);}
                        sleep(1000);
                        count += 1;
                    }
                }

            }
            catch (InterruptedException e){
                out.print("3 TL is stopped\n");
            }
        };
        final Thread ThirdTLThread = new Thread(ThirdTLChanger);


        //Поток 4 светофора
        final Runnable FourthTLChanger=()->{
            try { int count=0; //Кол-во проехавших объектов
                int k; //Коэффициент нагруженности
                while (!currentThread().isInterrupted()) {
                    k = getLoadFactor( FourthTl.getQueue(),ThirdTl.getQueue(), SecondTl.getQueue(), FirstTl.getQueue());
                    while (FourthTl.getCondition() != 'r') {

                        if (count > 4+k || FourthTl.getQueue() == 0) {
                            FourthTl.setCondition('y');
                            sleep(1000);
                            FourthTl.setCondition('r');
                            sleep(2000);
                            count = 0;
                            break;
                        }
                        synchronized (FourthTl){FourthTl.setQueue(FourthTl.getQueue() - 1);}
                        sleep(1000);
                        count += 1;
                    }
                }


            }
            catch (InterruptedException e){
                out.print("4 TL is stopped\n");
            }
        };
        final Thread FourthTLThread = new Thread(FourthTLChanger);

        FirstTLThread.setDaemon(true);
        SecondTLThread.setDaemon(true);
        ThirdTLThread.setDaemon(true);
        FourthTLThread.setDaemon(true);

        //Запуск всех потоков для светофоров
        FirstTLThread.start();
        SecondTLThread.start();
        ThirdTLThread.start();
        FourthTLThread.start();



        //Поток-регулировщик
        final Runnable conditionChanger=()->{
            boolean operatingMode=true;  //Режим работы перекрестка: true - проезд авто, false - пропускаем пешеходов
            crossroadMode=operatingMode;
            int delay=100;          //Задержка опросов автомобильных светофоров
            int limAuto=8;            //Ограничение времени пропуска автомобилей
            int limWalker=4;        //Ограничение времени пешеходного перехода
            int timer =(int) (currentTimeMillis() / 1000);//Обнуляем таймер
            try {
                while (!currentThread().isInterrupted()) {
                        if (!FirstTLThread.getState().equals(Thread.State.TIMED_WAITING) && FirstTl.getQueue() > SecondTl.getQueue()
                                && FirstTl.getCondition() == 'r' && SecondTl.getCondition() == 'r' && FourthTl.getCondition() == 'r'|| ThirdTl.getCondition()=='g') {
                            FirstTl.setCondition('g');
                        }
                    sleep(delay);
                   // if (ThirdTl.getCondition()=='g' && FirstTl.getQueue()>0) {FirstTl.setCondition('g');}
                        if (!SecondTLThread.getState().equals(Thread.State.TIMED_WAITING) && SecondTl.getQueue() > ThirdTl.getQueue()
                                && SecondTl.getCondition() == 'r' && FirstTl.getCondition() == 'r' && ThirdTl.getCondition() == 'r'|| FourthTl.getCondition()=='g') {
                            SecondTl.setCondition('g');
                        }
                    sleep(delay);
                   // if (FourthTl.getCondition()=='g' && SecondTl.getQueue()>0) {SecondTl.setCondition('g');}
                        if (!ThirdTLThread.getState().equals(Thread.State.TIMED_WAITING) && ThirdTl.getQueue() > FourthTl.getQueue()
                                && ThirdTl.getCondition() == 'r' && SecondTl.getCondition() == 'r' && FourthTl.getCondition() == 'r'|| FirstTl.getCondition()=='g') {
                            ThirdTl.setCondition('g');
                        }
                    sleep(delay);
                   // if (FirstTl.getCondition()=='g' && ThirdTl.getQueue()>0) {ThirdTl.setCondition('g');}
                        if (!FourthTLThread.getState().equals(Thread.State.TIMED_WAITING) && FourthTl.getQueue() > FirstTl.getQueue()
                                && FourthTl.getCondition() == 'r' && FirstTl.getCondition() == 'r' && ThirdTl.getCondition() == 'r'|| SecondTl.getCondition()=='g') {
                            FourthTl.setCondition('g');
                        }
                    sleep(delay);
                    //if (SecondTl.getCondition()=='g' && FourthTl.getQueue()>0) {FourthTl.setCondition('g');}

                        if (currentTimeMillis()/1000 - timer > limAuto || Collections.max(listOfWalkersQueue) > 5) {
                            if(Collections.max(listOfWalkersQueue)!=0){
                                while (FirstTl.getCondition()!='r' || SecondTl.getCondition()!='r' || ThirdTl.getCondition()!='r' || FourthTl.getCondition()!='r'){
                                    sleep(100);}
                                timer = (int) (currentTimeMillis() / 1000);//Обнуляем таймер
                                operatingMode = false;
                                crossroadMode=operatingMode;
                                WalkerFirstTl.  setCondition('g');
                                WalkerSecondTl. setCondition('g');
                                WalkerThirdTl.  setCondition('g');
                                WalkerFourthTl. setCondition('g');
                                while(currentTimeMillis()/1000 - timer < limWalker && !Collections.max(listOfWalkersQueue).equals(0)){
                                    synchronized (WalkerFirstTl){WalkerFirstTl.setQueue(WalkerFirstTl.getQueue()    >  0? WalkerFirstTl.getQueue() - 1: WalkerFirstTl.getQueue());}
                                    synchronized (WalkerSecondTl){WalkerSecondTl.setQueue(WalkerSecondTl.getQueue() >  0? WalkerSecondTl.getQueue() - 1:WalkerSecondTl.getQueue());}
                                    synchronized (WalkerThirdTl){WalkerThirdTl.setQueue(WalkerThirdTl.getQueue()    >  0? WalkerThirdTl.getQueue() - 1: WalkerThirdTl.getQueue());}
                                    synchronized (WalkerFourthTl){WalkerFourthTl.setQueue(WalkerFourthTl.getQueue() >  0? WalkerFourthTl.getQueue() - 1:WalkerFourthTl.getQueue());}


                                sleep(1500);    //Время перехода одного пешехода

                                }
                                WalkerFirstTl.  setCondition('r');
                                WalkerSecondTl. setCondition('r');
                                WalkerThirdTl.  setCondition('r');
                                WalkerFourthTl. setCondition('r');
                                timer=(int) (currentTimeMillis() / 1000);//Обнуляем таймер
                                operatingMode = true;
                                crossroadMode=operatingMode;

                            }
                        }
                    }

                }
            catch (InterruptedException e){
                //Здесь нужно будет переключить все светофоры на красный
                out.print("Controller is stopped\n");
            }
        };
        final Thread conThread = new Thread(conditionChanger);
        conThread.start();


        //Поток - генератор очередей
        final Runnable queueGen= ()->{
            try {
                while(!currentThread().isInterrupted()){
                    int changeQueue= (int) getRandomIntegerBetweenRange(0,3);       //Выбор дороги для нагрузки
                    int changeWalkerQueue= (int) getRandomIntegerBetweenRange(0,3); //Выбор участка перехода для нагрузки

                    int valueOfCars= (int) getRandomIntegerBetweenRange(0,2);       //Кол-во авто для нагрузки
                    int valueOfWalkers=(int) getRandomIntegerBetweenRange(0,1);     //Кол-во пешеходов для нагрузки

                    //Очереди авто
                    switch (changeQueue){
                        case 0:
                            synchronized (FirstTl){ FirstTl.setQueue((FirstTl.getQueue()+valueOfCars));}
                            break;
                        case 1:
                            synchronized (SecondTl){SecondTl.setQueue((SecondTl.getQueue()+valueOfCars));}
                            break;
                        case 2:
                            synchronized (ThirdTl){ThirdTl.setQueue((ThirdTl.getQueue()+valueOfCars));}
                            break;
                        case 3:
                            synchronized (FourthTl){FourthTl.setQueue((FourthTl.getQueue()+valueOfCars));}
                            break;
                    }

                    //Очереди пешеходов
                    switch (changeWalkerQueue){
                        case 0:
                            synchronized (WalkerFirstTl){WalkerFirstTl.setQueue((WalkerFirstTl.getQueue()+valueOfWalkers));}
                            listOfWalkersQueue.set(changeWalkerQueue, WalkerFirstTl.getQueue());
                            break;
                        case 1:
                            synchronized (WalkerSecondTl){WalkerSecondTl.setQueue((WalkerSecondTl.getQueue()+valueOfWalkers));}
                            listOfWalkersQueue.set(changeWalkerQueue, WalkerSecondTl.getQueue());
                            break;
                        case 2:
                             synchronized (WalkerThirdTl){WalkerThirdTl.setQueue((WalkerThirdTl.getQueue()+valueOfWalkers));}
                            listOfWalkersQueue.set(changeWalkerQueue, WalkerThirdTl.getQueue());
                            break;
                        case 3:
                            synchronized (WalkerFourthTl){WalkerFourthTl.setQueue((WalkerFourthTl.getQueue()+valueOfWalkers));}
                            listOfWalkersQueue.set(changeWalkerQueue, WalkerFourthTl.getQueue());
                            break;
                    }
                    sleep((long) (getRandomIntegerBetweenRange(1,10)*100));
                }
            } catch (InterruptedException e) {
               // e.printStackTrace();
                out.print("Session terminated.\n");
            }
        };
        final Thread queueThread = new Thread(queueGen);
        queueThread.start();

        //Цикл для выгрузки данных о состоянии перекрестка в консоль
        for (int i = 1; i<=STATUS_NUMBER; i++){
        out.print("Status #"+i+"\t"+"Crossroad mode: "
                +(crossroadMode==true? "Cars go": "Walkers go")
                +"\nid\t|\tobjects\t\t|condition\t|queue\n"
                +FirstTl.getInfoOfTL()
                +SecondTl.getInfoOfTL()
                +ThirdTl.getInfoOfTL()
                +FourthTl.getInfoOfTL()
                +"______________________________________\n"
                +WalkerFirstTl.getInfoOfTL()
                +WalkerSecondTl.getInfoOfTL()
                +WalkerThirdTl.getInfoOfTL()
                +WalkerFourthTl.getInfoOfTL()
                +"______________________________________\n"
                //+listOfWalkersQueue
                +"\n");
        //Частота обновления
        sleep(STATUS_UPDATE_INTERVAL);
        }

        //Завершение всех потоков
        conThread.interrupt();
        sleep(50);
        queueThread.interrupt();


    }

    // Функция возвращает значение которое необходимо добавить к задержке работы светофора, учитывая загруженность остальных
    private static int getLoadFactor(int mainQueue, int firstSecondaryQueue, int secondSecondaryQueue, int thirdSecondaryQueue) {
        try{
        return (mainQueue/((firstSecondaryQueue+secondSecondaryQueue+thirdSecondaryQueue)/3)-1)*2;

        }catch (ArithmeticException arithmeticException){
            return 0;
        }
    }

    //Генератор случайного целого числа
    public static double getRandomIntegerBetweenRange(double min, double max){ double x = (int)(Math.random()*((max-min)+1))+min; return x; }


}
