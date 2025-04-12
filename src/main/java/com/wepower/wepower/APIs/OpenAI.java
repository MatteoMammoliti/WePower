package com.wepower.wepower.APIs;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;
import java.util.function.Consumer;

public class OpenAI {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";

    public static void chiediPowerinoStreaming(String domanda, Consumer<String> onNewToken) throws IOException {
        URL url = new URL(API_URL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "text/event-stream");
        conn.setRequestProperty("HTTP-Referer", "https://tuo-sito-o-app"); // opzionale
        conn.setDoOutput(true);

        String requestBody = """
        {
          "model": "mistralai/mistral-7b-instruct",
          "messages": [
            { "role": "system", "content": "Sei un assistente virtuale in una palestra." },
            { "role": "user", "content": "%s" }
          ],
          "stream": true
        }
        """.formatted(domanda);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes("utf-8"));
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("data: ")) {
                        String data = line.substring("data: ".length());

                        if (data.equals("[DONE]")) break;

                        try {
                            // Estrai il contenuto del token generato
                            org.json.JSONObject json = new org.json.JSONObject(data);
                            String token = json.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("delta")
                                    .optString("content", "");

                            if (!token.isEmpty()) {
                                onNewToken.accept(token); // callback per aggiornare la UI
                            }
                        } catch (Exception e) {
                            System.err.println("Errore parsing: " + e.getMessage());
                        }
                    }
                }
            }
        } else {
            throw new IOException("Errore nella richiesta: " + responseCode);
        }
    }
}
