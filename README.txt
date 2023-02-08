# Tema POO  - GwentStone
Fotin Andrei-Stefan 336CA


Json Parse I/O

Am realizat acest aspect folosindu-ma de biblioteca jackson inclusa in
scheletul temei. Am gandit sa mapez json-inputul pe clasele de input
oferite in schelet si apoi sa mapez acele date pe ierarhia mea de clase
gandita pt tema. Am folosit copy constructori ca sa mapez practic
niste date din input in obiectele mele.
Pt partea de scriere am creat clase speciale pe care le-am populat
prin constructor iar apoi am parsat obiectul folosind clasele si metodele
generalizate pe care le-am gandit pt a parsa din obiect in json.
Astfel am rezolvat problema I/O a temei.


Ierarhia temei

M-am gandit la ierarhia temei cumva top - bottom
Mai intai a contat sa parsez inputul si sa populez obiectele mele cu ele.
Apoi a trebuit gandita arhitectura de game serie intre jucatori, care
poate sa contina mai multe games si apoi cum se va desfasura un game efectiv.
Game-ul dupa ce a fost instantiat si populat cu datele de intrare,
practic va depinde pana se sfarseste de comenzi,
astfel incat atentia se muta la commands si la descifrarea si aplicarea lor.


Cards

Ierarhia de cards a fost gandita cu Card clasa abstracta obliga mostenitorii
sa implementeze multe metode (de ex un minion o sa implementeze si o metoda
de environment si una de hero, doar ca lasam implementarea goala sau cu
mesaj de debug, pt ca nu ne intereseaza efectiv pt ca nu este o actiune
specifica acelui tip de card).

Cartile sunt impartite de la Card ceva general, la ceva din ce in ce mai nisat
si implementeaza commands de attack etc.

Comenzile specifice de game (de debug) sunt implementate de Game, folosim in general clase
ajutatoare, Statistics care reprezinta un singleton cu statisticile
tuturor jocurilor, care ofera aceste informatii.
Dar mai ales cea mai importanta clasa este cea GameManager care
ofera metode statice utile in implementarea actiunilor din joc.


Actiunile sunt interdependente si un pic cam usor de bugguit pt ca nu sunt
gandite bug-safe. Adica practic se sesizeaza orice abatere de la desfasurarea
jocului din ref, prin output gresit sau erori la runtime (exceptions).

Cam atat este de povestit, oricum se vad destule detalii si din cod si modul
de organizare, implementari si cumva respectarea de SOLID pattern,
folosirea principiilor OOP utile in tema aceasta.