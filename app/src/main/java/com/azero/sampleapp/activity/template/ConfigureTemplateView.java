/*
 * Copyright (c) 2019 SoundAI. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.azero.sampleapp.activity.template;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azero.sampleapp.R;
import com.azero.sampleapp.activity.playerinfo.playerinfo.PlayerInfoActivity;
import com.azero.sampleapp.activity.template.BodyTemplate1Activity;
import com.azero.sampleapp.activity.template.BodyTemplate2Activity;
import com.azero.sampleapp.activity.template.ListTemplate1Activity;
import com.azero.sampleapp.activity.template.QrCodeActivity;
import com.azero.sampleapp.activity.template.WeatherActivity;
import com.azero.sampleapp.util.DownloadImageTask;
import com.azero.sampleapp.util.QRCUtils;
import com.azero.sdk.util.log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 配置界面内容
 */
public class ConfigureTemplateView {
    private static int sNumForecastItems = 5; // For weather template card
    private static int sNumListItems = 5; // For list template card
    private static int sNumLocalSearchListItems = 4; // For local search template card

    public static void configureBodyTemplate1(BodyTemplate1Activity ba, JSONObject template) {
        try {
            if (template.has("title")) {
                JSONObject title = template.getJSONObject("title");
                if (title.has("mainTitle")) {
                    String mainTitle = title.getString("mainTitle");
                    ba.getMainTitle().setText(mainTitle);
                }

                if (title.has("subTitle")) {
                    String subTitle = title.getString("subTitle");
                    ba.getSubTitle().setText(subTitle);
                }
            }

            if (template.has("textField")) {
                String textField = template.getString("textField");
                ba.getTextField().setText(textField);
                ba.getTextField().post(() -> ba.getTextField().startAnimation());
                ba.getTextField().setMovementMethod(ScrollingMovementMethod.getInstance());
            }

            if (template.has("backgroundImage")) {
                JSONObject backgroundImage = template.getJSONObject("backgroundImage");
                if (backgroundImage.has("sources")) {
                    JSONArray sources = backgroundImage.getJSONArray("sources");
                    JSONObject source = (JSONObject) sources.get(0);
                    if (source.has("url")) {
                        String url = source.getString("url");
                        setBackground(url, ba.getBackground());
                    }
                }
            }
        } catch (JSONException e) {
            log.e(e.getMessage());
        }
    }

    public static void configureBodyTemplate2(BodyTemplate2Activity ba, JSONObject template) {
        try {
            if (template.has("title")) {
                JSONObject title = template.getJSONObject("title");
                if (title.has("mainTitle")) {
                    String mainTitle = title.getString("mainTitle");
                    ba.getMainTitle().setText(mainTitle);
                }

                if (title.has("subTitle")) {
                    String subTitle = title.getString("subTitle");
                    ba.getSubTitle().setText(subTitle);
                }
            }

            if (template.has("textField")) {
                String textField = template.getString("textField");
                ba.getTextField().setText(textField);
                ba.getTextField().setMovementMethod(ScrollingMovementMethod.getInstance());
            }

            if (template.has("image")) {
                JSONObject image = template.getJSONObject("image");
                String imageURL = getImageUrl(image);
                new DownloadImageTask(ba.getImage()).execute(imageURL);
            }
            if (template.has("backgroundImage")) {
                JSONObject backgroundImage = template.getJSONObject("backgroundImage");
                if (backgroundImage.has("sources")) {
                    JSONArray sources = backgroundImage.getJSONArray("sources");
                    JSONObject source = (JSONObject) sources.get(0);
                    if (source.has("url")) {
                        String url = source.getString("url");
                        setBackground(url, ba.getBackground());
                    }
                }
            }

        } catch (JSONException e) {
            log.e(e.getMessage());
        }
    }

    public static void configureListTemplate1(ListTemplate1Activity la, JSONObject template) {
        try {
            if (template.has("title")) {
                JSONObject title = template.getJSONObject("title");
                if (title.has("mainTitle")) {
                    String mainTitle = title.getString("mainTitle");
                    la.getMainTitle().setText(mainTitle);
                }

                if (title.has("subTitle")) {
                    String subTitle = title.getString("subTitle");
                    la.getSubTitle().setText(subTitle);
                }
            }

            if (template.has("listItems")) {
                JSONArray listItems = template.getJSONArray("listItems");

                // Truncate list
                int numItems = listItems.length() > sNumListItems
                        ? sNumListItems : listItems.length();

                la.clearLists();
                for (int j = 0; j < numItems; j++) {
                    JSONObject nextItem = listItems.getJSONObject(j);
                    String index = nextItem.has("leftTextField")
                            ? nextItem.getString("leftTextField") : "";
                    String content = nextItem.has("rightTextField")
                            ? nextItem.getString("rightTextField") : "";
                    la.insertListItem(index, content);
                }
            }
        } catch (JSONException e) {
            log.e(e.getMessage());
        }
    }

    public static void configureWeatherTemplate(WeatherActivity wa, JSONObject template) {
        try {
            if (template.has("title")) {
                JSONObject title = template.getJSONObject("title");
                if (title.has("mainTitle")) {
                    String mainTitle = title.getString("mainTitle");
                    wa.getMainTitle().setText(mainTitle);
                }

                if (title.has("subTitle")) {
                    String subTitle = title.getString("subTitle");
                    wa.getSubTitle().setText(subTitle);
                }
            }

            if (template.has("currentWeather")) {
                String currentWeather = template.getString("currentWeather");
                wa.getCurrentWeather().setText(currentWeather);
            }

            if (template.has("currentWeatherIcon")) {
                String currentWeatherIconURL =
                        getImageUrl(template.getJSONObject("currentWeatherIcon"));
                new DownloadImageTask(wa.getCurrentWeatherIcon()).execute(currentWeatherIconURL);
            }

            if (template.has("highTemperature")) {
                String highTempValue =
                        template.getJSONObject("highTemperature").getString("value");
                wa.getHighTemp().setText(highTempValue);
            }

            if (template.has("lowTemperature")) {
                String lowTempValue =
                        template.getJSONObject("lowTemperature").getString("value");
                wa.getLowTemp().setText(lowTempValue);
            }

            if (template.has("weatherForecast")) {
                JSONArray forecasts = template.getJSONArray("weatherForecast");
                for (int j = 0; j < sNumForecastItems; j++) {
                    // Get forecast
                    JSONObject next = forecasts.getJSONObject(j);
                    View forecastView = wa.getForecasts(j);

                    // Set icon
                    JSONObject image = next.getJSONObject("image");
                    String url = getImageUrl(image);
                    new DownloadImageTask(
                            (ImageView) forecastView.findViewById(R.id.forecastIcon)
                    ).execute(url);

                    // Set day
                    String day = next.has("day") ? next.getString("day") : "";
                    ((TextView) forecastView.findViewById(R.id.day)).setText(day);

                    // Set high temp
                    String high = next.has("highTemperature")
                            ? next.getString("highTemperature") : "";
                    ((TextView) forecastView.findViewById(R.id.highTemp)).setText(high);

                    // Set low temp
                    String low = next.has("lowTemperature")
                            ? next.getString("lowTemperature") : "";
                    ((TextView) forecastView.findViewById(R.id.lowTemp)).setText(low);
                }
            }
        } catch (JSONException e) {
            log.e(e.getMessage());
        }
    }

    private static String getImageUrl(JSONObject image) {
        String url = null;

        try {
            JSONArray sources = image.getJSONArray("sources");
            HashMap<String, String> imageMap = new HashMap<>();

            for (int j = 0; j < sources.length(); j++) {
                JSONObject next = sources.getJSONObject(j);
                String size;
                if (next.has("size")) {
                    size = next.getString("size").toUpperCase();
                } else {
                    size = "DEFAULT";
                }
                imageMap.put(size, next.getString("url"));
            }

            if (imageMap.containsKey("DEFAULT")) {
                url = imageMap.get("DEFAULT");
            } else if (imageMap.containsKey("X-LARGE")) {
                url = imageMap.get("X-LARGE");
            } else if (imageMap.containsKey("LARGE")) {
                url = imageMap.get("LARGE");
            } else if (imageMap.containsKey("MEDIUM")) {
                url = imageMap.get("MEDIUM");
            } else if (imageMap.containsKey("SMALL")) {
                url = imageMap.get("SMALL");
            } else if (imageMap.containsKey("X-SMALL")) {
                url = imageMap.get("X-SMALL");
            }
        } catch (JSONException e) {
            log.e(e.getMessage());
        }
        return url;
    }

    private static void setBackground(String url, ViewGroup background) {
        RequestOptions options = new RequestOptions().override(1920, 1080).centerCrop();
        Glide
                .with(background)
                .load(url)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        background.setBackground(resource);
                    }
                });
    }

    public static void configurePlayerInfo(PlayerInfoActivity pa, JSONObject template) {
        try {
            String header = template.has("header")
                    ? template.getString("header") : "";
            pa.getHeader().setText(header);

            String headerSubtext1 = "";
            if (template.has("headerSubtext1")) {
                headerSubtext1 = template.getString("headerSubtext1");
            } else if (template.has("provider")) {
                // Set header subtext to provider name if no header subtext given
                JSONObject provider = template.getJSONObject("provider");
                if (provider.has("name")) {
                    headerSubtext1 = provider.getString("name");
                }
            }
            pa.getHeaderSubtext1().setText(headerSubtext1);

            String title = template.has("title")
                    ? template.getString("title") : "";
            pa.getmTitle().setText(title);

            String titleSubtext1 = template.has("titleSubtext1")
                    ? template.getString("titleSubtext1") : "";
            pa.getTitleSubtext1().setText(titleSubtext1);

            String titleSubtext2 = template.has("titleSubtext2")
                    ? template.getString("titleSubtext2") : "";
            pa.getTitleSubtext2().setText(titleSubtext2);

            if (template.has("art")) {
                JSONObject art = template.getJSONObject("art");
                String url = getImageUrl(art);
                new DownloadImageTask(pa.getArt()).execute(url);
            } else {
                pa.getArt().setImageDrawable(null);
            }

            if (template.has("provider")) {
                JSONObject provider = template.getJSONObject("provider");
                if (provider.has("logo")) {
                    JSONObject logo = provider.getJSONObject("logo");
                    String url = getImageUrl(logo);
                    new DownloadImageTask(pa.getPartnerLogo()).execute(url);
                } else {
                    pa.getPartnerLogo().setImageDrawable(null);
                }
            }
        } catch (JSONException e) {
            log.e(e.getMessage());
        }
    }

    public static void configureAndLink(QrCodeActivity qca, JSONObject json) {
        try {
            if (json.has("type")) {
                String type = json.getString("type");
                String msg = json.getString("message");

                Bitmap bitmap = QRCUtils.createQrCodeBitmap(msg, 800, 800,
                        "UTF-8", "L", "2", Color.BLACK,
                        Color.WHITE);
                switch (type) {
                    case "andLink_qrCode":
                        qca.getQrCodeTitle().setText(qca.getResources().getString(R.string.qr_code_title));
                        qca.getQrCodeText1().setText(String.format(qca.getResources().getString(R.string.qr_code_paragraph_1), "和家亲"));
                        qca.getQrCodeText2().setText(String.format(qca.getResources().getString(R.string.qr_code_paragraph_2), "和家亲"));
                        qca.getQrCodeText3().setText(String.format(qca.getResources().getString(R.string.qr_code_paragraph_3), "和家亲"));
                        qca.getQrCodeText4().setText(String.format(qca.getResources().getString(R.string.qr_code_paragraph_4), "和家亲"));
                        qca.getQrCodeImage().setImageBitmap(bitmap);
                        break;
                    case "phoneCall_qrCode":
                        qca.getQrCodeTitle().setText("请绑定和家固话");
                        qca.getQrCodeImage().setImageBitmap(bitmap);
                        break;
                    case "finish":
                        qca.finish();
                        break;
                    case "wait":
                        qca.getQrCodeTitle().setText("正在连接...请稍后");
                        qca.getQrCodeImage().setVisibility(View.GONE);

                        qca.getQrCodeText1().setVisibility(View.GONE);
                        qca.getQrCodeText2().setVisibility(View.GONE);
                        qca.getQrCodeText3().setVisibility(View.GONE);
                        qca.getQrCodeText4().setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

        } catch (JSONException e) {
            log.e("QrCodeJson, configureAndLink: " + e);
        }
    }
}
