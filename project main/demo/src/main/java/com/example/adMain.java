package com.example;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class adMain extends Frame implements ActionListener {
    private Label Loglbl, UserLbl, PassLbl;
    private TextField UserField, PassField;
    private Button Click, sign;

    public adMain() {
        setSize(400, 200);
        setLayout(new BorderLayout());
        setTitle("Admin LogIn");
        centerFrame();

        Loglbl = new Label("Enter your credentials:", Label.CENTER);
        UserLbl = new Label("Enter Username:");
        PassLbl = new Label("Enter Password:");
        sign = new Button("Sign Up");
        UserField = new TextField(20);
        PassField = new TextField(20);
        PassField.setEchoChar('*');
        Click = new Button("Log In");
        Click.setPreferredSize(new Dimension(100, 50));

        // Create a panel for user and password labels and fields
        Panel inputPanel = new Panel(new GridLayout(4, 1));
        inputPanel.add(UserLbl);
        inputPanel.add(UserField);
        inputPanel.add(PassLbl);
        inputPanel.add(PassField);

        // Create a panel for the sign-up button
        Panel signUpPanel = new Panel(new FlowLayout());
        signUpPanel.add(sign);

        // Create a panel for the login button
        Panel loginPanel = new Panel(new FlowLayout());
        loginPanel.add(Click);

        // Add components to specific regions of the BorderLayout
        add(Loglbl, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(signUpPanel, BorderLayout.EAST);
        add(loginPanel, BorderLayout.SOUTH);

        // Add a WindowListener to handle window closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        // Register action listeners
        sign.addActionListener(this);
        Click.addActionListener(this);

        setVisible(true);
    }

    private void centerFrame() {
        // Get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calculate the new location of the frame
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        // Set the location of the frame
        setLocation(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sign) {
            new signup(); // Open signup window
        } else if (e.getSource() == Click) {
            String userName = UserField.getText();
            String password = PassField.getText();

            if (userName.isEmpty() || password.isEmpty()) {
                System.out.println("Both fields are required.");
                return;
            }

            authenticateUser(userName, password);
        }
    }

    private void authenticateUser(String userName, String password) {
        String uri = "mongodb://localhost:27017";
        String dbName = "auction";
        String collectionName = "auct";

        try (MongoClient mongoClient = new MongoClient(new MongoClientURI(uri))) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document query = new Document("name", userName).append("roo", password);
            Document user = collection.find(query).first();

            if (user != null) {
                new afterLogin();
                
                // Here you can open a new window or perform other actions upon successful login
            } else {
                System.out.println("Invalid credentials.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new adMain();
    }
}
