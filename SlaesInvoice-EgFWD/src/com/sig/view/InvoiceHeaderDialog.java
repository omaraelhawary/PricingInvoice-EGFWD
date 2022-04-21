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
public class InvoiceHeaderDialog extends JDialog {
    private JTextField custName;
    private JTextField invDate;
    private JLabel custNameLabel;
    private JLabel invDateLabel;
    private JButton yesBtn;
    private JButton cancelBtn;

    public InvoiceHeaderDialog(InvoiceFrame frame) {
        custNameLabel = new JLabel("Customer Name:");
        custName = new JTextField(20);
        invDateLabel = new JLabel("Invoice Date:");
        invDate = new JTextField(20);
        yesBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        
        yesBtn.setActionCommand("newInvoiceOK");
        cancelBtn.setActionCommand("newInvoiceCancel");
        
        yesBtn.addActionListener(frame.getActionListener());
        cancelBtn.addActionListener(frame.getActionListener());
        setLayout(new GridLayout(3, 2));
        
        add(invDateLabel);
        add(invDate);
        add(custNameLabel);
        add(custName);
        add(yesBtn);
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
