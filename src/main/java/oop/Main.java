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

public class Main extends Application {
    //globaalsed muutujad
    public static int aknasuurusX = 900;
    public static int aknasuurusY = 800;
    private static Group grupp;
    private static int laualaius = (int) (aknasuurusX * 0.8);
    private static int lauaxyalgus = (aknasuurusX - laualaius) / 2;
    private static int joonexyalgus = lauaxyalgus + (int) ((0.0625) * laualaius);
    private static int joonepikkus = (int) (laualaius * (0.875));
    private static int[][] nuppudeAsukohad = new int[15][15];
    public static final double MAX_FONT_SIZE = aknasuurusX/40.0;

    static {
        grupp = new Group();
        //luuakse mängulaud
        Rectangle ruut = new Rectangle(lauaxyalgus, lauaxyalgus, laualaius, laualaius);
        ruut.setFill(Color.BURLYWOOD);
        grupp.getChildren().add(ruut);

        Line joon;
        // luuakse mängulauale jooned
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
        //Kasutaja saab sisetada mustade nuppudega ja valgete nuppudega mängija nime
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
        Button sisesta = new Button("Mängi!");
        GridPane.setConstraints(sisesta, 1, 0);
        GridPane.setConstraints(sisesta, 1, 0);
        grid.getChildren().addAll(must, valge, sisesta);
        grupp1.getChildren().add(grid);




        int[] käiguArv = {1};

        Group nupud = new Group();
        grupp.getChildren().add(nupud);

        // massiiv nähtamatute ruutude jaoks
        // ruudud on elemendid mängulaual, millele saab klikata ning selle ruudu kohale tekibmängunupp
        Rectangle[][] laud = new Rectangle[15][15];

        //Siin tsüklites luuakse mängunupud
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                ISORuut ruut = new ISORuut(joonexyalgus + i * joonepikkus / 14.0 - joonepikkus / 28.0, joonexyalgus + j * joonepikkus / 14.0 - joonepikkus / 28.0, joonepikkus / 14.0, joonepikkus / 14.0, i, j);
                ruut.setFill(Color.TRANSPARENT);
                //Nupu loomise lambda funktsioon
                ruut.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                    //Musta nupu loomine
                    if (me.getButton().equals(MouseButton.PRIMARY) && käiguArv[0] % 2 != 0) {
                        ISORing ring = new ISORing(ruut.getX() + joonepikkus / 28.0, ruut.getY() + joonepikkus / 28.0, joonepikkus / 30.0, Color.BLACK, käiguArv[0]);

                        nupud.getChildren().add(ring);
                        nuppudeAsukohad[ruut.getJ()][ruut.getI()] = 1;
                    }
                    //valge nupu loomine
                    else {
                        ISORing ring = new ISORing(ruut.getX() + joonepikkus / 28.0, ruut.getY() + joonepikkus / 28.0, joonepikkus / 30.0, Color.SNOW, käiguArv[0]);

                        nupud.getChildren().add(ring);
                        nuppudeAsukohad[ruut.getJ()][ruut.getI()] = -1;
                    }
                    //Kui keegi võidab:
                    if (käiguArv[0] > 8 && onVõit(ruut.getI(), ruut.getJ()))  {
                        // Teatatakse, mis värv võitis
                        Label v6it = new Label(((käiguArv[0] % 2 != 0) ? "Must" : "Valge") + " võitis mängu!");
                        Text text = new Text(v6it.getText());
                        v6it.setFont(new Font(MAX_FONT_SIZE));
                        text.setFont(v6it.getFont());
                        double width = text.getBoundsInLocal().getWidth();
                        v6it.setLayoutY(lauaxyalgus - ((aknasuurusX - laualaius)/4.0));
                        v6it.setLayoutX(lauaxyalgus + laualaius/2.0 - width/2);
                        grupp.getChildren().add(v6it);
                        // Nuppudel olevad numbrid tehakse nähtavaks
                        for (Node x : nupud.getChildren()) {
                            ((ISORing) x).getNr().setTextFill(((ISORing) x).getAlgneTekstiVärv());
                        }

                        //lauale lisatake pealmine kate, et rohkem nuppe ei saaks käia
                        Rectangle kate = new Rectangle(lauaxyalgus, lauaxyalgus, laualaius, laualaius);
                        kate.setFill(Color.TRANSPARENT);
                        grupp.getChildren().add(kate);
                        //Logifaili lisatakse mängijate nimed ja kes võitis
                        try {
                            File f1 = new File("gomokulog.txt");
                            if(!f1.exists()) {
                                f1.createNewFile();
                            }
                            //FileWriter fW = new FileWriter(f1.getName(),true);
                            BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("gomokulog.txt", true), StandardCharsets.UTF_8));
                            bW.write("must: " + must.getText()+ ", valge: " + valge.getText() + ", võitja: " + ((käiguArv[0] % 2 != 0) ? must.getText() : valge.getText()));
                            bW.newLine();
                            bW.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    käiguArv[0] += 1;
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
        //Esimeses aknas nupu "Mängi!" vajutamisel luuakse järgmine aken
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

        // Listenerid akna suuruse muutmisega tegelemiseks
        // Akna minimaalne suurus on määratud algsuurusega, aga akent suuremaks tehes liigub mängulaud selle keskele
        peaLava.widthProperty().addListener((observable, oldValue, newValue) ->
                grupp.setLayoutX((peaLava.getWidth() - laualaius) / 2 - grupp.getLayoutBounds().getMinX())
                                            );
        peaLava.heightProperty().addListener((observable, oldValue, newValue) ->
                grupp.setLayoutY((peaLava.getHeight() - laualaius) / 2 - grupp.getLayoutBounds().getMinY())
                                            );
    }

    public static void main(String[] args) {
        launch(args);
    }

    //Võidu check
    public boolean onVõit(int i, int j) {
        String[][][] sobivadÜmbrused = new String[4][3][3];
        // Käidud nupp saab luua kuni viiese pikkusega rea
        // seega kontrollitakse tema kaugusi alates 1-st ja lõpetades 5-ga
        for (int n = 1; n < 5; n++) {
            int[] koord = {-n, 0, n};
            int pV = nuppudeAsukohad[j][i]; //praeguneVärv
            String[][] sobivÜmbrusN = new String[3][3];
            sobivÜmbrusN[1][1] = "T";
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
                            sobivÜmbrusN[xJ/n + 1][xI/n + 1] = "v";
                            continue;
                        }
                    } catch (EiSobiErind  e) {
                        sobivÜmbrusN[xJ/n + 1][xI/n + 1] = "v";
                        continue;
                    }
                    sobivÜmbrusN[xJ/n + 1][xI/n + 1] = "t";
                }
            }
            sobivadÜmbrused[n-1] = sobivÜmbrusN;
        }

        // Neli suunda, millel saab käidud nupp tekitada viiese rea
        String[] neliSuunda = new String[4];
        //diagonaal1
        neliSuunda[0] = sobivadÜmbrused[3][0][0] + sobivadÜmbrused[2][0][0] + sobivadÜmbrused[1][0][0]+ sobivadÜmbrused[0][0][0]+ "t" + sobivadÜmbrused[0][2][2]+ sobivadÜmbrused[1][2][2]+ sobivadÜmbrused[2][2][2] + sobivadÜmbrused[3][2][2];
        //horisontaal
        neliSuunda[1] = sobivadÜmbrused[3][1][0] + sobivadÜmbrused[2][1][0] + sobivadÜmbrused[1][1][0]+ sobivadÜmbrused[0][1][0]+ "t" + sobivadÜmbrused[0][1][2] + sobivadÜmbrused[1][1][2] + sobivadÜmbrused[2][1][2]+ sobivadÜmbrused[3][1][2];
        //diagonaal2
        neliSuunda[2] = sobivadÜmbrused[3][0][2] + sobivadÜmbrused[2][0][2] + sobivadÜmbrused[1][0][2]+ sobivadÜmbrused[0][0][2]+ "t" + sobivadÜmbrused[0][2][0] + sobivadÜmbrused[1][2][0] + sobivadÜmbrused[2][2][0]+ sobivadÜmbrused[3][2][0];
        //vertikaal
        neliSuunda[3] = sobivadÜmbrused[3][0][1] + sobivadÜmbrused[2][0][1] + sobivadÜmbrused[1][0][1]+ sobivadÜmbrused[0][0][1]+ "t" + sobivadÜmbrused[0][2][1] + sobivadÜmbrused[1][2][1] + sobivadÜmbrused[2][2][1]+ sobivadÜmbrused[3][2][1];

        for (String s : neliSuunda) {
            if (s.matches(".*ttttt.*")) return true;
        }

        return false;
    }

}
