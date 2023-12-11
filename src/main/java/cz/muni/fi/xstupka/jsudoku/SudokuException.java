package cz.muni.fi.xstupka.jsudoku;

/**
 * Trida SudokuException
 * @author Pavel Stupka
 */
public class SudokuException extends Exception {
    
    /** 
     * Vytvori novou instanci SudokuException
     * @param cause popis vyjimky
     */
    public SudokuException(String cause) {
        super(cause);
    }

}
