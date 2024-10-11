package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

class MongoDBManager {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBManager() {
        mongoClient = MongoClients.create("mongodb://localhost:27017"); // Replace with your MongoDB connection string
        database = mongoClient.getDatabase("product"); // Connect to 'product' database
        collection = database.getCollection("prod"); // Get reference to 'prod' collection
    }

    public void insertItem(String id, String name, String price, byte[] imageData) {
        Document doc = new Document("id", id)
                            .append("name", name)
                            .append("price", price)
                            .append("image", imageData);

        collection.insertOne(doc);
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}

public class afterLogin extends JFrame {
    private JButton listItemsBtn, uploadImageBtn, getBuyerDetailBtn;
    private JTextField idField, nameField, priceField;
    private JLabel uploadedImageLabel;

    private MongoDBManager mongoManager;

    afterLogin() {
        mongoManager = new MongoDBManager();

        setTitle("Admin Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1, 1)); // Changed to single panel layout

        JPanel rightPanel = new JPanel(new GridLayout(4, 1)); // Adjusted to accommodate 4 components
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        idField = new JTextField();
        nameField = new JTextField();
        priceField = new JTextField();

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);

        rightPanel.add(inputPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension buttonSize = new Dimension(120, 30);

        listItemsBtn = new JButton("List Item");
        listItemsBtn.setPreferredSize(buttonSize);
        listItemsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get input values from fields
                String id = idField.getText();
                String name = nameField.getText();
                String price = priceField.getText();

                // Get image data from uploadedImageLabel
                ImageIcon icon = (ImageIcon) uploadedImageLabel.getIcon();
                Image image = icon.getImage();
                ImageIcon scaledIcon = new ImageIcon(image.getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT));
                Image scaledImage = scaledIcon.getImage();

                // Convert Image to byte array
                BufferedImage bufferedImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(scaledImage, 0, 0, null);
                g2d.dispose();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(bufferedImage, "jpg", baos);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                byte[] imageData = baos.toByteArray();

                // Insert item into MongoDB
                mongoManager.insertItem(id, name, price, imageData);
            }
        });

        buttonPanel.add(listItemsBtn);

        uploadImageBtn = new JButton("Upload Image");
        uploadImageBtn.setPreferredSize(buttonSize);
        uploadImageBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        File destFile = new File(selectedFile.getName());
                        Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        ImageIcon originalImageIcon = new ImageIcon(destFile.getAbsolutePath());
                        int preferredWidth = 300;
                        int originalWidth = originalImageIcon.getIconWidth();
                        int originalHeight = originalImageIcon.getIconHeight();
                        int scaledWidth = preferredWidth;
                        int scaledHeight = originalHeight * scaledWidth / originalWidth;
                        Image scaledImage = originalImageIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
                        uploadedImageLabel.setIcon(scaledImageIcon);
                        uploadedImageLabel.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
                        uploadedImageLabel.revalidate();
                        uploadedImageLabel.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        buttonPanel.add(uploadImageBtn);

        getBuyerDetailBtn = new JButton("Get Buyer Detail");
        getBuyerDetailBtn.setPreferredSize(buttonSize);
        getBuyerDetailBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new getdetail(idField.getText());
            }
        });
        buttonPanel.add(getBuyerDetailBtn);

        rightPanel.add(buttonPanel);

        uploadedImageLabel = new JLabel();
        uploadedImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uploadedImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rightPanel.add(uploadedImageLabel);

        add(rightPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new afterLogin();
    }
}
