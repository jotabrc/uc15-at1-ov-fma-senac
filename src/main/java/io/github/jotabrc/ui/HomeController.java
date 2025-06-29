package io.github.jotabrc.ui;

import io.github.jotabrc.model.FinancialEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class HomeController {

    @FXML
    private VBox vboxHome;

    @FXML
    private TableView<FinancialEntity> table;

    @FXML
    private FlowPane buttons;

    private final ObservableList<FinancialEntity> financialEntities = FXCollections.observableArrayList();


    public void updateTableView() {

    }
}
