package app.calc.controller;

import app.calc.model.Calc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.Scanner;

public class Controller {
    @FXML
    public TextArea textInput;
    @FXML
    public TextField textOutput;

    public void doCalc(ActionEvent actionEvent) {
        try {
            Calc calc = new Calc(textInput.getText());
            textOutput.setText(calc.getResult().toString());
            textInput.setText(calc.getOperations());
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().equals(textInput.getText())) //When the input is just one number
                textOutput.setText(iae.getMessage());
            else
                textInput.setText(iae.getMessage());
        }
    }

    public void doClear(ActionEvent actionEvent) {
        textInput.setText("");
        textOutput.setText("");
    }
}