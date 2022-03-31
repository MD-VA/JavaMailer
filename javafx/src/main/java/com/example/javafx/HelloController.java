package com.example.javafx;

import MailHandler.readMail;
import MailHandler.sendMail;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.util.List;


public class HelloController extends readMail {

    private ChangeListener listener;

    @FXML
    private Label welcomeText;
    @FXML
    private TreeView listingEmail;
    @FXML
    public WebView viewer;

    final String username = "javamail132@gmail.com";
    final String password = "eepzgpxwkzhrimef";



    public void initialize() throws MessagingException {
        TreeItem<Object> root = new TreeItem<>("Boite de réception");

        Message[] messages = readMail.getEmails(username, password);
        for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[i];
            System.out.println("THE EMAIL LIST IS ");
            System.out.println(message.getSubject());
            root.getChildren().add(new TreeItem<>(message.getSubject()));
        }
        listingEmail.setRoot(root);

        listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                System.out.println("The selected item is " + newValue);
            }
        };
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}