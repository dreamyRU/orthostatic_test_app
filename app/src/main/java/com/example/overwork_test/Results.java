package com.example.overwork_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import org.jsoup.nodes.Document;
import java.io.IOException;

import org.jsoup.Jsoup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Results extends AppCompatActivity {

    TextView ResultDescription;
    TextView MetricsDescription;
    ImageView Metrics_1;
    ImageView Metrics_2;
    String[] credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ResultDescription = findViewById(R.id.result_description);
        MetricsDescription = findViewById(R.id.metrics_description);
        Metrics_1 = findViewById(R.id.metrics_iv);
        Metrics_2 = findViewById(R.id.metrics_smile_iv);
        Intent intent = getIntent();

        credentials = intent.getStringArrayExtra("credentials");
        String first_value = intent.getStringExtra("first_value");
        String second_value = intent.getStringExtra("second_value");

        // Формирование запроса
        String data = String.format("day=15&month=12&year=1990&sex=1&m1=%s&m2=%s", first_value, second_value);

        try {
            overwork_api(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void overwork_api(String data) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("http://abashin.ru/cgi-bin/ru/tests/burnout")
                .method("POST", body)
                .addHeader("Host", "abashin.ru")
                .addHeader("Connection", "close")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("DNT", "1")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;" +
                        "q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Accept-Encoding", "deflate")
                .addHeader("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Content-Length", "43")
                .build();
        // Поток отправки запроса и обработки ответа (обновляет tv при получении ответа 200)
        // Создаем callback (метод обратного вызова) каждый раз, когда отправляем форму через ВВОД
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String message = response.body().string();
                    // Парсинг html body из ответа
                    Document html_response = Jsoup.parse(message);
                    String format_message = html_response.text();
                    // Обновление интерфейса при получении ответа
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { //https://gnicpm.ru/articles/zdorovyj-obraz-zhizni/oczenka-fizicheskoj-trenirovannosti-metody-samokontrolya.html

                            if (Integer.parseInt(MainActivity.Van) < 40 || Integer.parseInt(MainActivity.Van) > 90 || Integer.parseInt(MainActivity.Billy) > 140 || Integer.parseInt(MainActivity.Billy) < Integer.parseInt(MainActivity.Van)) {
                                MetricsDescription.setText("Похоже, у Вас проблемы со здоровьем, или Вы ввели неверные данные");
                                resultWorst();
                            } else if (Integer.parseInt(MainActivity.Billy) < 100) {
                                MetricsDescription.setText("Отличный результат");
                                resultAwesome();
                            } else if (Integer.parseInt(MainActivity.Billy) < 120) {
                                MetricsDescription.setText("Хороший результат");
                                resultGood();
                            } else if (Integer.parseInt(MainActivity.Billy) <= 140) {
                                MetricsDescription.setText("Удовлетворительно");
                                resultBad();
                            }
                        }
                    });;
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void resultAwesome() {
        ResultDescription.setText("Не забывайте о регулярном профессиональном медицинском осмотре!");
        Metrics_1.setImageResource(R.drawable.percent_100);
        Metrics_2.setImageResource(R.drawable.awesome_condition);

    }

    public void resultGood() {
        ResultDescription.setText("Не забывайте о регулярном профессиональном медицинском осмотре и регулярных тренировках!");
        Metrics_1.setImageResource(R.drawable.percent_60);
        Metrics_2.setImageResource(R.drawable.good_condition);
    }

    public void resultBad() {
        ResultDescription.setText("Вам нужно больше занимаься спортом и/или обратиться к специалисту!");
        Metrics_1.setImageResource(R.drawable.percent_40);
        Metrics_2.setImageResource(R.drawable.bad_condition);
    }

    public void resultWorst() {
        ResultDescription.setText("Незамедлительено обратитесь к врачу или проверьте корректность ввода!");
        Metrics_1.setImageResource(R.drawable.percent_0);
        Metrics_2.setImageResource(R.drawable.worst_condition);
    }
}
