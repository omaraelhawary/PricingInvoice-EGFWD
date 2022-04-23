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
public class InvoiceLinePopUp extends JDialog{
    private final JTextField itemName;
    private final JTextField itemCount;
    private final JTextField itemPrice;
    private final JLabel itemNameLabel;
    private final JLabel itemCountLabel;
    private final JLabel itemPriceLabel;
    private final JButton save;
    private final JButton cancel;
    
    public InvoiceLinePopUp(SalesForm frame) {
        itemName = new JTextField(20);
        itemNameLabel = new JLabel("Item Name");
        
        itemCount = new JTextField(20);
        itemCountLabel = new JLabel("Item Count");
        
        itemPrice = new JTextField(20);
        itemPriceLabel = new JLabel("Item Price");
        
        save = new JButton("Save");
        cancel = new JButton("Cancel");
        
        save.setActionCommand("createLineSave");
        cancel.setActionCommand("createLineCancel");
        
        save.addActionListener(frame.getInvoicelistener());
        cancel.addActionListener(frame.getInvoicelistener());
        setLayout(new GridLayout(4, 2));
        
        add(itemNameLabel);
        add(itemName);
        add(itemCountLabel);
        add(itemCount);
        add(itemPriceLabel);
        add(itemPrice);
        add(save);
        add(cancel);
        
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
