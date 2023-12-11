package cz.muni.fi.xstupka.jsudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Trida Main
 * @author Pavel Stupka
 */
public class Main {
        
    private static final double VERSION = 1.1;
    private static final String YEAR = "2006";
    private static int ar;
    private static String DEFAULT_DIFFICULTY = "35";
    
    /** 
     * Hlavni metoda main
     */
    public static void main(String[] args) {        
        int ar = args.length;

        // kontrola parametru
        if (ar == 1 && args[0].equals("v")) {       // vypis verze
            printVersionAndExit();
        } 
        
        else if (ar == 1 && args[0].equals("h")) {  // vypis napovedy
            printHelpAndExit();
        } 
        
        else if (ar > 1 && args[0].equals("s")) {   // reseni sudoku
            if (ar == 3 && args[1].equals("out")) {
                solveToStdoutAndExit(args[2]);
            } else if (ar == 4 && args[1].equals("txt")) {
                solveToTxtAndExit(args[2], args[3]);
            } else if (ar == 4 && args[1].equals("xhtml")) {
                solveToXhtmlAndExit(args[2], args[3]);
            }
        }
        
        else if (ar > 1 && args[0].equals("g")) {   // generuje sudoku
            if (ar == 2 && args[1].equals("out")) {
                generateSudokuToStdoutAndExit(DEFAULT_DIFFICULTY);
            } else if (ar == 3 && args[1].equals("out")) {
                generateSudokuToStdoutAndExit(args[2]);
            } else if (ar == 3 && args[1].equals("txt")) {
                generateSudokuToTxtAndExit(args[2], DEFAULT_DIFFICULTY);
            } else if (ar == 4 && args[1].equals("txt")) {
                generateSudokuToTxtAndExit(args[2], args[3]);
            } else if (ar == 3 && args[1].equals("xhtml")) {
                generateSudokuToXhtmlAndExit(args[2], DEFAULT_DIFFICULTY);
            } else if (ar == 4 && args[1].equals("xhtml")) {
                generateSudokuToXhtmlAndExit(args[2], args[3]);
            }
        }
        
        printUsageAndExit();
    }
    
    /**
     * Vypise informace o pouziti programu
     */
    private static void printUsageAndExit() {
        System.out.println("");
        System.out.println("Pouziti: java -jar jSudoku.jar {s/g/v/h} {out/txt/xhtml} [input] [output] [d]");
        System.out.println("Spustte \"java -jar jSudoku.jar h\" pro ziskani napovedy");
        System.out.println("");
        System.exit(0);
    }
    
    /**
     * Vypise napovedu o pouziti programu
     */
    private static void printHelpAndExit() {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("jSudoku v" + VERSION + " (MIT License) (C) " + YEAR + ", Pavel Stupka");
        System.out.println("");
        System.out.println("Pouziti: java -jar jSudoku.jar {s/g/v/h} {out/txt/xhtml} [input] [output] [d]");
        System.out.println("");
        System.out.println("s - Resi Sudoku");
        System.out.println("g - Generuje Sudoku");
        System.out.println("v - Vypise verzi programu");
        System.out.println("h - Vypise tuto napovedu");
        System.out.println("");
        System.out.println("out   - Vysledek bude vypsan na standartni vystup");
        System.out.println("txt   - Vysledek bude ulozen do textoveho souboru");
        System.out.println("xhtml - Vysledek bude ulozen do XHTML souboru");
        System.out.println("");
        System.out.println("input  - Vstupni soubor");
        System.out.println("output - Vystupni soubor");
        System.out.println("d      - Obtiznost generovaneho Sudoku (0 az 54)");
        System.out.println("-----------------------------------------------------------------------------");
        System.exit(0);
    }

    /**
     * Vypise napovedu o pouziti programu
     */
    private static void printVersionAndExit() {
        System.out.println("");
        System.out.println("jSudoku v" + VERSION + " (MIT License) (C) " + YEAR + ", Pavel Stupka");
        System.out.println("");
        System.exit(0);
    }

    /**
     * Resi Sudoku a vysledek vypisuje na standartni vystup
     */
    private static void solveToStdoutAndExit(String input) {
        try {
            Sudoku sudoku = new Sudoku(new File(input));
            SudokuTree tree = new SudokuTree(sudoku);
            Set<Sudoku> set = tree.solveSudoku();
            if (set.isEmpty()) {
                System.out.println("Nebylo nalezeno zadne reseni!");
            } else if (set.size() == 1) {
                SudokuPrinter.print((Sudoku) set.toArray()[0]);
            } else {
                int count = 1;
                for (Sudoku sdk : set) {
                    String foo = "============================================\n" +
                                 "Reseni c. " + count + "\n" +
                                 "============================================\n\n";
                    System.out.print(foo);
                    SudokuPrinter.print(sdk);
                    System.out.print("\n");
                    count++;
                }
            }
        } catch (IOException ex) {
            System.err.println("Chyba: Soubor " + input + " nelze otevrit!");
            System.exit(1);
        } catch (NumberFormatException ex) {
            System.err.println("Chyba: Soubor " + input + " ma spatny format!");
            System.exit(1);
        }

        System.exit(0);
    }

    /**
     * Resi Sudoku a vysledek vypisuje do textoveho souboru
     */
    private static void solveToTxtAndExit(String input, String output) {
        Sudoku sudoku = null;
        try {
            sudoku = new Sudoku(new File(input));
        } catch (IOException ex) {
            System.err.println("Chyba: Soubor " + input + " nelze otevrit!");
            System.exit(1);
        } catch (NumberFormatException ex) {
            System.err.println("Chyba: Soubor " + input + " ma spatny format!");
            System.exit(1);
        }
        
        try {
            FileWriter out = new FileWriter(output);
            SudokuTree tree = new SudokuTree(sudoku);
            Set<Sudoku> set = tree.solveSudoku();
            
            if (set.isEmpty()) {
                out.write("Nebylo nalezeno zadne reseni!\n");
            } else if (set.size() == 1) {
                SudokuPrinter.writeToTxt((Sudoku) set.toArray()[0], out);
            } else {
                int count = 1;
                for (Sudoku sdk : set) {
                    String foo = "============================================\n" +
                                 "Reseni c. " + count + "\n" +
                                 "============================================\n\n";
                    out.write(foo);
                    SudokuPrinter.writeToTxt(sdk, out);
                    out.write("\n");
                    count++;
                }
            }
            out.close();
        } catch (IOException ex) {
            System.err.println("Chyba: Soubor " + output + " nelze otevrit!");
            System.exit(1);
        }

        System.exit(0);
    }

    /**
     * Resi Sudoku a vysledek vypisuje do XHTML souboru
     */
    private static void solveToXhtmlAndExit(String input, String output) {
        Sudoku sudoku = null;
        try {
            sudoku = new Sudoku(new File(input));
        } catch (IOException ex) {
            System.err.println("Chyba: Soubor " + input + " nelze otevrit!");
            System.exit(1);
        } catch (NumberFormatException ex) {
            System.err.println("Chyba: Soubor " + input + " ma spatny format!");
            System.exit(1);
        }
        
        try {
            FileWriter out = new FileWriter(output);
            SudokuTree tree = new SudokuTree(sudoku);
            Set<Sudoku> set = tree.solveSudoku();
            
            generateHeadXhtml(out);

            if (set.isEmpty()) {
                out.write("      NEBYLO NALEZENO ZADNE RESENI!</p>\n");
            } else {
                out.write("      VYPIS RESENI!</p>\n");
                int count = 1;
                for (Sudoku sdk : set) {
                    String foo = "      <p><b>Reseni c. " + count + ":</b></p>\n";
                    out.write(foo);
                    SudokuPrinter.writeToXhtml(sdk, out);
                    count++;
                }
            }

            out.write("   </body>\n</html>\n");
            out.close();
        } catch (IOException ex) {
            System.err.println("Chyba: Soubor " + output + " nelze otevrit!");
            System.exit(1);
        }
        
        System.exit(0);
    }

    /**
     * Generuje Sudoku a vysledek vypisuje na standartni vystup
     */
    private static void generateSudokuToStdoutAndExit(String difficulty) {
        int diff = 0;

        try {
            diff = Integer.parseInt(difficulty);   
        } catch (NumberFormatException ex) {
            System.err.println("Chyba: Obtiznost " + difficulty + " ma spatny format!");
            System.exit(1);
        }

        Sudoku sudoku = SudokuGenerator.generateSudoku(54 - diff);
        SudokuPrinter.print(sudoku);
        
        System.exit(0);
    }

    /**
     * Generuje Sudoku a vysledek vypisuje do textoveho souboru
     */
    private static void generateSudokuToTxtAndExit(String output, String difficulty) {
        int diff = 0;

        try {
            diff = Integer.parseInt(difficulty);   
        } catch (NumberFormatException ex) {
            System.err.println("Chyba: Obtiznost " + difficulty + " ma spatny format!");
            System.exit(1);
        }
        
        try {
            FileWriter out = new FileWriter(output);
            Sudoku sudoku = SudokuGenerator.generateSudoku(54 - diff);
            SudokuPrinter.writeToTxt(sudoku, out);
            out.close();
        } catch (IOException ex) {
            System.err.println("Chyba: Soubor " + output + " nelze otevrit!");
            System.exit(1);
        }
        
        System.exit(0);
    }

    /**
     * Generuje Sudoku a vysledek vypisuje do XHTML souboru
     */
    private static void generateSudokuToXhtmlAndExit(String output, String difficulty) {
        int diff = 0;

        try {
            diff = Integer.parseInt(difficulty);
        } catch (NumberFormatException ex) {
            System.err.println("Chyba: Obtiznost " + difficulty + " ma spatny format!");
            System.exit(1);
        }
        
        try {
            if (diff < 0 || diff > 54) {
                diff = 54;
            }
            FileWriter out = new FileWriter(output);
            Sudoku sudoku = SudokuGenerator.generateSudoku(54 - diff);
            generateHeadXhtml(out);
            out.write("      Obtiznost: " + diff + "</p>\n");
            SudokuPrinter.writeToXhtml(sudoku, out);
            out.write("   </body>\n</html>\n");
            out.close();
        } catch (IOException ex) {
            System.err.println("Chyba: Soubor " + output + " nelze otevrit!");
            System.exit(1);
        }

        System.exit(0);
    }
    
    /**
     * Generuje XHTML soubor (pouze cast)
     */
    private static void generateHeadXhtml(FileWriter out) throws IOException{
        out.write("<?xml version='1.0' encoding='iso-8859-2'?>\n\n");
        out.write("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.1//EN' 'http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd'>\n\n");
        out.write("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>\n");
        out.write("   <head>\n");
        out.write("      <title>Sudoku</title>\n");
        out.write("      <meta http-equiv='content-type' content='text/html; charset=iso-8859-2'/>\n");
        out.write("      <style type='text/css'>\n");
        out.write("         table { border: 2px solid black; margin: 0px; }\n");
        out.write("         td { width: 40px; height: 40px; border: 1px solid black; text-align: center; }\n");
        out.write("      </style>\n");
        out.write("   </head>\n");
        out.write("   <body>\n");
        out.write("      <p>Generovano: <b>jSudoku v" + VERSION + "</b> &copy; " + YEAR + ", Pavel Stupka<br/>\n");
    }
}
