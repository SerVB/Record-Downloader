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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.management.InvalidAttributeValueException;

/**
 *
 */
public class RecordDownloader {

    private final static String UI_PROGRAM_NAME = "Record Downloader";
    private final static String UI_PROGRESS = "Прогресс";

    /**
     * Путь к сайту Радио Рекорд.
     */
    private final static String RECORD_SITE = "http://78.140.251.40/tmp_audio/top100/";

    /**
     * Папка загрузок.
     */
    private final static String MUSIC_DIR = "D:/SerVB/Music/Record/";

    /**
     * Размер буфера.
     */
    private final static int BUFFER_SIZE = 8*1024*1024;

    /**
     * Символ переноса строки.
     */
    private final static String NEW_LINE = System.getProperty("line.separator");

    /**
     * Текущее время.
     */
    private final static String START_TIME = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());

    /**
     * Хранилище параметров жанров.
     */
    private static ArrayList<Genre> alGenres = new ArrayList();

    /**
     * Фрейм.
     */
    private static UI ui;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws IOException, Exception {
        ui = new UI();
        ui.setVisible(true);

        vSetProgramName();
        vSetDefaultRepoPath();
        vSetSavePath(MUSIC_DIR);
        vSetProgress(-1, -1);
    }

    /**
     * Задает заголовок окна.
     */
    private static void vSetProgramName() {
        ui.setTitle(UI_PROGRAM_NAME + " " + START_TIME);
    }

    /**
     * Задает стандартный путь сохранения.
     */
    public static void vSetDefaultRepoPath() {
        ui.jTextFieldRepoPath.setText(RECORD_SITE);
    }

    /**
     * Задает путь сохранения.
     * @param path Путь сохранения.
     */
    public static void vSetSavePath(String path) {
        ui.jTextFieldSavePath.setText(path);
    }

    /**
     * Задает прогресс выполнения.
     * @param done Выполнено единиц.
     * @param toDo Необходимо выполнить единиц.
     * @throws javax.management.InvalidAttributeValueException
     */
    private static void vSetProgress(int done, int toDo) throws InvalidAttributeValueException {
        if (done < 0) {
            ui.jLabelProgress.setText(UI_PROGRESS + ":");
            ui.jProgressBarProgress.setValue( 0 );
        } else if (done <= toDo) {
            ui.jLabelProgress.setText(UI_PROGRESS + String.format(" (%d/%d):", done, toDo));
            ui.jProgressBarProgress.setValue((int)Math.round(done * 100.0 / toDo));
        } else {
            throw new InvalidAttributeValueException(String.format("done (==%d) > toDo (==%d)!", done, toDo));
        }
    }

    /**
     * Возвращает код страницы.
     * @param siteURL Прямая ссылка на страницу.
     * @return Код страницы.
     * @throws IOException
     */
    private static String sGetCode(String siteURL) throws IOException {
        String sScanned = "";
        URL url = new URL(siteURL);

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String sCurrentLine;
        while ( (sCurrentLine = br.readLine()) != null )
            sScanned += sCurrentLine + NEW_LINE;

        return sScanned;
    }

    /**
     * Обновляет названия жанров.
     * @throws IOException
     * @throws javax.management.InvalidAttributeValueException
     */
    public static void vUpdateGenresNames() throws IOException, InvalidAttributeValueException {
        ui.jComboBoxGenres.removeAllItems();

        final String site = ui.jTextFieldRepoPath.getText();

        String arsHtmlM = sGetCode(site); // Текст "главной" страницы

        ui.jLabelStatus.setText("Получение жанров...");
        int idy, idx = 0, numOfGenres = 0;
        String tmp;
        idx = arsHtmlM.indexOf("href=", idx) + 1; // Пропуск первой ссылки
        while( (idx=arsHtmlM.indexOf("href=",idx)) != -1 ){ // Заполнение жанров
            idx+=6;
            tmp = arsHtmlM.substring(idx-1,idx); // " или '
            idy = arsHtmlM.indexOf(tmp,idx);
            String link = arsHtmlM.substring(idx, idy); // Полученная ссылка
            alGenres.add(new Genre(link, "", site + link));
            numOfGenres++;
        }

        ui.jLabelStatus.setText("Получение списка треков...");
        vSetProgress(0, numOfGenres);
        for(int i = 0; i < numOfGenres; i++) {
            alGenres.get(i).code = sGetCode(alGenres.get(i).fullPath);
            vSetProgress(i+1, numOfGenres);

            ui.jLabelStatus.setText("Получение списка треков... Получен список " + alGenres.get(i).originalName + ".");
            System.out.println("Successful reception of the code: "  + alGenres.get(i).originalName);

            ui.jComboBoxGenres.addItem(alGenres.get(i).name);
        }

        ui.jLabelStatus.setText("Обработка треков...");
        vSetProgress(0, alGenres.size());
        for(int i = 0; i < alGenres.size(); i++) {
            idx=alGenres.get(i).code.indexOf("href=",idx)+1; // Пропуск первой ссылки
            while((idx=alGenres.get(i).code.indexOf("href=",idx))!=-1){ // Заполнение треков
                idx+=6;
                tmp = alGenres.get(i).code.substring(idx-1,idx); // " или '
                idy = alGenres.get(i).code.indexOf(tmp,idx);
                String link = alGenres.get(i).code.substring(idx, idy); // Полученная ссылка
                alGenres.get(i).alTracks.add( new Track(link, "", -1, alGenres.get(i).fullPath + link) );
            }

            vSetProgress(i + 1, alGenres.size());
        }
        ui.jLabelStatus.setText("Обработка треков... Готово.");
    }

    /**
     * Загружает все треки жанра на диск. НЕ РАБОТАЕТ ВЫВОД В ОКНО!
     * @param genreID Номер жанра для загрузки.
     * @throws ProtocolException
     * @throws IOException
     * @throws javax.management.InvalidAttributeValueException
     */
    public static void vDownloadFullList(final int genreID) throws ProtocolException, IOException, InvalidAttributeValueException {
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
            System.out.println("done loading " + decodedName);
            ui.jLabelStatus.setText("Загрузка треков... Загружен " + decodedName + ".");
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

//    static void vShowNamesOfGenres() {
//        for(int i = 0; i < alGenres.size(); i++) {
//            System.out.println(i + " - " + alGenres.get(i).name);
//        }
//    }
}
