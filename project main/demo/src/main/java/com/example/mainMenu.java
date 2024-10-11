package com.example;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class mainMenu extends Frame implements ActionListener {
    Button Admin, User;
    Label mainlabel;
    Panel p1, p2;

    mainMenu() {
        setLayout(new BorderLayout());
        setSize(400, 200);
        centerFrame();

        
        setTitle("Product System");

        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

       
        p1 = new Panel();
        p2 = new Panel();

        p1.setLayout(new FlowLayout());
        p2.setLayout(new FlowLayout());

        mainlabel = new Label("Welcome to my software", Label.CENTER);
        Admin = new Button("Admin");
        User = new Button("User");

        Admin.setPreferredSize(new Dimension(100, 50));
        User.setPreferredSize(new Dimension(100, 50));

        p1.add(mainlabel);
        p2.add(Admin);
        p2.add(User);

        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.CENTER);

        Admin.addActionListener(this);
        User.addActionListener(this);

       
        setVisible(true);
    }

    private void centerFrame() {
                   
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        
        setLocation(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       
        if (e.getSource() == Admin) {
            new adMain();
        } else if (e.getSource() == User) {
            
            new userMain();
        }
    }

    public static void main(String[] args) {
        new mainMenu();
    }
}
