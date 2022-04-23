package com.sig.model;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Omaraelhawary
 */
public class InvoiceLineTable extends AbstractTableModel {
    private final List<InvoiceLine> invoiceLines;
    
    public InvoiceLineTable(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }
    
    /**
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return invoiceLines.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Item Name";
            case 1 -> "Item Price";
            case 2 -> "Count";
            case 3 -> "Item Total";
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> String.class;
            case 1 -> Double.class;
            case 2 -> Integer.class;
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
        InvoiceLine row = invoiceLines.get(rowIndex);
        
        return switch (columnIndex) {
            case 0 -> row.getItemName();
            case 1 -> row.getItemPrice();
            case 2 -> row.getItemCount();
            case 3 -> row.getLineTotal();
            default -> "";
        };
        
    }
    
}
