# jsudoku
Sudoku generator and solver

The rest of this readme file is in Czech language only.

--------

````
verze: 1.1
licence: MIT License
autor: Pavel Stupka <p.stupka(at)centrum.cz>
````

Program pro reseni a generovani krizovek Sudoku.

Pouziti:
--------

````
java -jar jSudoku.jar {s/g/v/h} {out/txt/xhtml} [input] [output] [d]

s - Resi Sudoku (zadava se 'input')
g - Generuje Sudoku (nezadava se 'input')
v - Vypise verzi programu
h - Vypise napovedu

out   - Vysledek bude vypsan na standartni vystup (nezadava se 'output')
txt   - Vysledek bude ulozen do textoveho souboru (zadava se 'output')
xhtml - Vysledek bude ulozen do XHTML souboru (zadava se 'output')

input  - Vstupni soubor
output - Vystupni soubor
d      - Obtiznost generovaneho Sudoku (0 az 54, nepovinne, implicitne 35)
````

Textovy soubor obsahujici zadani Sudoku ma nasledujici format
(nuly znaci prazdna mista):

````
5 0 6 | 0 0 0 | 0 0 0
3 0 7 | 4 5 6 | 0 8 0
0 2 0 | 8 0 7 | 6 0 5
----------------------
0 0 3 | 7 0 9 | 0 6 0
6 9 1 | 0 3 0 | 7 2 0
8 0 0 | 1 6 0 | 9 4 3
----------------------
0 6 0 | 0 0 8 | 0 0 4
0 5 8 | 0 0 1 | 3 9 0
0 3 9 | 2 0 0 | 0 7 0
````

Priklady pouziti:
-----------------

````
java -jar jSudoku.jar g out
... generuje nove sudoku a vypisuje jen na standartni vystup

java -jar jSudoku.jar g out 45
... to same, ale s obtiznosti 45 (je nutne doplnit 45 cisel)

java -jar jSudoku.jar g txt output.txt
... generuje nove sudoku a uklada jej do textoveho souboru 'output.txt'

java -jar jSudoku.jar g txt output.txt 45
... to same, ale s obtiznosti 45 (je nutne doplnit 45 cisel)

java -jar jSudoku.jar g xhtml output.xhtml
... generuje nove sudoku a uklada jej do XHTML souboru (vhodne pro tisk)

java -jar jSudoku.jar g xhtml output.xhtml 45
... to same, ale s obtiznosti 45 (je nutne doplnit 45 cisel)

java -jar jSudoku.jar s out input.txt
... na standartni vystup vypise reseni sudoku v souboru 'input.txt'

java -jar jSudoku.jar s txt input.txt output.txt
... do 'output.txt' vypise reseni sudoku ze souboru 'input.txt'

java -jar jSudoku.jar s xhtml input.txt output.xhtml
... do 'output.xhtml' vypise reseni sudoku ze souboru 'input.txt'
````

Poznamka:
---------

Na tomto programu jsem si chtel zkusit, za jak dlouho jsem schopnen
rychle vymyslet a implementovat algoritmus pro reseni Sudoku.
Z tohoto duvodu je kod mene kvalitni a chybi testovaci tridy.

Celkova prace: jedna probdela noc, bolest hlavy a oci :)

(plati pro verzi 1.0)

Popis pouziteho algoritmu:
--------------------------

Zadane Sudoku prochazi dokola za sebou od shora dolu po radcich a doplnuje
jednoznacna pole, tedy pole, ktera jsou volna a lze do nich doplnit prave
jedno cislo. Pokud neni doplneno ani jedno cislo pri danem pruchodu, cyklus
konci.

Po skonceni kazdeho pruchodu se testuje, zda-li je Sudoku vyreseno. Pokud
ano, konec. Na dane pole nelze v nejhorsim pripade doplnit zadne cislo.
Sudoku pro tuto variantu tedy nema reseni, take konec.

Nyni je Sudoku opet prochazeno od shora dolu po radcich az se narazi na prvni
volne pole. Pro toto pole jsou vypocitany mozna cisla, ktera lze doplnit.
Pro kazdou variantu je vytvorena vetev n-arniho stromu, ktera je stejnym
algoritmem resena do hloubky.

Nejednoznacne zadane Sudoku muze mit vice reseni. Vsechna reseni jsou
potom nalezena prohledanim do hloubky vytvoreneho n-arniho stromu.

Zmeny ve verzich
----------------

verze 1.1 - upraven algoritmus reseni Sudoku (jednoznacna pole se doplnuji tak
dlouho, dokud je behem jednoho pruchodu doplneno aspon jedno cislo)

verze 1.0 - prvni oficialni verze

verze 0.1 - vyvojova verze (bez generatoru Sudoku)
