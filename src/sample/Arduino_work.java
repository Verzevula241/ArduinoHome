package sample;

import arduino.Arduino;
import jssc.SerialPortList;

import java.util.concurrent.TimeUnit;

/**
 * класс для работы с портом
 */
public class Arduino_work {
    public static  Arduino arduino = Controller.arduino;

    /**
     * Метод служит для перебора портов на которых может работать плата
     * @return - возвращает номер рабочего порта
     */
    public String port() {
        String port = null;
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            try {
                System.out.println(portNames[i]);
                Arduino arduino = new Arduino(portNames[i], 9600);
                arduino.openConnection();
                TimeUnit.SECONDS.sleep(1);
                arduino.serialWrite('H');
                TimeUnit.SECONDS.sleep(1);
                String t = arduino.serialRead();
                System.out.println(t);
                if (t.equals("H\n")) port = portNames[i];
                arduino.closeConnection();
            } catch (Exception e) {
                i++;
            }

        }

        return port;
    }

    /**
     *
     * @param t - значение температуры взятое из порта
     * @return - возвращает его в виде int переменной
     */
    public int temp(String t){
        int temp = 0;
        if (t.charAt(0) == 't'){
            String tem = t.substring(1,3);
            temp = Integer.parseInt(tem);}

        return temp;
    }

    /**
     *
     * @param h - значение влажности взятое из порта
     * @return - возвращает его в виде int переменной
     */
    public int hum (String h){
        int temp = 0;
        if (h.charAt(0) == 'h'){
            String hum = h.substring(1,3);
            temp = Integer.parseInt(hum);}

        return temp;
    }

    /**
     *
     * @param s - значение освещенности взятое из порта
     * @return - возвращает его в виде int переменной
     */
    public int sve (String s){
        int temp = 0;
        if (s.charAt(0) == 's'){
            String sve = s.substring(1,3);
            temp = Integer.parseInt(sve);}

        return temp;
    }
}
