/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.controller;

import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceHeaderTable;
import com.sig.model.InvoiceLine;
import com.sig.model.InvoiceLineTable;
import com.sig.view.SalesForm;
import com.sig.view.InvoiceHeaderPopUp;
import com.sig.view.InvoiceLinePopUp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Omaraelhawary
 */
public class SalesListener implements ActionListener, ListSelectionListener  {
    private final SalesForm frame;
    private final DateFormat invDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    
    public SalesListener(SalesForm frame) {
        this.frame = frame;
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "CreateNewInvoice" -> displayNewInvoicePopUp();
            case "DeleteInvoice" -> deleteInvoice();
            case "CreateNewLine" -> displayNewLinePopUp();
            case "DeleteLine" -> deleteLine();
            case "LoadFile" -> loadFile();
            case "SaveFile" -> saveData();
            case "createInvCancel" -> createInvCancel();
            case "createInvSave" -> createInvSave();
            case "createLineCancel" -> createLineCancel();
            case "createLineSave" -> createLineSave();
        }
    }

    private void loadFile() {
        JOptionPane.showMessageDialog(frame, "Select Invoices Header File!", "Attention", JOptionPane.WARNING_MESSAGE);
        JFileChooser openFile = new JFileChooser();
        int result = openFile.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = openFile.getSelectedFile();
            try {
                FileReader invoicesHeaderFr = new FileReader(headerFile);
                BufferedReader invoicesHeaderBr = new BufferedReader(invoicesHeaderFr);
                String headerLine;
                headerLine = null;

                while ((headerLine = invoicesHeaderBr.readLine()) != null) {
                    String[] arr = headerLine.split(",");
                    String invNumStr = arr[0];
                    String invDateStr = arr[1];
                    String custName = arr[2];
                    int invNum = Integer.parseInt(invNumStr);
                    Date invDate = invDateFormat.parse(invDateStr);
                    InvoiceHeader inv = new InvoiceHeader(invNum, custName, invDate);
                    frame.getInvoicesHeaders().add(inv);
                }

                JOptionPane.showMessageDialog(frame, "Select Invoices Lines file!", "Attention", JOptionPane.WARNING_MESSAGE);
                result = openFile.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = openFile.getSelectedFile();
                    BufferedReader linesBr = new BufferedReader(new FileReader(linesFile));
                    String linesLine;
                    linesLine = null;
                    while ((linesLine = linesBr.readLine()) != null) {
                        String[] arr = linesLine.split(",");
                        String invNumStr = arr[0];
                        String itemName = arr[1];
                        String itemPriceStr = arr[2];
                        String itemCountStr = arr[3];
                        int invNum = Integer.parseInt(invNumStr);
                        double itemPrice = Double.parseDouble(itemPriceStr);
                        int itemCount = Integer.parseInt(itemCountStr);
                        InvoiceHeader header = findInvoiceByNum(invNum);
                        InvoiceLine invLine = new InvoiceLine(itemName, itemPrice, itemCount, header);
                        header.getLines().add(invLine);
                    }
                    frame.setInvoiceHeaderTable(new InvoiceHeaderTable(frame.getInvoicesHeaders()));
                    frame.getInvoicesTable().setModel(frame.getInvoiceHeaderTable());
                    frame.getInvoicesTable().validate();
                }
                System.out.println("Done");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Date Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Number Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "File Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Read Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayInvoices();
    }

    private void saveData() {
        String invoicesHeader = "";
        String invoicesLines = "";
        for (InvoiceHeader header : frame.getInvoicesHeaders()) {
            invoicesHeader += header.getDataCSV();
            invoicesHeader += "\n";
            for (InvoiceLine line : header.getLines()) {
                invoicesLines += line.getDataCSV();
                invoicesLines += "\n";
            }
        }
        JOptionPane.showMessageDialog(frame, "Please, select where to save Invoices header data!", "Attention", JOptionPane.WARNING_MESSAGE);
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = fileChooser.getSelectedFile();
            try {
                FileWriter hFW = new FileWriter(headerFile);
                hFW.write(invoicesHeader);
                hFW.flush();
                hFW.close();

                JOptionPane.showMessageDialog(frame, "Please, select where to save Invoices lines data!", "Attention", JOptionPane.WARNING_MESSAGE);
                result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = fileChooser.getSelectedFile();
                    FileWriter lFW = new FileWriter(linesFile);
                    lFW.write(invoicesLines);
                    lFW.flush();
                    lFW.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(frame, "Invoices saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

    }

    private InvoiceHeader findInvoiceByNum(int invNum) {
        InvoiceHeader header = null;
        for (InvoiceHeader inv : frame.getInvoicesHeaders()) {
            if (invNum == inv.getInvNum()) {
                header = inv;
                break;
            }
        }
        return header;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Invoices have been Selected!");
        invoicesTableRowSelected();
    }

    private void invoicesTableRowSelected() {
        int selectedRowIndex = frame.getInvoicesTable().getSelectedRow();
        if (selectedRowIndex >= 0) {
            InvoiceHeader row = frame.getInvoiceHeaderTable().getInvoicesList().get(selectedRowIndex);
            frame.getCustNameTF().setText(row.getCustomerName());
            frame.getInvDateTF().setText(invDateFormat.format(row.getInvDate()));
            frame.getInvNum().setText("" + row.getInvNum());
            frame.getInvTotal().setText("" + row.getInvTotal());
            ArrayList<InvoiceLine> lines = row.getLines();
            frame.setInvoiceLinesTable(new InvoiceLineTable(lines));
            frame.getInvLinesTable().setModel(frame.getInvoiceLinesTable());
            frame.getInvoiceLinesTable().fireTableDataChanged();
        }
    }

    private void displayNewInvoicePopUp() {
        frame.setHeaderPopUp(new InvoiceHeaderPopUp(frame));
        frame.getHeaderPopUp().setVisible(true);
    }

    private void displayNewLinePopUp() {
        frame.setLinePopUp(new InvoiceLinePopUp(frame));
        frame.getLinePopUp().setVisible(true);
    }

    private void createInvCancel() {
        frame.getHeaderPopUp().setVisible(false);
        frame.getHeaderPopUp().dispose();
        frame.setHeaderPopUp(null);
    }

    private void createInvSave() {
        String custName = frame.getHeaderPopUp().getCustName().getText();
        String invDateStr = frame.getHeaderPopUp().getInvDate().getText();
        frame.getHeaderPopUp().setVisible(false);
        frame.getHeaderPopUp().dispose();
        frame.setHeaderPopUp(null);
        try {
            Date invDate = invDateFormat.parse(invDateStr);
            int invNum = getNextInvoiceNum();
            InvoiceHeader invoiceHeader = new InvoiceHeader(invNum, custName, invDate);
            frame.getInvoicesHeaders().add(invoiceHeader);
            frame.getInvoiceHeaderTable().fireTableDataChanged();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format, Please correct to 12-12-2022", "Error", JOptionPane.ERROR_MESSAGE);
        }
        displayInvoices();
    }

    private int getNextInvoiceNum() {
        int max = 0;
        for (InvoiceHeader header : frame.getInvoicesHeaders()) {
            if (header.getInvNum() > max) {
                max = header.getInvNum();
            }
        }
        return max + 1;
    }

    private void createLineCancel() {
        frame.getLinePopUp().setVisible(false);
        frame.getLinePopUp().dispose();
        frame.setLinePopUp(null);
    }

    private void createLineSave() {
        String itemName = frame.getLinePopUp().getItemName().getText();
        String itemCountStr = frame.getLinePopUp().getItemCount().getText();
        String itemPriceStr = frame.getLinePopUp().getItemPrice().getText();
        frame.getLinePopUp().setVisible(false);
        frame.getLinePopUp().dispose();
        frame.setLinePopUp(null);
        int itemCount = Integer.parseInt(itemCountStr);
        double itemPrice = Double.parseDouble(itemPriceStr);
        int headerIndex = frame.getInvoicesTable().getSelectedRow();
        InvoiceHeader invoice = frame.getInvoiceHeaderTable().getInvoicesList().get(headerIndex);

        InvoiceLine invoiceLine = new InvoiceLine(itemName, itemPrice, itemCount, invoice);
        invoice.addInvLine(invoiceLine);
        frame.getInvoiceLinesTable().fireTableDataChanged();
        frame.getInvoiceHeaderTable().fireTableDataChanged();
        frame.getInvTotal().setText("" + invoice.getInvTotal());
        displayInvoices();
    }

    private void deleteInvoice() {
        int invIndex = frame.getInvoicesTable().getSelectedRow();
        InvoiceHeader header = frame.getInvoiceHeaderTable().getInvoicesList().get(invIndex);
        frame.getInvoiceHeaderTable().getInvoicesList().remove(invIndex);
        frame.getInvoiceHeaderTable().fireTableDataChanged();
        frame.setInvoiceLinesTable(new InvoiceLineTable(new ArrayList<InvoiceLine>()));
        frame.getInvLinesTable().setModel(frame.getInvoiceLinesTable());
        frame.getInvoiceLinesTable().fireTableDataChanged();
        frame.getCustNameTF().setText("");
        frame.getInvDateTF().setText("");
        frame.getInvNum().setText("");
        frame.getInvTotal().setText("");
        displayInvoices();
    }

    private void deleteLine() {
        int lineIndex = frame.getInvLinesTable().getSelectedRow();
        InvoiceLine line = frame.getInvoiceLinesTable().getInvoiceLines().get(lineIndex);
        frame.getInvoiceLinesTable().getInvoiceLines().remove(lineIndex);
        frame.getInvoiceLinesTable().fireTableDataChanged();
        frame.getInvoiceHeaderTable().fireTableDataChanged();
        frame.getInvTotal().setText("" + line.getHeader().getInvTotal());
        displayInvoices();
    }

    private void displayInvoices() {
        System.out.println("###########");
        for (InvoiceHeader header : frame.getInvoicesHeaders()) {
            System.out.println(header);
        }
        System.out.println("###########");
    }
    
}
