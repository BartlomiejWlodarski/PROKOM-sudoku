package pl.kompo.view;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.text.Font;

public class SudokuTextField extends TextField {

    public SudokuTextField() {
        super();
        this.setPrefWidth(62);
        this.setPrefHeight(62);
        this.setFont(Font.font(26));
        this.setAlignment(Pos.CENTER);
        //this.setStyle("-fx-opacity: 1.0;");
        this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
    }

    public SudokuTextField(String s) {
        super(s);
        this.setPrefWidth(62);
        this.setPrefHeight(62);
        this.setFont(Font.font(26));
        this.setAlignment(Pos.CENTER);
        //this.setStyle("-fx-opacity: 1.0;");
        this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
    }
}
