/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Omaraelhawary
 */
public class InvoiceLineDialog extends JDialog{
    private JTextField itemName;
    private JTextField itemCount;
    private JTextField itemPrice;
    private JLabel itemNameLabel;
    private JLabel itemCountLabel;
    private JLabel itemPriceLabel;
    private JButton yesBtn;
    private JButton cancelBtn;
    
    public InvoiceLineDialog(InvoiceFrame frame) {
        itemName = new JTextField(20);
        itemNameLabel = new JLabel("Item Name");
        
        itemCount = new JTextField(20);
        itemCountLabel = new JLabel("Item Count");
        
        itemPrice = new JTextField(20);
        itemPriceLabel = new JLabel("Item Price");
        
        yesBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        
        yesBtn.setActionCommand("newLineOK");
        cancelBtn.setActionCommand("newLineCancel");
        
        yesBtn.addActionListener(frame.getActionListener());
        cancelBtn.addActionListener(frame.getActionListener());
        setLayout(new GridLayout(4, 2));
        
        add(itemNameLabel);
        add(itemName);
        add(itemCountLabel);
        add(itemCount);
        add(itemPriceLabel);
        add(itemPrice);
        add(yesBtn);
        add(cancelBtn);
        
        pack();
    }

    public JTextField getItemName() {
        return itemName;
    }

    public JTextField getItemCount() {
        return itemCount;
    }

    public JTextField getItemPrice() {
        return itemPrice;
    }
}
