package raf.dsw.classycraft.app.gui.swing.view;

import javax.swing.*;

public class UserGuideFrame extends JFrame {
    public UserGuideFrame(){
        this.setTitle("User Guide");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);

        JTextArea guideText = new JTextArea(10, 40);
        guideText.setText("UPUTSTVO ZA KREIRANJE INTERKLASE: \n" +
                        "Ime interklase mora poceti sa +,-,#,~ da bi joj zadali vidljivost. Moze ostati i prazno polje. \n" +
                        "Klase su crvene boje, Apstraktne klase su zute boje, Interfejs je zelene boje, Enum je plave boje\n" +
                        "TIP moze biti: int, boolean, String, double,  ILI NAZIV BILO KOJE POSTOJECE KLASE KOJU SMO PRETHODNO DODALI (ima i ta opcija)\n " +
                        "Atributi su u obliku    vidljivost + naziv atributa: + tip  \n"   +
                        "PRIMER ZA TESTIRANJE:    -brojStranica: int \n" +
                        "Metode su u obliku   vidljivost + naziv metode + (bilo koji tipovi, samo da su odvojeni zapetom, npr: int,double,String) + povratni tip\n" +
                        "PRIMER ZA TESTIRANJE:    #brojSlovaUImenu (String,double): int  \n\n\n" +


                        "UPUTSTVO ZA KREIRANJE VEZE: \n" +
                        "Ukoliko je u pitanju veza NASLEDJIVANJA, REALIZACIJE, ZAVISNOSTI, mozete dodati vezu bez ikakvih pravila \n" +
                        "Ukoliko zelite da nacrtate vezu, a ona je tipa ASOCIJACIJE, AGREGACIJE, KOMPOZICIJE ----> morate slediti pravila:\n" +
                        "   a) Ne mozete ostaviti default hint text u text area, ni u jednom slucaju, morate barem kliknuti na oba\n" +
                        "   b) Opis kardinalnosti veze mora biti u obliku: \n" +
                        "           vidljivost + naziv instanca + kardinalnost,  npr -----> -tipKnjizevnosti 0..1\n"+
                        "   c) Mozete popuniti jedan textField, po pravilu UML, a drugi da ostane prazan, ali morate barem jednom kliknuti da bi se sklonio HINT text \n\n\n" +



                        "UPUTSTVO ZA KORISCENJE STATE-ova \n " +
                        "1. Select dodaje i izbacuje iz LISTe selektovanih interklasa/connection-a (nalazi se u diagramView), svaki put kada se mis u drag-u pomeri za mm, " +
                        "ponovo se proverava da li se neki nalazi element nalazi trenutno u selektu. \n" +
                        "Logika je napravljena tako da se boja interklase menja u belo kada je selektovana, i veza u narandzasto (preko setColor(), koji u modelu ima notify). \n " +
                        "Ako je deselektovan, boja mu se vraca u primarnu, isto preko observera i setColor(). \n\n"+

                        "2. Move - da bi mogao da se koristi move, tj da bi uspesno radio, neki elementi trebaju da budu prethodno SELEKTOVANI. \n" +
                        "Zatim, treba kliknuti misem na bilo koju selektovanu interclassu (veza ne moze da se pomera), i samo dragovati po ekranu.\n\n" +

                        "3. Add interclass - dodaje tip interclase po zelji korisnika, na diagramView. \n" +
                        "Ako je neki element bio prethodno selektovan, postace deselektovan preko metode u diagramView 'backToOriginalColor()'\n\n" +

                        "4. Add Connection - dodaje vezu izmedju 2 interclase. Dodaje u modelu polja OD-DO. Tako se i brise.  \n" +
                        "Da bi se uspesno iscrtala veza, mora da se prvo klikne na neku klasu, i prevlaci do sledece klase. \n" +
                        "Ako se ne otpusti klik misa na sledecu klasu, veza se nece nacrtati. \n\n" +

                        "5. Edit - da bi neka interklasa/connection mogla da se edituje, NE MORA da bude prethodno selektovana, dovoljno je samo kliknuti na nju. \n\n" +
                        "Nakon klika na dugme SAVE, sve promene koje su dodate, se cuvaju u MODELU. Ako se ista promenilo, pozvace se notify. \n\n" +

                        "6. Delete - da bi neka neka interklasa/connection bila obrisana, MORA prethodno da bude selektovana! \n" +
                        "Opcija je ovako implementirana zbog multi brisanja, kada brisemo vise elemenata odjednom. \n" +
                        "Da bi se uspesno obrisale sve selektovane stvari, potrebno je kliknuti na bilo koji selektovani element (interclass/connection) \n" +
                        "OBRATITI paznju na delete veze !!! \n" +
                        "Prilikom kreiranja veze, oko nje se stvara pravougaonik, zbog opcije da moze da se selektuje. \n" +
                        "Medjutim taj pravougaonik moze biti izuzetno male visine ako je veza bas HORIZONTALNA, pa se moze pomisliti da Delete ne radi, a zapravo je bas potrebno pogoditi dobro klik. \n" +
                        "Ukoliko zelis da testiras, mozes da odkomentarises kod, koji crta roze pravougaonik u CONNECTIONpAINTER. \n \n" +

                        "7. Zoom - da bi opcija zoom radila, mora se drzati klik, i pomerati tockic za zoom! \n" +
                        "Ako se ne drzi klik misem nego samo pomera tockic misem, nece raditi. \n\n" +

                        "8. Zoom to fit - potrebno je samo dodati neku klasu pre klika na ovo dugme, da bi moglo da uhvati njenu gornju i levu koordinatu. \n\n" +

                        "Dodavanje u STABLO: \n" +
                        "Dodavanje DIAGRAM ELEMENATA u stablo sa leve strane (TREEVIEW) se vrsi uspesno, ali je potrebno dvaput kliknuti na dijagram, da bi se expandovala povrsina\n");

        guideText.setWrapStyleWord(true);
        guideText.setLineWrap(true);
        guideText.setCaretPosition(0);
        guideText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(guideText);
        this.add(scrollPane);
        this.setVisible(true);
    }
}