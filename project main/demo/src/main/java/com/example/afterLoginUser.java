package com.example;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.bson.Document;
import org.bson.types.Binary;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class afterLoginUser extends Frame {
    Button fetchProduct, nextProduct;
    TextField nameField, contactField, emailField, bidprice;
    Label nameUse, contactNo, email;
    JLabel productName, productPrice, productImage;
    String id;
    private MongoCursor<Document> productCursor;

    afterLoginUser() {
        setTitle("User  Dashboard");
        setSize(800, 400);
        setLayout(new GridLayout(1, 2));
        
        centerFrame();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        Panel leftPanel = new Panel(new GridLayout(7, 1));
        Panel rightPanel = new Panel(new GridLayout(5, 2));

        fetchProduct = new Button("Fetch Product");
        nextProduct = new Button("Next Product");
        productName = new JLabel();
        productPrice = new JLabel();
        productImage = new JLabel();

        leftPanel.add(new Label("Product Name:"));
        leftPanel.add(productName);
        leftPanel.add(new Label("Product Price:"));
        leftPanel.add(productPrice);
        leftPanel.add(new Label("Product Image:"));
        leftPanel.add(productImage);
        leftPanel.add(fetchProduct);
        leftPanel.add(nextProduct);

        fetchProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchProductData();
            }
        });

        nextProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextProduct();
            }
        });

        nameUse = new Label("Name:");
        nameField = new TextField();
        contactNo = new Label("Contact Number:");
        contactField = new TextField();
        email = new Label("Email:");
        emailField = new TextField();
        bidprice = new TextField();

        Button bid = new Button("Sent detail");
        bid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sentdetail();
            }
        });


        rightPanel.add(nameUse);
        rightPanel.add(nameField);
        rightPanel.add(contactNo);
        rightPanel.add(contactField);
        rightPanel.add(email);
        rightPanel.add(emailField);
       
        rightPanel.add(new Label());
        rightPanel.add(bid);

        add(leftPanel);
        add(rightPanel);

        setVisible(true);
    }

    
    public void fetchProductData() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("product");
            MongoCollection<Document> collection = database.getCollection("prod");

            productCursor = collection.find().iterator();
            if (productCursor.hasNext()) {
                Document latestProduct = productCursor.next();
                displayProduct(latestProduct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nextProduct() {
        if (productCursor != null && productCursor.hasNext()) {
            Document nextProduct = productCursor.next();
            displayProduct(nextProduct);
        }
    }

    public void displayProduct(Document product) {
        id = product.getString("id");
        String name = product.getString("name");
        String price = product.getString("price");
        Binary imageBinary = product.get("image", Binary.class);

        productName.setText(name);
        productPrice.setText(price);

        if (imageBinary != null) {
            byte[] imageData = imageBinary.getData();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage image;
            try {
                image = ImageIO.read(bis);
                ImageIcon icon = new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                productImage.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            productImage.setIcon(null);
        }
    }

    private void sentdetail(){
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("product");
            MongoCollection<Document> collection = database.getCollection("userdetail");
            
            String uname = nameField.getText();
            String umail = emailField.getText();
            String uphone = contactField.getText();

            Document doc = new Document("id",id)
            .append("uname", uname)
            .append("umail", umail)
            .append("uphone", uphone);
            collection.insertOne(doc);
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = getSize().width;
        int frameHeight = getSize().height;
        setLocation((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2);
    }

    public static void main(String[] args) {
        new afterLoginUser();
    }
}
