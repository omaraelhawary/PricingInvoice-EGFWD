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
import com.sig.view.InvoiceFrame;
import com.sig.view.InvoiceHeaderDialog;
import com.sig.view.InvoiceLineDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Omaraelhawary
 */
public class InvoiceListener implements ActionListener {

    private InvoiceFrame frame;
    private InvoiceHeaderDialog headerDialog;
    private InvoiceLineDialog lineDialog;

    public InvoiceListener(InvoiceFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "SaveFiles":
                saveFile();
                break;

            case "LoadFiles":
                loadFile();
                break;

            case "NewInvoice":
                newInvoice();
                break;

            case "DeleteInvoice":
                deleteInvoice();
                break;

            case "NewLine":
                createNewLine();
                break;

            case "DeleteLine":
                deleteLine();
                break;

            case "newInvoiceOK":
                newInvoiceDialogYes();
                break;

            case "newInvoiceCancel":
                newInvoiceDialogCancel();
                break;

            case "newLineCancel":
                newLineDialogCancel();
                break;

            case "newLineOK":
                newLineDialogYES();
                break;
        }
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] arr = headerLine.split(",");
                    String code = arr[0];
                    String invDate = arr[1];
                    String cname = arr[2];
                    int invCode = Integer.parseInt(code);
                    Date invoiceDate = InvoiceFrame.invDateFormat.parse(invDate);
                    InvoiceHeader header = new InvoiceHeader(invCode, cname, invoiceDate);
                    invoiceHeaders.add(header);
                }
                frame.setInvoicesArray(invoiceHeaders);

                result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileChooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
                    for (String lineLine : lineLines) {
                        String[] arr = lineLine.split(",");
                        String code = arr[0];
                        String str2 = arr[1];
                        String itemPrice = arr[2];
                        String qunt = arr[3];
                        int invCode = Integer.parseInt(code);
                        double price = Double.parseDouble(itemPrice);
                        int count = Integer.parseInt(qunt);
                        InvoiceHeader inv = frame.getInvObj(invCode);
                        InvoiceLine line = new InvoiceLine(str2, price, count, inv);
                        inv.getLines().add(line);
                    }
                }
                InvoiceHeaderTable headerTable = new InvoiceHeaderTable(invoiceHeaders);
                frame.setHeaderTable(headerTable);
                frame.getInvHTbl().setModel(headerTable);
                System.out.println("files readed sucessuflly");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newInvoice() {
        headerDialog = new InvoiceHeaderDialog(frame);
        headerDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedInvoiceIndex = frame.getInvHTbl().getSelectedRow();
        if (selectedInvoiceIndex != -1) {
            frame.getInvoicesArray().remove(selectedInvoiceIndex);
            frame.getHeaderTable().fireTableDataChanged();

            frame.getInvLTbl().setModel(new InvoiceLineTable(null));
            frame.setLinesArray(null);
            frame.getCustNameLbl().setText("");
            frame.getInvNumLbl().setText("");
            frame.getInvTotalIbl().setText("");
            frame.getInvDateLbl().setText("");
        }
    }

    private void createNewLine() {
        lineDialog = new InvoiceLineDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteLine() {
        int selectedLineIndex = frame.getInvLTbl().getSelectedRow();
        int selectedInvoiceIndex = frame.getInvHTbl().getSelectedRow();
        if (selectedLineIndex != -1) {
            frame.getLinesArray().remove(selectedLineIndex);
            InvoiceLineTable lineTableModel = (InvoiceLineTable) frame.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            frame.getInvTotalIbl().setText("" + frame.getInvoicesArray().get(selectedInvoiceIndex).getInvoiceTotal());
            frame.getHeaderTable().fireTableDataChanged();
            frame.getInvHTbl().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
        }
    }

    private void saveFile() {
        ArrayList<InvoiceHeader> invoicesArray = frame.getInvoicesArray();
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                String headers = "";
                String lines = "";
                for (InvoiceHeader invoice : invoicesArray) {
                    headers += invoice.toString();
                    headers += "\n";
                    for (InvoiceLine line : invoice.getLines()) {
                        lines += line.toString();
                        lines += "\n";
                    }
                }


                headers = headers.substring(0, headers.length()-1);
                lines = lines.substring(0, lines.length()-1);
                result = fc.showSaveDialog(frame);
                File lineFile = fc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                hfw.write(headers);
                lfw.write(lines);
                hfw.close();
                lfw.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newInvoiceDialogCancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newInvoiceDialogYes() {
        headerDialog.setVisible(false);

        String custName = headerDialog.getCustName().getText();
        String str = headerDialog.getInvDate().getText();
        Date d = new Date();
        try {
            d = InvoiceFrame.invDateFormat.parse(str);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Date Error, reset to today date.", "Invalid date format", JOptionPane.ERROR_MESSAGE);
        }

        int invNum = 0;
        for (InvoiceHeader inv : frame.getInvoicesArray()) {
            if (inv.getInvNum() > invNum) {
                invNum = inv.getInvNum();
            }
        }
        invNum++;
        InvoiceHeader newInv = new InvoiceHeader(invNum, custName, d);
        frame.getInvoicesArray().add(newInv);
        frame.getHeaderTable().fireTableDataChanged();
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newLineDialogCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void newLineDialogYES() {
        lineDialog.setVisible(false);

        String itemName = lineDialog.getItemName().getText();
        String itemQunt = lineDialog.getItemCount().getText();
        String itemPrice = lineDialog.getItemPrice().getText();
        int count = 1;
        double price = 1;
        try {
            count = Integer.parseInt(itemQunt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Cannot convert number", "Invalid number format, please fix", JOptionPane.ERROR_MESSAGE);
        }

        try {
            price = Double.parseDouble(itemPrice);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Cannot convert price", "Invalid number format, please fix", JOptionPane.ERROR_MESSAGE);
        }
        int selectedInvoiceHeader = frame.getInvHTbl().getSelectedRow();
        if (selectedInvoiceHeader != -1) {
            InvoiceHeader invHeader = frame.getInvoicesArray().get(selectedInvoiceHeader);
            InvoiceLine line = new InvoiceLine(itemName, price, count, invHeader);
            frame.getLinesArray().add(line);
            InvoiceLineTable lineTableModel = (InvoiceLineTable) frame.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            frame.getHeaderTable().fireTableDataChanged();
        }
        frame.getInvHTbl().setRowSelectionInterval(selectedInvoiceHeader, selectedInvoiceHeader);
        lineDialog.dispose();
        lineDialog = null;
    }

}
