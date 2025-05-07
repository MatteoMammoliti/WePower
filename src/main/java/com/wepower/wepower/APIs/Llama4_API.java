package com.wepower.wepower.APIs;

import com.google.gson.*;
import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.DatiPalestra.ModelPrenotazioni;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesiCliente;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelAutenticazione;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Llama4_API {

    private static final String API_KEY = "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";
    private static final String ENDPOINT = "https://api.groq.com/openai/v1/chat/completions";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS).build();

    private static final List<Map<String, String>> chatHistory = new ArrayList<>();

    private static final String eserciziPalestra = String.valueOf(ModelAutenticazione.riempiEserciziDisponibiliPalestra());

    // definisco l'identità e il comportamento del chatbot stabilendo cosa può e non può fare/dire
    private static final String systemPrompt = """
    Il tuo nome è Powerino. Sei un assistente virtuale esperto di fitness e lavori all'interno di un'applicazione per la gestione di una palestra. Il tuo compito è aiutare gli utenti a scegliere esercizi per obiettivi specifici (massa muscolare, dimagrimento, forza, mobilità), spiegare come svolgerli correttamente e rispondere a domande sui parametri fisici registrati. 
    Parla in modo amichevole, motivazionale ma professionale. Non fornire consigli medici, non parlare di diete cliniche o integratori specifici. Se ti viene chiesto qualcosa al di fuori dell’ambito fitness, rispondi gentilmente che puoi solo aiutare con il mondo della palestra. 
    Gli esercizi attualmente disponibili sono: %s. Suggerisci agli utenti esercizi solo da questa lista.

    Se l'utente ti chiede di prenotare, rispondi normalmente, MA aggiungi il comando tecnico alla fine **solo se l'utente ha specificato esplicitamente una data nel formato YYYY-MM-DD e un orario nel formato HH:MM**. Non interpretare espressioni come "oggi", "stasera", "domani": devono essere date e orari precisi. 
    Se sono presenti entrambi, aggiungi il comando alla fine nel formato: |||PRENOTA:YYYY-MM-DDTHH:MM (es. 2025-04-25T18:00). Altrimenti, **non aggiungere nulla**.
    
    Se l'utente ti chiede cosa può fare nell'applicazione, rispondi che WePower permette di prenotarsi alla sala pesi nella sezione 'Prenotati', di creare una scheda di allenamento o di richiederne una, di inserire i propri dati fisici o massimali degli esercizi, e molto altro.
    """.formatted(eserciziPalestra);

    public static void sendMessage(String userMessage, Consumer<String> risposta) {

        /*
        Una consumer string è una funzione personalizzabile che accetta un paramentro (in questo caso una string)
        che stabilisce come visualizzare la risposta all'utente in chat
         */

        // storico della chat: powerino si ricorda quello che ha risposto precedentemente o che gli è stato richiesto durante la singola sessione
        chatHistory.add(Map.of("role", "user", "content", userMessage));

        JsonArray messages = new JsonArray(); // array di json (racchiuso tra le [] )

        JsonObject system = new JsonObject(); // singolo json (racchiuso tra le {} )
        // stabiliamo la tipologia di json (messaggio di sistema comportamentale per il bot)6
        system.addProperty("role", "system");
        system.addProperty("content", systemPrompt);
        messages.add(system);

        // aggiunta di ogni messaggio nello storico
        for (Map<String, String> msg : chatHistory) {
            JsonObject m = new JsonObject();
            m.addProperty("role", msg.get("role"));
            m.addProperty("content", msg.get("content"));
            messages.add(m);
        }
        /* esempio dello storico
        [ -> json array
          {"role": "system", "content": "istruzioni chatbot"}, -> singolo json
          {"role": "user", "content": "Voglio fare massa muscolare"},
          {"role": "assistant", "content": "Ti consiglio il bench press"},
          {"role": "user", "content": "Prenota il 2025-05-01 alle 18:00"}
        ]
         */

        // json inviato all'API di Groq
        JsonObject payload = new JsonObject();
        payload.addProperty("model", "meta-llama/llama-4-scout-17b-16e-instruct");
        payload.addProperty("stream", true);
        payload.add("messages", messages);

        RequestBody body = RequestBody.create(
                payload.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            StringBuilder rispostaFinale = new StringBuilder();
            boolean formatoPrenotazioneNonValida = false;

            // se la connessione con l'API fallisce powerino risposte in chat con un messaggio di errore
            // la risposta viene visualizzata non appena possibile
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> risposta.accept("Errore: " + e.getMessage() + "\n"));
            }

            // se la connessione va a buon fine, si prova a parserizzare il json
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    assert response.body() != null;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
                        String line;

                        /* esempio di risposta fornita dal modello
                            data: {"choices":[{"delta":{"content":"Ciao "}}]}
                            data: {"choices":[{"delta":{"content":"come stai?"}}]}
                            data: [DONE] -> streaming finito
                         */

                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) { // consideriamo solo le righe che iniziano con data:
                                String jsonLine = line.substring(6).trim();
                                if (jsonLine.equals("[DONE]")) break; // ignoriamo il fine streaming

                                JsonObject obj = JsonParser.parseString(jsonLine).getAsJsonObject();
                                JsonObject delta = obj // prendiamo la porzione di json che contiene il messaggio
                                        .getAsJsonArray("choices").get(0)
                                        .getAsJsonObject()
                                        .getAsJsonObject("delta");

                                if (delta.has("content")) { // estraiamo dal delta il content ovvero la stringa pura
                                    String contentPiece = delta.get("content").getAsString();
                                    rispostaFinale.append(contentPiece);
                                }
                            }
                        }

                        // se la risposta estratta contiene il messaggio di sistema |||PRENOTA:, allora l'utente vuole una prenotazione
                        if (rispostaFinale.toString().contains("|||PRENOTA:")) {
                            Pattern dataPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
                            Pattern oraPattern = Pattern.compile("\\d{2}:\\d{2}");

                            Matcher dataMatch = dataPattern.matcher(rispostaFinale.toString());
                            Matcher oraMatch = oraPattern.matcher(rispostaFinale.toString());

                            if (dataMatch.find() && oraMatch.find()) {
                                String data = dataMatch.group();
                                LocalDate dataConvertita = LocalDate.parse(data);
                                String ora = oraMatch.group();
                                LocalTime orarioConvertito = LocalTime.parse(ora);

                                String[] temp = DatiSessionePalestra.getOrariPrenotazione();
                                boolean trovato = false;
                                for(String orario : temp){
                                    if (orario.equals(ora)) {
                                        trovato = true;
                                        break;
                                    }
                                }

                                if (!trovato || (dataConvertita.isEqual(LocalDate.now())) && orarioConvertito.isBefore(LocalTime.now())){
                                    Platform.runLater(() -> risposta.accept("Attenzione, l'orario non è valido per la nostra palestra o potrebbe essere passato. Controlla in 'Prenotazioni' gli orari disponibili." + "\n"));
                                }
                                else if (dataConvertita.isBefore(LocalDate.now()) || dataConvertita.getDayOfWeek() == DayOfWeek.SUNDAY){
                                    Platform.runLater(() -> risposta.accept("Attenzione, hai scelto una data non valida. Potrebbe essere passata oppure una domenica." + "\n"));
                                }
                                else if (prenotaAllenamento(DatiSessioneCliente.getIdUtente(), data, ora)) {
                                    Platform.runLater(() -> {
                                        risposta.accept("✅ Prenotazione registrata per " + data + " alle " + ora + "\n");
                                        Model.getInstance().getClientDashboardController().loadCalendario();
                                    });
                                }
                                else Platform.runLater(() ->
                                            risposta.accept("❌ Prenotazione fallita per " + data + " alle " + ora + "\n")
                                    );
                            } else {
                                formatoPrenotazioneNonValida = true;
                            }
                        } else {
                            risposta.accept(rispostaFinale.toString());
                        }

                        if (formatoPrenotazioneNonValida) {
                            risposta.accept("Per procedere la data deve rispettare il seguente formato YYYY-MM-DD e l'orario HH:MM (ad esempio 2025-04-24 alle 17:00). Controlla anche che gli orari rispettino quelli della palestra, li puoi trovare nella sezione 'Prenotazioni'.\n");
                            formatoPrenotazioneNonValida = false;
                        }
                        chatHistory.add(Map.of("role", "assistant", "content", rispostaFinale.toString()));
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> risposta.accept("❌ Sembra ci sia stato qualche problema.\n"));;
                }
            }
        });
    }

    public static boolean prenotaAllenamento(int idUtente, String data, String orario) throws SQLException {

        if(DatiSessionePalestra.getNumeroPrenotazioniDataOraResidue(new PrenotazioneSalaPesiCliente(idUtente, data, orario)) <= 0) return false;

        if (ModelPrenotazioni.aggiuntiPrenotazioneSalaPesi(data, orario, idUtente)) {
            DatiSessioneCliente.aggiungiPrenotazione(new PrenotazioneSalaPesi(data, orario));
            DatiSessionePalestra.aggiungiPrenotazioneSalaPesi(new PrenotazioneSalaPesiCliente(idUtente, data, orario));
            return true;
        }
        return false;
    }
}