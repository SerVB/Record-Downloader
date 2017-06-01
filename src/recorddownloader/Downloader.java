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

package recorddownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InvalidAttributeValueException;
import static recorddownloader.RecordDownloader.alGenres;
import static recorddownloader.RecordDownloader.START_TIME;
import static recorddownloader.RecordDownloader.BUFFER_SIZE;
import static recorddownloader.RecordDownloader.ui;
import static recorddownloader.RecordDownloader.vSetProgress;

/**
 *
 */
public class Downloader implements Runnable {

    @Override
    public void run() {
        try {
            vDownloadFullList(ui.jComboBoxGenres.getSelectedIndex());
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Загружает все треки жанра на диск. НЕ РАБОТАЕТ ВЫВОД В ОКНО!
     * @param genreID Номер жанра для загрузки.
     * @throws ProtocolException
     * @throws IOException
     */
    public static void vDownloadFullList(final int genreID) throws ProtocolException, IOException {
        final String saveFolder = ui.jTextFieldSavePath.getText() + START_TIME + alGenres.get(genreID).name;
        File fPathToSave = new File(saveFolder);
        fPathToSave.mkdirs();

        ui.jLabelStatus.setText("Загрузка треков...");
        vSetProgress(0, alGenres.get(genreID).alTracks.size());
        for(int i = 0; i < alGenres.get(genreID).alTracks.size(); i++) {
            String decodedName = java.net.URLDecoder.decode(alGenres.get(genreID).alTracks.get(i).originalName, "UTF-8");

            vDownloadFile(
                    alGenres.get(genreID).alTracks.get(i).fullPath,
                    saveFolder + decodedName,
                    BUFFER_SIZE
            );

            vSetProgress(i + 1, alGenres.get(genreID).alTracks.size());
            System.out.println("Done loading \"" + alGenres.get(genreID).alTracks.get(i).fullPath + "\".");
            ui.jLabelStatus.setText("Загрузка треков... Загружен \"" + decodedName + "\".");
        }
        ui.jLabelStatus.setText("Загрузка треков... Готово.");
    }

    /**
     * Загружает файл на диск.
     * @param strURL Прямая ссылка к загружаемому файлу.
     * @param strPath Путь к загружаемому файлу на диске.
     * @param buffSize Размер буфера (любой).
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    static void vDownloadFile(String strURL, String strPath, int buffSize) throws MalformedURLException, ProtocolException, IOException {
        URL connection = new URL(strURL);
        HttpURLConnection urlconn;
        urlconn = (HttpURLConnection) connection.openConnection();
        urlconn.setRequestMethod("GET");
        urlconn.connect();
        InputStream in = urlconn.getInputStream();
        OutputStream writer = new FileOutputStream(strPath);
        byte buffer[] = new byte[buffSize];
        int c = in.read(buffer);
        while (c > 0) {
            writer.write(buffer, 0, c);
            c = in.read(buffer);
        }
        writer.flush();
        writer.close();
        in.close();
    }
}
