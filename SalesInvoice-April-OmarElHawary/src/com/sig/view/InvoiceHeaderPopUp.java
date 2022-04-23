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
public class InvoiceHeaderPopUp extends JDialog {
    private final JTextField custName;
    private final JTextField invDate;
    private final JLabel custNameLabel;
    private final JLabel invDateLabel;
    private final JButton saveBtn;
    private final JButton cancelBtn;

    public InvoiceHeaderPopUp(SalesForm frame) {
        custNameLabel = new JLabel("Customer Name:");
        custName = new JTextField(20);
        invDateLabel = new JLabel("Invoice Date:");
        invDate = new JTextField(20);
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        
        saveBtn.setActionCommand("createInvSave");
        cancelBtn.setActionCommand("createInvCancel");
        
        saveBtn.addActionListener(frame.getInvoicelistener());
        cancelBtn.addActionListener(frame.getInvoicelistener());
        setLayout(new GridLayout(3, 2));
        
        add(invDateLabel);
        add(invDate);
        add(custNameLabel);
        add(custName);
        add(saveBtn);
        add(cancelBtn);
        
        pack();
        
    }

    public JTextField getCustName() {
        return custName;
    }

    public JTextField getInvDate() {
        return invDate;
    }
    
}
