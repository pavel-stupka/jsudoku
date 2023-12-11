package cz.muni.fi.xstupka.jsudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Trida Sudoku pro vytvareni a manipulaci s hraci plochou hry Sudoku
 * @author Pavel Stupka
 */
public class Sudoku {
    
    public static final int EMPTY = 0;
    private int[] data;

    /** 
     * Vytvori novou instanci Sudoku
     */
    public Sudoku() {
        data = new int[81];
        clearBoard();
    }
    
    /** 
     * Vytvori novou instanci Sudoku nactenim ze souboru
     * @param file soubor, ze ktereho se ma nacist sudoku
     */
    public Sudoku(File file) throws IOException {
        this();
        loadFromFile(file);
    }
    
    /** 
     * Vytvori novou instanci Sudoku
     * @param parent rodic
     */
    public Sudoku(Sudoku parent) {
        data = new int[81];
        for (int p = 0; p < 81; p++) {
            data[p] = parent.data[p];
        }
    }
    
    /** 
     * Vytvori novou instanci Sudoku
     * @param data sudoku (pole o 81 prvcich, cisla 1-9 nebo Sudoku.EMPTY)
     */
    public Sudoku(int[] data) {
        this.data = data;
    }

    /**
     * Vymaze pole sudoku
     */
    public void clearBoard() {
        for (int p = 0; p < 81; p++) {
            data[p] = EMPTY;
        }
    }
    
    /**
     * Nacte sudoku ze souboru
     * @param file soubor, ze ktereho se ma nacist sudoku
     * @throws IOException
     */
    public void loadFromFile (File file) throws IOException {                
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        String line;
        
        int y = 0;
        for (int p = 0; p < 11; p++) {
            line = br.readLine();
            if (p == 3 || p == 7) {
                continue;
            }

            int number;
            
            number = Integer.parseInt(line.charAt(0) + ""); setData(0, y, number);
            number = Integer.parseInt(line.charAt(2) + ""); setData(1, y, number);
            number = Integer.parseInt(line.charAt(4) + ""); setData(2, y, number);
            number = Integer.parseInt(line.charAt(8) + ""); setData(3, y, number);
            number = Integer.parseInt(line.charAt(10) + ""); setData(4, y, number);
            number = Integer.parseInt(line.charAt(12) + ""); setData(5, y, number);
            number = Integer.parseInt(line.charAt(16) + ""); setData(6, y, number);
            number = Integer.parseInt(line.charAt(18) + ""); setData(7, y, number);
            number = Integer.parseInt(line.charAt(20) + ""); setData(8, y, number);
            

            y++;
        }
        
        br.close();
    }
    
    /**
     * Vraci cislo z dane pozice v poli
     * @param x vodorovna pozice
     * @param y svisla pozice
     * @return cislo na dane pozici
     */
    public int getData(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            throw new IllegalArgumentException("Rozmezi souradnic je 0-8");
        }
        return data[y * 9 + x];
    }
    
    /**
     * Nastavuje cislo na danou pozici v poli
     * @param x vodorovna pozice
     * @param y svisla pozice
     * @param number cislo, ktere se ma nastavit
     */
    public void setData(int x, int y, int number) {
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            throw new IllegalArgumentException("Rozmezi souradnic je 0-8");
        }
        if ( (number >= 1 && number <= 9) || number == EMPTY) {
            data[y * 9 + x] = number;
        } else
        {
            throw new IllegalArgumentException("Rozmezi cisel je 1-9 nebo Sudoku.EMPTY");
        }
    }
    
    /**
     * Vraci pole cisel, ktera mohou byt doplnena na danou pozici
     * @param x x-ova pozice policka
     * @param y y-ova pozice policka
     * @return pole o 9 prvcich typu boolean, index pole plus jedna urcuje
     * cislo a hodnota pole v tomto indexu urcuje, zda je cislo vyhovujici
     * pro dane pole, pr.: pole[2] == true, potom index plus jedna == 3,
     * tedy cislo 3 je vhodne
     * @throws SudokuException pokud neexistuje zadne reseni
     */
    public boolean[] getPossibleNumbers(int x, int y) throws SudokuException {
        if (getData(x, y) != EMPTY) {
            boolean[] numbers = new boolean[] {false, false, false, false, false, false, false, false, false};
            numbers[getData(x, y) - 1] = true;
            return numbers;
        }

        boolean[] row = solveRow(y);
        boolean[] column = solveColumn(x);
        boolean[] quad = solveQuad(x, y);
        
        boolean[] numbers = completeSolutions(row, column, quad);
        
        boolean test = false;
        for (int p = 0; p < 9; p++) {
            if (numbers[p] == true) {
                test = true;
            }
        }
        
        if (!test) {
            throw new SudokuException("Reseni nenalezeno pro pole [" + x + ", " + y + "]");
        }

        return numbers;
    }
    
    /**
     * Vraci informaci o vyreseni sudoku
     * @return true pokud je sudoku vyreseno
     */
    public boolean isSolved() {
        boolean solved = true;
        for (int p = 0; p < 81; p++) {
            if (data[p] == EMPTY) {
                solved = false;
            }
        }
        return solved;
    }

    /**
     * Resi radu
     * @param y y-ova rada bude resena
     * @return pole o 9 prvcich boolean
     */
    private boolean[] solveRow(int y) {
        // pole o 9 prvcich, index+1 je cislo a hodnota boolean urcuje, jestli muze byt pouzito
        boolean[] numbers = new boolean[] {true, true, true, true, true, true, true, true, true};
        
        for (int p = 0; p < 9; p++) {
            if (getData(p, y) != EMPTY) {
                numbers[getData(p, y) - 1] = false;
            }
        }
        
        return numbers;
    }
    
    /**
     * Resi sloupec
     * @param x y-ovy sloupec bude resena
     * @return pole o 9 prvcich boolean
     */
    private boolean[] solveColumn(int x) {
        // pole o 9 prvcich, index+1 je cislo a hodnota boolean urcuje, jestli muze byt pouzito
        boolean[] numbers = new boolean[] {true, true, true, true, true, true, true, true, true};
        
        for (int p = 0; p < 9; p++) {
            if (getData(x, p) != EMPTY) {
                numbers[getData(x, p) - 1] = false;
            }
        }
        
        return numbers;
    }
    
    /**
     * Resi ctverec pro dane pole
     * @param x x-ova pozice pole
     * @param y y-ova pozice pole
     * @return pole o 9 prvcich boolean
     */
    private boolean[] solveQuad(int x, int y) {
        // pole o 9 prvcich, index+1 je cislo a hodnota boolean urcuje, jestli muze byt pouzito
        boolean[] numbers = new boolean[] {true, true, true, true, true, true, true, true, true};
        
        int quadX;
        int quadY;
        
        if (x <= 2) {
            quadX = 0;
        } else if (x <= 5) {
            quadX = 3;
        } else {
            quadX = 6;
        }
        
        if (y <= 2) {
            quadY = 0;
        } else if (y <= 5) {
            quadY = 3;
        } else {
            quadY = 6;
        }
        
        for (int b = quadY; b < quadY + 3; b++) {
            for (int a = quadX; a < quadX + 3; a++) {    
                if (getData(a, b) != EMPTY) {
                    numbers[getData(a, b) - 1] = false;
                }
            }
        }
        
        return numbers;
    }

    /**
     * Kombinuje reseni
     * @param a reseni pro radu
     * @param b reseni pro sloupec
     * @param c reseni pro ctverec
     */
    private boolean[] completeSolutions(boolean[] a, boolean[] b, boolean[] c) {
        // pole o 9 prvcich, index+1 je cislo a hodnota boolean urcuje, jestli muze byt pouzito
        boolean[] numbers = new boolean[] {false, false, false, false, false, false, false, false, false};
        
        for (int p = 0; p < 9; p++) {
            if (a[p] && b[p] && c[p]) {
                numbers[p] = true;
            }
        }

        return numbers;
    }
}
