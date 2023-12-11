package cz.muni.fi.xstupka.jsudoku;

import java.util.Set;

/**
 * Trida Node
 * @author paja
 */
public class Node {
    
    private Sudoku sudoku;
    private Node[] childs;
    private int numberOfChilds;
    
    /** 
     * Vytvori novou instanci Node
     * @param sudoku k reseni
     */
    public Node(Sudoku sudoku) {
        this.sudoku = sudoku;
        numberOfChilds = 0;
    }
    
    /**
     * Resi sudoku.
     * Nejprve projde nekolikrat vsechna pole a pokud existuje prave jedno 
     * cislo, ktere lze na dane pole doplnit, doplni jej. Pote se prochazeji
     * vsechna pole znovu a pro prvni volne pole se vytvori vetev s variantami.
     * Vetve se pote prochazi do hloubky. Prvni doplnovani se deje opakovane
     * pro zvyseni efektivity. Pokud totiz doplneni neni jednoznacne hned, 
     * muze byt jednoznacne po doplneni nasledujiciho cisla, ktere je 
     * jednoznacne jiz od pocatku.
     */
    public void solveSudoku() {
        try {
            // doplnujeme jednoznacna cisla, dokud muzeme
            int filled = 0;
            do {
                filled = solveEasyNumbers();
                
                // pokud je vyreseno, koncime
                if (sudoku.isSolved()) {
                    numberOfChilds = 0;
                    return;
                }
            } while (filled != 0);
            // vytvori potomky
            createChilds();
        } catch (SudokuException ex) {
            numberOfChilds = 0; // neni reseni, nepotrebujeme delat vetve
        }

        // potomci vytvoreni, pojdme je zkusit :)
        for (int p = 0; p < numberOfChilds; p++) {
            childs[p].solveSudoku();
        }
    }
    
    /**
     * Pokud je sudoku uzlu vyreseno, prida jej do mnoziny. Vola rekurzivne
     * potomky (vetve).
     */
    public void addSolvedSudoku(Set<Sudoku> set) {
        
        if (sudoku.isSolved()) {
            Sudoku foo = new Sudoku(sudoku);
            set.add(foo);
        }
        
        for (int p = 0; p < numberOfChilds; p++) {
            childs[p].addSolvedSudoku(set);
        }
    }

    /**
     * Vraci pocet nasledniku
     * @return pocet nasledniku
     */
    public int getNumberOfChilds() {
        return numberOfChilds;
    }

    /**
     * Vraci daneho naslednika
     * @param child pozice naslednika
     * @return naslednik
     */
    public Node getChild(int child) {
        if (child < 0 || child >= numberOfChilds) {
            throw new IllegalArgumentException("Spatny index naslednika");
        }
        return childs[child];
    }
    
    /**
     * Vraci sudoku uzlu
     * @return sudoku uzlu
     */
    public Sudoku getSudoku() {
        return sudoku;
    }
    
    /**
     * Doplnuje jednoznacna cisla
     */
    private int solveEasyNumbers() throws SudokuException {
        
        // pocet doplnenych cisel
        int filled = 0;
        
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                boolean[] nums = sudoku.getPossibleNumbers(x, y);
                
                // spocitame pocet moznych cisel
                int count = 0;
                for (int p = 0; p < 9; p++) {
                    if (nums[p]) {
                        count++;
                    }
                }
                
                // pokud je pocet roven 1, doplnime toto cislo
                if (count == 1) {
                    for (int p = 0; p < 9; p++) {
                        if (nums[p] && sudoku.getData(x, y) == Sudoku.EMPTY) {
                            sudoku.setData(x, y, p+1);
                            filled++;
                        }
                    }    
                }
            }
        }
        // vratime pocet doplnenych cisel
        return filled;
    }
    
    /**
     * Vytvori vetve variant pro prvni volne pole
     */
    private void createChilds() throws SudokuException {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (sudoku.getData(x, y) == Sudoku.EMPTY) {
                    // nasli jsme prvni volne pole, pojdme vytvorit vetve
                    boolean[] nums = sudoku.getPossibleNumbers(x, y);
                    
                    // spocitame pocet moznych cisel
                    int count = 0;
                    for (int p = 0; p < 9; p++) {
                        if (nums[p]) count++;
                    }
                    
                    // vytvorime potomky
                    numberOfChilds = count;
                    childs = new Node[numberOfChilds];
                    
                    // vytvorime jednotlive vetve
                    int foo = 0;
                    for (int p = 0; p < 9; p++) {
                        if (nums[p]) {
                           Sudoku newSudoku = new Sudoku(sudoku);
                           newSudoku.setData(x, y, p+1);
                           childs[foo] = new Node(newSudoku);
                           foo++;
                        }
                    }
                    return;
                }
            }
        }
    }
}
