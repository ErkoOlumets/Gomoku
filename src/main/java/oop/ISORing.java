package oop;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ISORing extends Group {
    // See klass käitub efektiivselt Circle isendina,
    // sest nii Circle kui ka Group on Node alamklassid
    // Mängunupp koosneb käigule vastavalt mustast või valgest ringist
    // ning arvust, mis tähistab, mis käigul see käidi
    // arv on algselt nähtamatu
    private int käik;
    final double NUPP_FONT_SIZE = Main.MAX_FONT_SIZE / 2;
    private Color algneTekstiVärv;
    private Label nr;

    public ISORing(double centerX, double centerY, double radius, Paint fill, int käik) {
        super();
        this.getChildren().add(new Circle(centerX, centerY, radius, fill));
        this.käik = käik;
        this.algneTekstiVärv = käik % 2 == 0 ? Color.BLACK : Color.SNOW;
        nr = new Label("" + käik);
        Text text = new Text(nr.getText());
        nr.setFont(new Font(NUPP_FONT_SIZE));
        text.setFont(nr.getFont());
        double width = text.getBoundsInLocal().getWidth();
        nr.setLayoutY(centerY - NUPP_FONT_SIZE / 2);
        nr.setLayoutX(centerX - width / 2);
        nr.setTextFill(Color.TRANSPARENT);
        this.getChildren().add(nr);
    }


    public Label getNr() { return nr; }
    public Color getAlgneTekstiVärv() { return algneTekstiVärv; }

}
