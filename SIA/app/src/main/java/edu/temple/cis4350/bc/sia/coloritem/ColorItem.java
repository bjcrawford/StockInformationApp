package edu.temple.cis4350.bc.sia.coloritem;

public class ColorItem {

    private String itemText;
    private int itemColorCode;

    public ColorItem(String itemText, int itemColorCode) {
        this.itemText = itemText;
        this.itemColorCode = itemColorCode;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public int getItemColorCode() {
        return itemColorCode;
    }

    public void setItemColorCode(int itemColorCode) {
        this.itemColorCode = itemColorCode;
    }
}
