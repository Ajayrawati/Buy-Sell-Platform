import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
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

public class signup extends Frame implements ActionListener {
    Button sibtn;
    Label nameLabel, userLabel;
    TextField nameText, passText;

    signup() {
        setSize(400, 200);
        setTitle("Sign Up");
        setLayout(new GridLayout(3, 2)); // Setting the GridLayout with 3 rows and 2 columns
        centerFrame();

        nameLabel = new Label("Enter Your Name");
        userLabel = new Label("Enter Your Password");
        nameText = new TextField(10);
        passText = new TextField(10);
        passText.setEchoChar('*'); // to hide password characters

        // Initialize the button
        sibtn = new Button("Submit");
        sibtn.setPreferredSize(new Dimension(100, 50));

        // Add components to the frame using GridLayout
        add(nameLabel);
        add(nameText);
        add(userLabel);
        add(passText);
        add(new Label()); // Adding an empty label for spacing
        add(sibtn);

        // Window closing event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        sibtn.addActionListener(this);
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
        String userName = nameText.getText();
        String password = passText.getText();
        if (userName.isEmpty() || password.isEmpty()) {
            System.out.println("Both fields are required.");
        } else {
            registerUser(userName, password);
        }
    }

    private void registerUser(String userName, String password) {
        // MongoDB credentials and connection string
        String uri = "mongodb://localhost:27017";
        String dbName = "auction";
        String collectionName = "loginda";

        MongoClient mongoClient = null;

        try {
            // Create MongoDB client
            mongoClient = new MongoClient(new MongoClientURI(uri));

            // Get database and collection
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Create a new user document
            Document user = new Document("nameuser", userName)
                               .append("passuser", password);

            // Insert the user document into the collection
            collection.insertOne(user);

            System.out.println("User registered successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the MongoDB client
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }

    public static void main(String[] args) {
        new signup();
    }
}
