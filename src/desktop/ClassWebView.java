/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

/**
 *
 * @author jessi
 */
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ClassWebView extends JFXPanel {

//Variable encargada de renderizar el website
    private WebEngine engine;

    //Constructor de la clase
    public ClassWebView(String url) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Button button = new Button("Cargar");
                button.setStyle("-fx-font: 18 arial; -fx-base: #265B91;");
                Button next_button = new Button("Cortar");
                next_button.setStyle("-fx-font: 18 arial; -fx-base: #265B91;");
                Button refresh_button = new Button("Reiniciar");
                refresh_button.setStyle("-fx-font: 18 arial; -fx-base: #265B91;");
                WebView view = new WebView();
                engine = view.getEngine();
                engine.setJavaScriptEnabled(true);
                engine.setUserStyleSheetLocation(getClass().getResource("style.css").toString());
                button.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        // Call a JavaScript function of the current page
                        engine.executeScript("upload('" + url + "');");
                    }
                });

                next_button.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        // Call a JavaScript function of the current page
                        engine.executeScript("next();");
                    }
                });

                refresh_button.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        // Call a JavaScript function of the current page
                        engine.executeScript("refresh();");
                    }
                });

                VBox root = new VBox();
                root.setAlignment(Pos.CENTER);
                root.setPadding(new Insets(5));
                root.setSpacing(5);
                root.getChildren().addAll(view, button, next_button, refresh_button);

                setScene(new Scene(root));
            }
        });
        setVisible(true);
    }
//Método para cargar la URL de la página web

    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String tmp = toURL(url);
                if (tmp == null) {
                    tmp = toURL(url);
                }

                engine.load(tmp);

            }
        });
    }

    private String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }
}
