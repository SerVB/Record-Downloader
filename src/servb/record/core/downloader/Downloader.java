/*
 * MIT License
 *
 * Copyright (c) 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package servb.record.core.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import static servb.record.uijava.downloader.RecordDownloader.alGenres;
import static servb.record.uijava.downloader.RecordDownloader.START_TIME;
import static servb.record.uijava.downloader.RecordDownloader.BUFFER_SIZE;
import static servb.record.uijava.downloader.RecordDownloader.ui;
import static servb.record.uijava.downloader.RecordDownloader.vSetProgress;

/**
 * Класс загрузчика.
 */
public class Downloader implements Runnable {

    @Override
    public void run() {
        vDownloadFullList(ui.jComboBoxGenres.getSelectedIndex());
    }

    /**
     * Загружает все треки жанра на диск.
     *
     * @param genreId Номер жанра для загрузки.
     */
    public static void vDownloadFullList(final int genreId) {
        final String saveFolder = ui.jTextFieldSavePath.getText() + START_TIME + alGenres.get(genreId).getName() + File.separator;
        File fPathToSave = new File(saveFolder);
        fPathToSave.mkdirs();

        ui.jLabelStatus.setText("Загрузка треков...");
        vSetProgress(0, alGenres.get(genreId).getTracks().size());
        for(int i = 0; i < alGenres.get(genreId).getTracks().size(); i++) {
            try {
                String decodedName = java.net.URLDecoder.decode(alGenres.get(genreId).getTracks().get(i).getOriginalName(), "UTF-8");

                vDownloadFile(
                        alGenres.get(genreId).getTracks().get(i).getFullPath(),
                        saveFolder + decodedName,
                        BUFFER_SIZE,
                        0
                );
                // TODO: Эта строчка выводит сообщение об успешности
                //       загрузки файла в любом случае. Исправить!
                System.out.println("Done loading \"" + alGenres.get(genreId).getTracks().get(i).getFullPath() + "\".");
                ui.jLabelStatus.setText("Загрузка треков... Загружен \"" + decodedName + "\".");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }

            vSetProgress(i + 1, alGenres.get(genreId).getTracks().size());
        }

        ui.jLabelStatus.setText("Загрузка треков... Готово.");
    }

    /**
     * Загружает файл на диск.
     *
     * @param strURL    Прямая ссылка к загружаемому файлу.
     * @param strPath   Путь к загружаемому файлу на диске.
     * @param buffSize  Размер буфера (любой).
     * @param count     Количество неудачных попыток загрузки.
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    static void vDownloadFile(String strURL, String strPath, int buffSize, int count) throws MalformedURLException, ProtocolException {
        final int MAX_TRIES = 5;

        if (count < MAX_TRIES) {
            URL connection = new URL(strURL);
            HttpURLConnection urlconn;
            InputStream in = null;
            OutputStream writer = null;

            try {
                urlconn = (HttpURLConnection) connection.openConnection();
                urlconn.setRequestMethod("GET");
                urlconn.connect();
                in = urlconn.getInputStream();
                writer = new FileOutputStream(strPath);
                byte buffer[] = new byte[buffSize];
                int c = in.read(buffer);
                while (c > 0) {
                    writer.write(buffer, 0, c);
                    c = in.read(buffer);
                }
            } catch (IOException ex) {
                String msg = String.format(
                        "Ошибка при загрузке! Попробую загрузить файл заново еще несколько раз (%d).",
                        MAX_TRIES - 1 - count
                );
                ui.jLabelStatus.setText(msg);
                // Загрузить то же самое заново, увеличив счетчик на 1:
                vDownloadFile(strURL, strPath, buffSize, count + 1);
            } finally {
                if (writer != null)
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Downloader.class.getName()).log(
                                Level.WARNING,
                                "Не могу закрыть writer!"
                        );
                    }

                if (in != null)
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Downloader.class.getName()).log(
                                Level.WARNING,
                                "Не могу закрыть in!"
                        );
                    }
            }
        } else {
            String msg = String.format(
                    "Загрузка файла не удалась несколько раз (%d), перехожу к следующему.",
                    MAX_TRIES
            );
            ui.jLabelStatus.setText(msg);
            Logger.getLogger(Downloader.class.getName()).log(
                    Level.WARNING,
                    "Загрузка файла не удалась несколько раз ({0}), перехожу к следующему.",
                    MAX_TRIES
            );
        }
    }
}
