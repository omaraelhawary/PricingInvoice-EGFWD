/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Omaraelhawary
 */
public class InvoiceHeaderTable extends AbstractTableModel {

    private final List<InvoiceHeader> invoicesList;
    private final DateFormat invDateFromat;
    
    public InvoiceHeaderTable(List<InvoiceHeader> invoicesList) {
        this.invDateFromat = new SimpleDateFormat("dd-MM-yyyy");
        this.invoicesList = invoicesList;
    }

    public List<InvoiceHeader> getInvoicesList() {
        return invoicesList;
    }
    
    
    @Override
    public int getRowCount() {
        return invoicesList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "No.";
            case 1 -> "Date";
            case 2 -> "Customer";
            case 3 -> "Total";
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Integer.class;
            case 1 -> String.class;
            case 2 -> String.class;
            case 3 -> Double.class;
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader row = invoicesList.get(rowIndex);
        
        return switch (columnIndex) {
            case 0 -> row.getInvNum();
            case 1 -> invDateFromat.format(row.getInvDate());
            case 2 -> row.getCustomerName();
            case 3 -> row.getInvTotal();
            default -> "";
        };
        
    }
    
}
