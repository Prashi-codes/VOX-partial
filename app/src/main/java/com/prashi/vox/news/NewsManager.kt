package com.prashi.vox.news

import org.json.JSONObject
import java.net.URL

class NewsManager {

    fun getKannadaNews(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        Thread {

            try {

                val apiKey =
                    "pub_4f5f477569164a57aaad97e75901697d"

                val url = URL(
                    "https://newsdata.io/api/1/news?apikey=$apiKey&language=kn&country=in&category=top&size=5"
                )

                val response = url.readText()

                val json = JSONObject(response)

                val results = json.getJSONArray("results")

                val newsBuilder = StringBuilder()

                val totalNews =
                    minOf(5, results.length())

                for (i in 0 until totalNews) {

                    val title = results
                        .getJSONObject(i)
                        .optString("title", "")

                    newsBuilder.append(
                        "ಸುದ್ದಿ ${i + 1}. $title. "
                    )
                }

                onSuccess(
                    "ಇಂದಿನ ಪ್ರಮುಖ ಸುದ್ದಿಗಳು. $newsBuilder"
                )

            } catch (e: Exception) {

                onError(e.toString())
            }

        }.start()
    }
}