package com.wepower.wepower.APIs;

import com.wepower.wepower.Models.ConnessioneDatabase;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class OpenRouter_AI {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";
    private static final List<Map<String, String>> chatHistory = new ArrayList<>();

    static {
        chatHistory.add(Map.of(
                "role", "system",
                "content", "Sono Powerino, un assistente virtuale in una palestra e sono presente nell'app per computer WePower. Le mie risposte devono essere brevi. " +
                        "Posso aiutarti a trovare informazioni sugli esercizi, le attrezzature e i programmi di allenamento. " +
                        "Non posso fornire consigli medici o nutrizionali. Se hai domande specifiche, chiedi pure!"
        ));
    }

    public static String getListaEserciziDisponibili() throws SQLException {

        String query = "SELECT NomeEsercizio FROM Esercizio";
        StringBuilder esercizi = new StringBuilder();

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement fetchEsercizi = conn.prepareStatement(query);
            ResultSet rs = fetchEsercizi.executeQuery();

            while (rs.next()) {
                String nomeEsercizio = rs.getString("NomeEsercizio");
                esercizi.append(nomeEsercizio).append(", ");
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero degli esercizi dal database.");
        }

        if (!esercizi.isEmpty()) {
            esercizi.setLength(esercizi.length() - 2);
        }

        return esercizi.toString();
    }

    public static void gestisciRichiestaAiutoApplicazione(String domanda) throws SQLException {

        if (domanda.toLowerCase().contains("aiuti") || domanda.toLowerCase().contains("applicazione") || domanda.toLowerCase().contains("funzionalità")) {

            String eserciziPalestra = getListaEserciziDisponibili();

            Map<String, String> funzionalitaApplicazione = new LinkedHashMap<>();
            funzionalitaApplicazione.put("allenamenti", "L'app WePower ti permette di monitorare i tuoi allenamenti. Nella sezione scheda potrai aggiungere un nuovo massimale per ciascun esercizio. Nella dashboard potrai vedere i grafici di andamento per ciascun esercizio.");
            funzionalitaApplicazione.put("scheda", "Nella sezione scheda puoi visualizzare la scheda che hai creato tu o che ti è stata assegnata. Se è presente vedrai l'elenco degli esercizi, altrimenti potrai crearne una nuova. Puoi anche modificare la scheda esistente.");
            funzionalitaApplicazione.put("profilo", "Nella sezione Profilo dell'app WePower puoi gestire le tue informazioni personali, come nome, email e password. Puoi anche caricare un nuovo certificato medico o abbonarti in palestra.");
            funzionalitaApplicazione.put("prenotazione", "Nella sezione prenotazioni dell'app WePower puoi prenotare le attrezzature e gli spazi della palestra. Puoi anche visualizzare le prenotazioni già effettuate e cancellarle se necessario.");
            funzionalitaApplicazione.put("esercizi", "Gli esercizi disponibili in palestra sono: " + eserciziPalestra);

            boolean rispostaTrovata = false;

            for (Map.Entry<String, String> entry : funzionalitaApplicazione.entrySet()) {
                if (domanda.toLowerCase().contains(entry.getKey())) {
                    chatHistory.add(Map.of(
                            "role", "assistant",
                            "content", entry.getValue()
                    ));
                    rispostaTrovata = true;
                    break;
                }
            }

            if (!rispostaTrovata) {
                chatHistory.add(Map.of(
                        "role", "assistant",
                        "content", "L'applicazione WePower consente di monitorare i tuoi allenamenti, visualizzare la scheda, gestire il profilo e prenotare attrezzature. " +
                                "Puoi anche chiedere informazioni sugli esercizi disponibili in palestra. Se hai domande specifiche, chiedi pure!"
                ));
            }
        }
    }

    public static void chiediPowerinoStreaming(String domanda, Consumer<String> onNewToken) throws IOException, SQLException {
        URL url = new URL(API_URL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "text/event-stream");
        conn.setDoOutput(true);

        gestisciRichiestaAiutoApplicazione(domanda);
        chatHistory.add(Map.of("role", "user", "content", domanda));

        JSONArray messagesArray = new JSONArray();
        for (Map<String, String> msg : chatHistory) {
            messagesArray.put(new JSONObject(msg));
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "mistralai/mistral-7b-instruct");
        requestBody.put("messages", messagesArray);
        requestBody.put("stream", true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder assistantReply = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("data: ")) {
                        String data = line.substring("data: ".length());
                        if (data.equals("[DONE]")) break;

                        try {
                            JSONObject json = new JSONObject(data);
                            String token = json.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("delta")
                                    .optString("content", "");

                            if (!token.isEmpty()) {
                                onNewToken.accept(token);
                                assistantReply.append(token);
                            }
                        } catch (Exception e) {
                            System.err.println("Errore nel parsing: " + e.getMessage());
                        }
                    }
                }
                chatHistory.add(Map.of("role", "assistant", "content", assistantReply.toString()));
            }
        } else {
            throw new IOException("Errore nella richiesta: " + responseCode);
        }
    }
}