# Goose Game Requirements

### Gioco dell’oca

- 1 solo livello
- multiplayer locale
- I giocatori hanno una posta

### Priorità (da tenere presente in caso di confltti tra regole)
- Effetto casella (più alta) 
- Regolamento del gioco (più bassa)

### Plancia
- Plancia di gioco con successione di caselle contigue
- Percorso non circolare, esiste casella iniziale e finale.

### Caselle
- Normali (nessun effetto)
- Effetto:
    1. positivo: 
        - raddoppi il punteggio del dado
    2. negativo:
        - paghi posta + un effetto negativo.
        
### Regole
- Scopo: entrare nella casella finale;
- Se il punteggio del dado eccede la quantità sufficiente a entrare nella casella finale, si torna indietro tanto quanto vale l’eccesso.
- I players giocano alternativamente 1 turno:

	- Risolve l’effetto di permanenza della casella in cui si trova;
    - Lancia il dado;
    - Muove la pedina dell’ammontare del punteggio del dado;
	- Risolve l’effetto di entrata della casella
	
### Dizionario
- **Entrata in una casella** : Una pedina in movimento entra in una casella se in corrispondenza di quella casella si esaurisce il punteggio del dado.
- **Uscita da una casella** : Si attiva immediatamente dopo che si esce da una casella.
- **Permanenza in una casella** : Si permane nella casella se ci si trova in essa all’inizio del proprio turno, prima di lanciare i dadi.
- **Effetto di permanenza** : Si attiva prima del tiro dei dadi. 
- **Effetto di entrata** : Si attiva immediatamente dopo l’entrata in una casella.
- **Effetto di uscita** : Si attiva immediatamente dopo l’uscita da una casella.
- **Posta** : Ogni giocatore ne ha un ammontare, che può essere speso durante la partita. Se un giocatore esaurisce la posta, perde la partita.
- **Movimento pedina** : L’uscita dalla casella in cui ci si trova a inizio turno, seguita dall’entrata nella casella distante tanto quanto il punteggio del dado, verso la casella finale.

### Lancio del Dado:
- Valori possibili \[1:6] 
