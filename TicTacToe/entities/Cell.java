package entities;

public class Cell {
    public String symbol;
    public Cell(){
        this.symbol = "";
    }
    public boolean isEmpty(){
        return this.symbol.equals("");
    }
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
}
