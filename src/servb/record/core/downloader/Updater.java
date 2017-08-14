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

import servb.record.core.downloader.entry.TrackEntry;
import servb.record.core.downloader.entry.GenreEntry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import static servb.record.uijava.downloader.RecordDownloader.NEW_LINE;
import static servb.record.uijava.downloader.RecordDownloader.alGenres;
import static servb.record.uijava.downloader.RecordDownloader.ui;
import static servb.record.uijava.downloader.RecordDownloader.vSetProgress;

/**
 * Обновлятель списка жанров.
 */
public class Updater implements Runnable {

    @Override
    public void run() {

        vUpdateGenresNames();

    }

    /**
     * Возвращает код страницы.
     *
     * @param siteURL Прямая ссылка на страницу.
     * @return Код страницы.
     */
    private static String sGetCode(String siteURL) {

        URL url;
        try {

            url = new URL(siteURL);

        } catch (MalformedURLException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        BufferedReader br;
        try {

            br = new BufferedReader(new InputStreamReader(url.openStream()));

        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        String sScanned = "";

        try {

            String sCurrentLine;
            while ( (sCurrentLine = br.readLine()) != null ) {
                sScanned += sCurrentLine + NEW_LINE;
            }

        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        return sScanned;
    }

    /**
     * Обновляет названия жанров.
     */
    public static void vUpdateGenresNames() {
        ui.jComboBoxGenres.removeAllItems();
        alGenres.clear();

        vGetGenresNames();
        vGetGenresCode();
        vGetTracksList();

        ui.jButtonDownload.setEnabled(true);
    }

    /**
     * Получает список жанров и заполняет ComboBox.
     */
    private static void vGetGenresNames() {
        ui.jLabelStatus.setText("Получение списка жанров...");

        final String site = ui.jTextFieldRepoPath.getText();

        String arsHtmlMain = sGetCode(site); // Текст "главной" страницы

        int idy, idx = 0;
        String tmp;
        idx = arsHtmlMain.indexOf("href=", idx) + 1; // Пропуск первой ссылки
        while( (idx=arsHtmlMain.indexOf("href=",idx)) != -1 ){ // Заполнение жанров
            idx+=6;
            tmp = arsHtmlMain.substring(idx-1,idx); // " или '
            idy = arsHtmlMain.indexOf(tmp,idx);
            String link = arsHtmlMain.substring(idx, idy); // Полученная ссылка
            alGenres.add(new GenreEntry(link.substring(0, link.length() - 1), "", site + link));
            ui.jComboBoxGenres.addItem(alGenres.get(alGenres.size()-1).getName());
        }

        ui.jLabelStatus.setText("Получение списка жанров... Готово.");
    }

    /**
     * Получает HTML код каждого жанра.
     */
    private static void vGetGenresCode() {
        ui.jLabelStatus.setText("Получение жанров...");

        vSetProgress(0, alGenres.size());

        for(int i = 0; i < alGenres.size(); i++) {
            alGenres.get(i).code = sGetCode(alGenres.get(i).getFullPath());
            ui.jLabelStatus.setText("Получение жанров... Получен жанр \"" + alGenres.get(i).getName() + "\".");

            vSetProgress(i + 1, alGenres.size());
        }

        ui.jLabelStatus.setText("Получение жанров... Готово.");
    }

    /**
     * Получение списка треков.
     */
    private static void vGetTracksList() {
        ui.jLabelStatus.setText("Получение списка треков...");
        vSetProgress(0, alGenres.size());

        for(int i = 0; i < alGenres.size(); i++) {
            int idx=alGenres.get(i).code.indexOf("href=")+1; // Пропуск первой ссылки

            while((idx=alGenres.get(i).code.indexOf("href=",idx))!=-1){ // Заполнение треков
                idx += 6;
                String tmp = alGenres.get(i).code.substring(idx-1,idx); // " или '
                int idy = alGenres.get(i).code.indexOf(tmp,idx);
                String link = alGenres.get(i).code.substring(idx, idy); // Полученная ссылка
                alGenres.get(i).getTracks().add(new TrackEntry(link, "", -1, alGenres.get(i).getFullPath() + link));
            }

            vSetProgress(i + 1, alGenres.size());
        }

        ui.jLabelStatus.setText("Получение списка треков... Готово.");
    }
}
