package cz.muni.fi.xstupka.jsudoku;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Trida SudokuPrinter pro vystup hraci plochy Sudoku
 * @author Pavel Stupka
 */
public class SudokuPrinter {
    
    
    /**
     * Vypise sudoku do souboru XHTML
     * @param sudoku ktere se ma vypsat
     * @param out FileWriter handler
     * @throws IOException
     */
    public static void writeToXhtml(Sudoku sudoku, FileWriter out) throws IOException {
        // prochazim po ctvercich
        out.write("\n");
        for (int b = 0; b < 9; b += 3) {
            for(int a = 0; a < 9; a += 3) {
                
                if (a == 0 || a ==3) {
                    out.write("      <table cellspacing='0' style='float: left'>\n");
                } else {
                    out.write("      <table cellspacing='0'>\n");
                }

                for (int y = b; y < b + 3; y++) {
                    out.write("         <tr>");
                    for (int x = a; x < a + 3; x++) {
                        out.write("<td>");
                        if (sudoku.getData(x, y) != Sudoku.EMPTY) {
                            String foo = "" + sudoku.getData(x, y);
                            out.write(foo);
                        } else {
                            out.write(" ");
                        }                            
                        out.write("</td>");
                    }
                    out.write("</tr>\n");
                }
                
                out.write("      </table>\n");
            }
        }
        out.write("\n");
    }   
    
    /**
     * Vypise sudoku do souboru (plain text)
     * @param sudoku ktere se ma vypsat
     * @param out FileWriter handler
     * @throws IOException
     */
    public static void writeToTxt(Sudoku sudoku, FileWriter out) throws IOException {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                String foo;
                switch (x) {
                    case 0:
                        foo = sudoku.getData(x, y) + " ";
                        break;
                    case 8:
                        foo = sudoku.getData(x, y) + "\n";
                        break;
                    case 2:
                    case 5:
                        foo = sudoku.getData(x, y) + " | ";
                        break;
                    default:
                        foo = sudoku.getData(x, y) + " ";
                        break;
                }
                out.write(foo);
            }
            if (y == 2 || y == 5) {
                    out.write("----------------------\n");
            }
        }
    }
    
    /**
     * Vypise sudoku
     * @param sudoku ktere se ma vypsat
     */
    public static void print(Sudoku sudoku) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                switch (x) {
                    case 8:
                        System.out.println(sudoku.getData(x, y));
                        break;
                    case 2:
                    case 5:
                        System.out.print(sudoku.getData(x, y) + " | ");
                        break;
                    default:
                        System.out.print(sudoku.getData(x, y) + " ");
                        break;
                }
            }
            if (y == 2 || y == 5) {
                    System.out.println("----------------------");
            }
        }
    }
}
