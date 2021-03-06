package oop;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class uus extends Application {
    //globaalsed muutujad
    public static int aknasuurusX = 1000;
    public static int aknasuurusY = 900;
    private static Group grupp;
    private static int laualaius = (int) (aknasuurusX * 0.8);
    private static int lauaxyalgus = (aknasuurusX - laualaius) / 2;
    private static int joonexyalgus = lauaxyalgus + (int) ((0.0625) * laualaius);
    private static int joonepikkus = (int) (laualaius * (0.875));
    private static int[][] nuppudeAsukohad = new int[15][15];
    public static final double MAX_FONT_SIZE = aknasuurusX/40.0;
    private static String[] t2hed = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
    private static String[] nrid = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};

    static {
        grupp = new Group();
        Rectangle ruut = new Rectangle(lauaxyalgus, lauaxyalgus, laualaius, laualaius);
        ruut.setFill(Color.BURLYWOOD);
        grupp.getChildren().add(ruut);

        Line joon;

        for (int i = 0; i < 15; i++) {
            joon = new Line(joonexyalgus, joonexyalgus + i * joonepikkus / 14.0, joonexyalgus + joonepikkus, joonexyalgus + (i) * joonepikkus / 14.0);
            joon.setFill(Color.BLACK);
            grupp.getChildren().add(joon);
        }
        for (int i = 0; i < 15; i++) {
            joon = new Line(joonexyalgus + i * joonepikkus / 14.0, joonexyalgus, joonexyalgus + (i) * joonepikkus / 14.0, joonexyalgus + joonepikkus);
            joon.setFill(Color.BLACK);
            grupp.getChildren().add(joon);
        }
    }

    public void start(Stage peaLava) {
        //Luuakse esimene aken, kahe tekstikastiga
        //Kasutaja saab sisetada mustade nuppudega ja valgete nuppudega m??ngija nime
        Group grupp1 = new Group();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        final TextField must = new TextField();
        must.setPromptText("Sisesta musta nimi");
        GridPane.setConstraints(must, 0, 0);
        final TextField valge = new TextField();
        valge.setPromptText("Sisesta valge nimi");
        GridPane.setConstraints(valge, 0, 1);
        Button sisesta = new Button("M??ngi!");
        GridPane.setConstraints(sisesta, 1, 0);
        GridPane.setConstraints(sisesta, 1, 0);
        grid.getChildren().addAll(must, valge, sisesta);
        grupp1.getChildren().add(grid);

        Rectangle[][] laud = new Rectangle[15][15];


        int[] k??iguArv = {1};

        Group nupud = new Group();
        grupp.getChildren().add(nupud);

        //Siin ts??klites luuakse m??ngulaud ja m??ngunupud
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                ISORuut ruut = new ISORuut(joonexyalgus + i * joonepikkus / 14.0 - joonepikkus / 28.0, joonexyalgus + j * joonepikkus / 14.0 - joonepikkus / 28.0, joonepikkus / 14.0, joonepikkus / 14.0, i, j);
                ruut.setFill(Color.TRANSPARENT);
                //Nupu loomise lambda funktsioon
                ruut.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                    //Musta nupu loomine
                    if (me.getButton().equals(MouseButton.PRIMARY) && k??iguArv[0] % 2 != 0) {
                        ISORing ring = new ISORing(ruut.getX() + joonepikkus / 28.0, ruut.getY() + joonepikkus / 28.0, joonepikkus / 30.0, Color.BLACK, k??iguArv[0]);

                        nupud.getChildren().add(ring);
                        nuppudeAsukohad[ruut.getJ()][ruut.getI()] = 1;
                    }
                    //valge nupuloomine
                    else /*if (me.getButton().equals(MouseButton.PRIMARY) && must[0] % 2 != 0) */ {
                        ISORing ring = new ISORing(ruut.getX() + joonepikkus / 28.0, ruut.getY() + joonepikkus / 28.0, joonepikkus / 30.0, Color.SNOW, k??iguArv[0]);

                        nupud.getChildren().add(ring);
                        nuppudeAsukohad[ruut.getJ()][ruut.getI()] = -1;
                    }
                    //V??idu check
                    if (k??iguArv[0] > 8 && onV??it(ruut.getI(), ruut.getJ()))  {
                        /*
                        String v2rv = (k??iguArv[0] % 2 == 0) ? "Must" : "Valge";
                        System.out.println(v2rv + " v??itis m??ngu!");

                         */
                        //V??idu korral teatatakse, mis v??rv v??itis
                        Label v6it = new Label(((k??iguArv[0] % 2 != 0) ? "Must" : "Valge") + " v??itis m??ngu!");
                        Text text = new Text(v6it.getText());
                        v6it.setFont(new Font(MAX_FONT_SIZE));
                        text.setFont(v6it.getFont());
                        double width = text.getBoundsInLocal().getWidth();
                        v6it.setLayoutY(lauaxyalgus - ((aknasuurusX - laualaius)/4));
                        v6it.setLayoutX(lauaxyalgus + laualaius/2 - width/2);
                        grupp.getChildren().add(v6it);
                        //nuppudele lisatakse numbrid
                        for (Node x : nupud.getChildren()) {
                            ((ISORing) x).getNr().setTextFill(((ISORing) x).getAlgneTekstiV??rv());
                        }

                        Rectangle kate = new Rectangle(lauaxyalgus, lauaxyalgus, laualaius, laualaius);
                        kate.setFill(Color.TRANSPARENT);
                        grupp.getChildren().add(kate);
                        //Logifaili lisatakse m??ngijate nimed ja kes v??itis
                        try {
                            File f1 = new File("gomokulog.txt");
                            if(!f1.exists()) {
                                f1.createNewFile();
                            }
                            //FileWriter fW = new FileWriter(f1.getName(),true);
                            BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("gomokulog.txt", true), StandardCharsets.UTF_8));
                            bW.write("must: " + must.getText()+ ", valge: " + valge.getText() + ", v??itja: " + ((k??iguArv[0] % 2 != 0) ? must.getText() : valge.getText()));
                            bW.newLine();
                            bW.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        /*
                        try(PrintWriter kirje = new PrintWriter("gomokulog.txt", StandardCharsets.UTF_8);){
                            kirje.println("must: " + must.getText()+ ", valge: " + valge.getText() + ", v??itja: " + ((k??iguArv[0] % 2 != 0) ? must.getText() : valge.getText()));

                        } catch (IOException e){
                            System.out.println("kui seda loed oled ...");
                        }
                        for (Node x : nupud.getChildren()) {
                            ((ISORing) x).getNr().setTextFill(((ISORing) x).getAlgneTekstiV??rv());
                        }

                         */

                    }
                    k??iguArv[0] += 1;
                    grupp.getChildren().remove(ruut);
                });

                laud[i][j] = ruut;
            }
        }

        for (Rectangle[] ruudud : laud) {
            for (Rectangle ruut : ruudud) {
                grupp.getChildren().add(ruut);
            }

        }
        //Esimeses aknas nupu vajutamisel luuakse teine aken
        sisesta.setOnAction(e -> {

            if (must.getText().length() > 10) System.exit(-1);
            Label m = new Label(must.getText(), new Circle(joonepikkus / 30.0, Color.BLACK));
            Label v = new Label(valge.getText());

            Label vnupp = new Label("", new Circle(joonepikkus / 30.0, Color.SNOW));
            m.setFont(new Font(MAX_FONT_SIZE));
            v.setFont(new Font(MAX_FONT_SIZE));
            Text text = new Text(v.getText());
            text.setFont(v.getFont());
            double width = text.getBoundsInLocal().getWidth();
            m.setLayoutX(lauaxyalgus);
            m.setLayoutY(lauaxyalgus - ((aknasuurusX - laualaius)/4.0));
            vnupp.setLayoutX(lauaxyalgus + laualaius - joonepikkus / 15.0);
            v.setLayoutX(lauaxyalgus + laualaius - joonepikkus / 15.0 - width - 10);
            vnupp.setLayoutY(lauaxyalgus - ((aknasuurusX - laualaius)/4.0));
            v.setLayoutY(lauaxyalgus - ((aknasuurusX - laualaius)/4.0) + 5);
            grupp.getChildren().addAll(m, v, vnupp);

            Scene stseen = new Scene(grupp, aknasuurusX, aknasuurusY);

            stseen.setFill(Color.LIGHTGRAY);
            peaLava.setResizable(true);
            peaLava.setScene(stseen);

            peaLava.setMinWidth(aknasuurusX);
            peaLava.setMinHeight(aknasuurusY);
            peaLava.show();

        });

        Scene StartScreen = new Scene(grupp1, 240, 100);
        peaLava.setScene(StartScreen);
        peaLava.setResizable(false);
        peaLava.setTitle("Gomoku");
        peaLava.show();

        //Listenerid akna suuruse muutmisega tegelemiseks
        peaLava.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                grupp.setLayoutX((peaLava.getWidth() - laualaius) / 2 - grupp.getLayoutBounds().getMinX());
                //laualaius = (int) (peaLava.getWidth() * 0.8);
            }
        });
        peaLava.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                grupp.setLayoutY((peaLava.getHeight() - laualaius) / 2 - grupp.getLayoutBounds().getMinY());
                //laualaius = (int) (peaLava.getHeight() * 0.8);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    /*
    @Override
    public void stop() {
        for (int[] ints : nuppudeAsukohad) {
            //System.out.println(Arrays.toString(ints));
        }
    }

     */
    //V??idu check
    public boolean onV??it(int i, int j) {
        String[][][] sobivad??mbrused = new String[4][3][3];
        for (int n = 1; n < 5; n++) {
            int[] koord = {-n, 0, n};
            int pV = nuppudeAsukohad[j][i]; //preguneV??rv
            String[][] ebasobiv??mbrusN = new String[3][3];
            ebasobiv??mbrusN[1][1] = "T";
            //System.out.println(Arrays.deepToString(ebasobiv??mbrusN));
            for (int xI : koord) {
                for (int xJ : koord) {
                    if (xI == 0 && xJ == 0) {
                        continue;
                    }
                    try {
                        try{
                        if (nuppudeAsukohad[j + xJ][i + xI] != pV) {
                            throw new EiSobiErind();
                        }}catch (ArrayIndexOutOfBoundsException e){
                            ebasobiv??mbrusN[xJ/n + 1][xI/n + 1] = "v";
                            continue;
                        }
                    } catch (EiSobiErind  e) {
                        ebasobiv??mbrusN[xJ/n + 1][xI/n + 1] = "v";
                        continue;
                    }
                    ebasobiv??mbrusN[xJ/n + 1][xI/n + 1] = "t";
                }
            }
            sobivad??mbrused[n-1] = ebasobiv??mbrusN;
        }
        String[] neliSuunda = new String[4];
        //diagonaal1
        neliSuunda[0] = sobivad??mbrused[3][0][0] + sobivad??mbrused[2][0][0] + sobivad??mbrused[1][0][0]+ sobivad??mbrused[0][0][0]+ "t" + sobivad??mbrused[0][2][2]+ sobivad??mbrused[1][2][2]+ sobivad??mbrused[2][2][2] + sobivad??mbrused[3][2][2];
        //horisontaal
        neliSuunda[1] = sobivad??mbrused[3][1][0] + sobivad??mbrused[2][1][0] + sobivad??mbrused[1][1][0]+ sobivad??mbrused[0][1][0]+ "t" + sobivad??mbrused[0][1][2] + sobivad??mbrused[1][1][2] + sobivad??mbrused[2][1][2]+ sobivad??mbrused[3][1][2];
        //diagonaal2
        neliSuunda[2] = sobivad??mbrused[3][0][2] + sobivad??mbrused[2][0][2] + sobivad??mbrused[1][0][2]+ sobivad??mbrused[0][0][2]+ "t" + sobivad??mbrused[0][2][0] + sobivad??mbrused[1][2][0] + sobivad??mbrused[2][2][0]+ sobivad??mbrused[3][2][0];
        //vertikaal
        neliSuunda[3] = sobivad??mbrused[3][0][1] + sobivad??mbrused[2][0][1] + sobivad??mbrused[1][0][1]+ sobivad??mbrused[0][0][1]+ "t" + sobivad??mbrused[0][2][1] + sobivad??mbrused[1][2][1] + sobivad??mbrused[2][2][1]+ sobivad??mbrused[3][2][1];

        for (String s : neliSuunda) {
            if (s.matches(".*ttttt.*")) return true;
        }

        return false;
    }

}
