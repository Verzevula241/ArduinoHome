package sample;

import arduino.Arduino;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    public static Stage stage;
    @FXML
    public Button lamp;
    @FXML
    public Text temp;
    @FXML
    public Text temp1;
    @FXML
    public ColorPicker ColorP;
    @FXML
    public TextField textcolor;
    @FXML
    public ImageView caply;

    public ImageView Lamp_img;

    @FXML

    public ToggleButton Lamp_tog;

    public Rectangle grad;
    @FXML
    public Rectangle grad1;
    @FXML
    public Rectangle grad2;
    @FXML
    public Button chartbutt;
    @FXML
    public Button R;
    @FXML
    public Button G;
    @FXML
    public Button B;
    @FXML
    public Button Y;
    @FXML
    public Button clc;
    @FXML
    public Button O;
    @FXML
    public Button V;
    @FXML
    public Button Random;
    @FXML
    public Button Backward;
    @FXML
    public Button Forward;
    @FXML
    public Button Open;
    @FXML
    public Button Close;
    @FXML
    public VBox box;
    @FXML
    public VBox box_hum;

    @FXML
    MenuButton Datch;
    @FXML
    MenuButton Porog;
    @FXML
    MenuButton Dev;
    @FXML
    Slider Porogi;


    @FXML
    CheckMenuItem Temp;
    @FXML
    CheckMenuItem dvig;
    @FXML
    CheckMenuItem Vlag;

    @FXML
    CheckMenuItem Svet;

    @FXML
    CheckMenuItem Diod;

    @FXML
    CheckMenuItem Serv;
    @FXML
    Label Porog_znak;

    @FXML
    Text Test_tries;

    public String port_name;
    Class<?> clazz = this.getClass();
    InputStream img1 = clazz.getResourceAsStream("/sample/img/caply.png");
    InputStream img2 = clazz.getResourceAsStream("/sample/img/caplymid.png");
    InputStream img3 = clazz.getResourceAsStream("/sample/img/caplymin.png");
    InputStream lamp_off = clazz.getResourceAsStream("/sample/img/creative.png");
    InputStream lamp_on = clazz.getResourceAsStream("/sample/img/creative_on.png");
    Image image = new Image(img1);
    Image image2 = new Image(img2);
    Image image3 = new Image(img3);


    public Integer count;
    public Integer Tem;
    public Integer Hum;
    public Integer Sve;
    public double slider_value;
    public String Dvig_sent;
int l = 0;

    public static Arduino arduino = new Arduino();
    int i = 0;
    int a = 0;
    NumberAxis x = new NumberAxis();
    NumberAxis y = new NumberAxis();

    @FXML
    NumberAxis x0 = new NumberAxis();
    NumberAxis y0 = new NumberAxis();

    LineChart numberLineChart = new LineChart(x, y);

    ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
    XYChart.Series series1 = new XYChart.Series();

    LineChart numberLineChart_hum = new LineChart(x0, y0);
    ObservableList<XYChart.Data> datas_hum = FXCollections.observableArrayList();
    XYChart.Series series = new XYChart.Series();

    Arduino_work ar = new Arduino_work();
    public void main(String[] args) {
    }

    /**
     * Класс, обрабатывающий события
     */
    @FXML
   public void initialize() {

        Platform.setImplicitExit(false);
        Lamp_img.setImage(new Image(lamp_off));
        numberLineChart_hum.setMaxHeight(100);

        Porogi.valueProperty().addListener((observable, oldValue, newValue) -> {
            Porog_znak.setText("" + newValue.intValue() + "");
            slider_value = newValue.intValue();
        });


        port_name = ar.port();
        arduino.setPortDescription(port_name);
        arduino.setBaudRate(9600);
        arduino.openConnection();
        caply.setImage(image);
        count = 1;

        Thread t = new Thread(task);
        t.start();

        chart();
        chart_hum();
        Forward.setOnAction(event -> arduino.serialWrite('f'));
        Backward.setOnAction(event -> arduino.serialWrite('b'));
        R.setOnAction(event -> arduino.serialWrite('2'));
        G.setOnAction(event -> arduino.serialWrite('3'));
        B.setOnAction(event -> arduino.serialWrite('4'));
        Y.setOnAction(event -> arduino.serialWrite('5'));
        O.setOnAction(event -> arduino.serialWrite('6'));
        V.setOnAction(event -> arduino.serialWrite('7'));
        Random.setOnAction(event -> arduino.serialWrite('r'));

    }


    /**
     * Метод отправляющий и получающий данные из порта
     */
    public void Temp() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    public void run() {
                        try {
                            //
                            arduino.serialWrite('t');
                            try {
                                TimeUnit.MILLISECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String t = arduino.serialRead();
                            if(t.contains("t"))Tem = ar.temp(t);
                            temp.setText(Tem.toString());
                            l++;
                            TempC();
                            //
                            arduino.serialWrite('h');
                            try {
                                TimeUnit.MILLISECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String h = arduino.serialRead();
                            if(h.contains("h"))Hum = ar.hum(h);
                            temp1.setText(Hum.toString());
                            CaplyC();
                            //
                            arduino.serialWrite('S');

                            String s = arduino.serialRead();
                            if(s.contains("s"))Sve = ar.sve(s);

                            //
                            arduino.serialWrite('d');
                            try {
                                TimeUnit.MILLISECONDS.sleep(800);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Dvig_sent = arduino.serialRead();
                            System.out.print(Dvig_sent);
                            dvig_sel(null);
                            svet_sel(null);
                            vlag_sel(null);
                            svet_sel(null);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 1000, 8000);

    }

    /**
     * Метод для отображения элемента интерфейса
     */
   public void CaplyC() {
        if (Integer.parseInt(temp1.getText()) <= 30) {
            caply.setImage(image3);

        }
        if (Integer.parseInt(temp1.getText()) <= 60) {
            caply.setImage(image);

        }
        if (Integer.parseInt(temp1.getText()) >= 61) {
            caply.setImage(image2);

        }


    }
    /**
     * Метод для отображения элемента интерфейса
     */
    public void TempC() {
        if (Integer.parseInt(temp.getText()) <= 10) {
            grad.setVisible(true);
            grad1.setVisible(false);
            grad2.setVisible(false);
        }
        if (Integer.parseInt(temp.getText()) <= 30) {
            grad.setVisible(false);
            grad1.setVisible(true);
            grad2.setVisible(false);
        }
        if (Integer.parseInt(temp.getText()) >= 31) {
            grad.setVisible(false);
            grad1.setVisible(false);
            grad2.setVisible(true);
        }


    }

    /**
     * Строит график температуры
     */
    public void chart() {
        numberLineChart.setLegendSide(Side.BOTTOM);
        series1.setName("Температура");
        box.getChildren().add(numberLineChart);
        series1.setData(datas);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        int initialDelay = 10;
        int period = 5;
        executor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                datas.add(new XYChart.Data(i, Tem));
                series1.setData(datas);
                i++;
            });
        }, initialDelay, period, TimeUnit.SECONDS);
        numberLineChart.getData().add(series1);

    }

    /**
     * Строит график влажности
     */
    public void chart_hum() {
        numberLineChart_hum.setLegendSide(Side.BOTTOM);
        series.setName("Влажность");
        box_hum.getChildren().add(numberLineChart_hum);
        series.setData(datas_hum);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        int initialDelay = 10;
        int period = 5;
        executor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                datas_hum.add(new XYChart.Data(a, Hum));
                series.setData(datas_hum);
                a++;
            });
        }, initialDelay, period, TimeUnit.SECONDS);
        numberLineChart_hum.getData().add(series);
    }


    /**
     * выбор датчика температуры для проверки
     * @param actionEvent - событие нажатия на ChekMenuItem
     */
    public void temp_sel(ActionEvent actionEvent) {
        if (Temp.isSelected()) {
            if (Tem > slider_value) {
                serv_sel(actionEvent);
                diod_sel(actionEvent);

            } else {
                arduino.serialWrite('1');
                arduino.serialWrite('m');
            }
        }
    }

    /**
     * выбор датчика влажности для проверки
     * @param actionEvent - событие нажатия на ChekMenuItem
     */
    public void vlag_sel(ActionEvent actionEvent) {
        if(Vlag.isSelected()) {
            if (Hum > slider_value) {
                serv_sel(actionEvent);
                diod_sel(actionEvent);
            } else {
                arduino.serialWrite('1');
                arduino.serialWrite('m');
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * выбор датчика освещения для проверки
     * @param actionEvent - событие нажатия на ChekMenuItem
     */
    public void svet_sel(ActionEvent actionEvent) {
        if(Svet.isSelected()) {
            if (Sve > slider_value) {
                serv_sel(actionEvent);
                diod_sel(actionEvent);
            } else {
                arduino.serialWrite('1');
                arduino.serialWrite('m');
            }
        }
    }


    /**
     * выбор светодиода как конечного устройства при работе с расписанием
     * @param actionEvent - событие нажатия на ChekMenuItem
     */
    public void diod_sel(ActionEvent actionEvent) {
        if(Diod.isSelected()){
            arduino.serialWrite('0');}
        else {
            arduino.serialWrite('1');
        }
    }

    /**
     * выбор сервопривода как конечного устройства при работе с расписанием
     * @param actionEvent - событие нажатия на ChekMenuItem
     */
    public void serv_sel(ActionEvent actionEvent) {
        if (Serv.isSelected()) {
            arduino.serialWrite('s');
        } else {
            arduino.serialWrite('m');

        }
    }


    public void tog(ActionEvent actionEvent) {
        if (Lamp_tog.isSelected()) {
            arduino.serialWrite('0');
        } else {
            arduino.serialWrite('1');
        }
    }

    /**
     * задача для запуска в отдельном потоке
     */
    Runnable task = () -> Platform.runLater(() -> Temp());


    /**
     * выбор датчика движения для проверки
     * @param actionEvent - событие нажатия на ChekMenuItem
     */
    public void dvig_sel(ActionEvent actionEvent) {
        if (dvig.isSelected()) {
            if (Dvig_sent.equals("YES\n")) {
                System.out.print("on\n");
                serv_sel(actionEvent);
                diod_sel(actionEvent);
            }
            if (Dvig_sent.equals("NO\n")) {
                System.out.print("off\n");
                arduino.serialWrite('1');
                arduino.serialWrite('m');
            }
        }
    }

}


