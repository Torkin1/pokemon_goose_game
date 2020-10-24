# Pokemon Goose Game Requirements

Questo foglio di requisiti ha la priorità rispetto a requirements_GooseGame, qualora sorgessero conflitti.


### Personaggi giocabili
1. Ogni giocatore può scegliere un pokemon come pedina di gioco;
2. Alla scelta, saranno mostrate informazioni dettagliate reperite dal database remoto [pokeapi](pokeapi.co);
	1. La lista dei pokemon selezionabili sarà reperita da pokeapi;
	2. Ogni pedina ha un **tipo**, determinato dal tipo del pokemon che rappresenta così come riporta pokeapi.

### Caselle

1. Una casella può avere un **effetto**, ovvero un'interazione con i giocatori in uno o più momenti del gioco;
2. Ogni casella è colorata in base al tipo (ad esempio acqua, fuoco, ...);
3. i tipi saranno reperiti da pokeapi;
4. Il tipo della casella può determinare delle interazioni con il giocatore che la occupa in base al tipo della sua pedina, oltre agli effetti previsti dalla casella. Queste interazioni sono qui descritte:
	1. Se il giocatore deve pagare una posta nel momento in cui si trova in una casella il cui tipo rientra nei tipi elencati negli attributi `half_damage_from`, `no_damage_from`, `double_damage_from` del pokemon che sta giocando, tale posta è rispettivamente dimezzata, annullata o raddoppiata.
	2. Se il giocatore deve "guadagnare" una posta nel momento in cui si trova in una casella il cui tipo rientra nei tipi elencati negli attributi `half_damage_to`, `no_damage_to`, `double_damage_to` del pokemon che sta giocando, tale posta è rispettivamente dimezzata, annullata o raddoppiata.
1. Le caselle **blu** hanno un effetto che è da considerarsi molto influente all'esito della partita, in maniera positiva o negativa o entrambi.
	1. Sono caselle **identificate**, ovvero sono distinte da tutte le altre per il loro nome e sono sempre le stesse tra una partita e l'altra.
1. Le caselle **gialle** hanno un effetto che è da considerarsi leggermente influente all'esito della partita, in maniera positiva o negativa o entrambi.+
	
### Posta 

1. La **posta** è un ammontare da sottrarre o aggiungere al giocatore che va a toccare una o più delle seguenti fonti, personali per ogni giocatore:
	1. **Monete**:
		1. Ogni giocatore inizia la partita con una **quantità iniziale** di monete pari a zero;
		1. Il giocatore che ne possiede di più a fine partita è proclamato vincitore.
	1. **HPs**:
		1. Ogni giocatore inizia la partita con una quantità iniziale di HPs pari a 100;
		1. Se un giocatore esaurisce i suoi HPs, il giocatore deve trasferire tutto il suo denaro sul **piatto** ed **esce dal gioco**. <div id=Posta.1.2.2>
2. Quando viene richiesto al giocatore di **pagare** una quantità, verrà trasferito dal giocatore una quantità di monete pari alla quantità specificata dalla sua quantità personale nel piatto.
3. Quando viene richiesto al giocatore di **perdere** una quantità, verrà sottratto dal giocatore una quantità di HPs o monete pari alla quantità specificata dalla sua quantità personale.
4. Quando viene richiesto al giocatore di **guadagnare** una quantità, verrà aggiunto al giocatore una quantità di HPs o monete pari alla quantità specificata alla sua quantità personale.
5. Quando viene richiesto al giocatore di **recuperare** una quantità, verrà aggiunto al giocatore una quantità di HPs pari alla quantità specificata alla sua quantità personale, senza eccedere la quantità iniziale .
		
### Fine della partita

1. La partita finisce quando tutti i giocatori che hanno iniziato la partita sono usciti dal gioco.
	1. L'uscita dal gioco di un giocatore avviene a seguito di uno dei seguenti eventi:
		1. [Posta.1.2.2](#Posta.1.2.2)
		2. Il giocatore entra nella **casella finale**.
			1. A seguito dell'entrata di un giocatore nella casella finale, se nessun altro giocatore è entrato nella casella finale prima di lui, a partire dal turno del giocatore successivo tutti i giocatori devono tirare un dado prima di svolgere il loro turno. Se il punteggio del dado è 1 o 2 o 3, determinata soglia, il giocatore che ha tirato perde rispettivamente il 10%, 15%, 20% della sua max vita.
	1. Quando un giocatore esce dal gioco perchè entrato nella casella finale, viene calcolato il suo **punteggio** sommando i seguenti parametri:
		1. Ammontare di HPs rimanenti;
		2. Ammontare di monete del piatto;
		3. Ammontare delle sue monete.
	2. Quando un giocatore esce dal gioco per un motivo che non sia entrare nella casella finale, Il suo punteggio è considerato pari a zero.
2. A seguito del termine della partita, il giocatore che detiene il punteggio maggiore **vince la partita**.

### Lotta

1. Dopo che un giocatore è entrato in una casella dove sono presenti uno o più altri giocatori, tale giocatore può **innescare una lotta** con uno degli altri giocatori a scelta presenti nella stessa casella;
2. È scelta una quantità di monete da mettere in **palio** tra i due lottatori, pari al 20% delle monete di chi perderà la lotta.
2. I lottatori tirano il dado e sommano al suo punteggio i **modificatori di lotta**, calcolando così il **punteggio di lotta**;
3. Il giocatore con il punteggio di lotta più alto **vince la lotta**;
4. Chi vince la lotta:
	1. Guadagna il palio;
5. Chi perde la lotta;
	1. Perde il palio;
	2. Perde HPs
	
### Tabellone

1. Saranno presenti 63 caselle totali su tabellone
	1. Ogni 9 caselle 1 di esse sarà **gialla**, partendo dalle caselle 9 e 5
	2. Le caselle il cui indice ricade nella seguente lista saranno considerate **blu**: [6, 19, 31, 42, 52, 58]