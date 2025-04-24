package com.wepower.wepower.APIs;

import com.google.gson.*;
import com.wepower.wepower.Models.DatiPalestra.ModelPrenotazioni;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelAutenticazione;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Llama4_API {

    private static final String API_KEY = "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";
    private static final String ENDPOINT = "https://api.groq.com/openai/v1/chat/completions";
    private static final OkHttpClient client = new OkHttpClient();

    private static final List<Map<String, String>> chatHistory = new ArrayList<>();

    private static final String eserciziPalestra = String.valueOf(ModelAutenticazione.riempiEserciziDisponibiliPalestra());
    private static final String systemPrompt = """
            Il tuo nome è Powerino. Sei un assistente virtuale esperto di fitness e lavori all'interno di un'applicazione per la gestione di una palestra. Il tuo compito è aiutare gli utenti a scegliere esercizi per obiettivi specifici (massa muscolare, dimagrimento, forza, mobilità), spiegare come svolgerli correttamente e rispondere a domande sui parametri fisici registrati. 
            Parla in modo amichevole, motivazionale ma professionale. Non fornire consigli medici, non parlare di diete cliniche o integratori specifici. Se ti viene chiesto qualcosa al di fuori dell’ambito fitness, rispondi gentilmente che puoi solo aiutare con il mondo della palestra. 
            Gli esercizi attualmente disponibili sono: %s. Suggerisci agli utenti esercizi solo da questa lista.
            Se l'utente ti chiede di prenotare, rispondi normalmente e poi aggiungi alla fine un comando tecnico nel formato esatto: |||PRENOTA:YYYY-MM-DDTHH:MM (es. 2025-04-25T18:00). Non lasciare questo campo incompleto.
            """.formatted(eserciziPalestra);

    public static void sendMessage(String userMessage, TextArea chatArea) {
        chatHistory.add(Map.of("role", "user", "content", userMessage));

        JsonArray messages = new JsonArray();

        JsonObject system = new JsonObject();
        system.addProperty("role", "system");
        system.addProperty("content", systemPrompt);
        messages.add(system);

        for (Map<String, String> msg : chatHistory) {
            JsonObject m = new JsonObject();
            m.addProperty("role", msg.get("role"));
            m.addProperty("content", msg.get("content"));
            messages.add(m);
        }

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

            @Override
            public void onFailure(@NotNull Call call, IOException e) {
                Platform.runLater(() -> chatArea.appendText("Errore: " + e.getMessage() + "\n"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Platform.runLater(() -> chatArea.appendText("Powerino: "));
                try {
                    assert response.body() != null;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String jsonLine = line.substring(6).trim();
                                if (jsonLine.equals("[DONE]")) break;

                                JsonObject obj = JsonParser.parseString(jsonLine).getAsJsonObject();
                                JsonObject delta = obj
                                        .getAsJsonArray("choices").get(0)
                                        .getAsJsonObject()
                                        .getAsJsonObject("delta");

                                if (delta.has("content")) {
                                    String contentPiece = delta.get("content").getAsString();
                                    rispostaFinale.append(contentPiece);
                                }
                            }
                        }

                        if (rispostaFinale.toString().contains("|||PRENOTA:")) {
                            Pattern dataPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
                            Pattern oraPattern = Pattern.compile("\\d{2}:\\d{2}");

                            Matcher dataMatch = dataPattern.matcher(rispostaFinale.toString());
                            Matcher oraMatch = oraPattern.matcher(rispostaFinale.toString());

                            System.out.println(dataMatch.matches());
                            if (dataMatch.find() && oraMatch.find()) {
                                String data = dataMatch.group();
                                String ora = oraMatch.group();

                                if (prenotaAllenamento(DatiSessioneCliente.getIdUtente(), data, ora)) {
                                    Platform.runLater(() -> {
                                        chatArea.appendText("✅ Prenotazione registrata per " + data + " alle " + ora + "\n");
                                        Model.getInstance().getClientDashboardController().loadCalendario();
                                    });
                                }
                            } else {
                                formatoPrenotazioneNonValida = true;
                            }
                        } else {
                            String content = rispostaFinale.toString();
                            chatArea.appendText(content);
                        }

                        if (formatoPrenotazioneNonValida) {
                            chatArea.appendText("Per procedere la data deve rispettare il seguente formato YYYY-MM-DD e l'orario HH:MM (ad esempio 2025-04-24 alle 17:00). Controlla anche che gli orari rispettino quelli della palestra, li puoi trovare nella sezione 'Prenotazioni'.");
                            formatoPrenotazioneNonValida = false;
                        }
                        chatHistory.add(Map.of("role", "assistant", "content", rispostaFinale.toString()));
                        Platform.runLater(() -> chatArea.appendText("\n"));
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> chatArea.appendText("❌ Sembra ci sia stato qualche problema.\n"));;
                }
            }
        });
    }

    public static boolean prenotaAllenamento(int idUtente, String data, String orario) throws SQLException {
        if (ModelPrenotazioni.aggiuntiPrenotazioneSalaPesi(data, orario, idUtente)) {
            DatiSessioneCliente.aggiungiPrenotazione(new PrenotazioneSalaPesi(data, orario));
            return true;
        }
        return false;
    }
}
