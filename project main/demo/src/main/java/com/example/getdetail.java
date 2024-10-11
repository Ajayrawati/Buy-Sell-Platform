package com.example;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class getdetail extends Frame {

    Label namelll, emaillbl, phonelbl;

    getdetail(String id) {

        setSize(400, 200);
        setTitle("User detail");
        setLayout(new GridLayout(3, 1)); // Setting the GridLayout with 3 rows and 1 column
        centerFrame();
        
        namelll = new Label("Name: ");
        emaillbl = new Label("Email: ");
        phonelbl = new Label("Phone No.: ");

        // Adding the labels to the frame
        add(namelll);
        add(emaillbl);
        add(phonelbl);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        det(id);
        setVisible(true);
    }

    private void det(String id) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("product");
            MongoCollection<Document> collection = database.getCollection("userdetail");

            // Construct the query to find the document by id
            Document query = new Document("id", id);
            FindIterable<Document> result = collection.find(query);

            // Iterate through the results and set label texts
            
            for (Document doc : result) {
                namelll.setText("Name: " + doc.getString("uname"));
                emaillbl.setText("Email id: " + doc.getString("umail"));
                phonelbl.setText("Phone No: " + doc.getString("uphone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) {
        new getdetail("fdjf"); // Example ID for testing
    }
}
