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
package servb.record.uijava.downloader;

import servb.record.core.downloader.entry.GenreEntry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InvalidAttributeValueException;
import servb.record.core.downloader.Updater;

/**
 *
 */
public class RecordDownloader {

    public final static String UI_PROGRAM_NAME = "Record Downloader";
    public final static String UI_PROGRESS = "Прогресс";

    /**
     * Путь к сайту Радио Рекорд.
     */
    public final static String RECORD_SITE = "http://78.140.251.40/tmp_audio/top100/";

    /**
     * Папка загрузок.
     */
    public final static String MUSIC_DIR = "D:/SerVB/Music/Record/";

    /**
     * Размер буфера.
     */
    public final static int BUFFER_SIZE = 8*1024*1024;

    /**
     * Символ переноса строки.
     */
    public final static String NEW_LINE = System.getProperty("line.separator");

    /**
     * Текущее время.
     */
    public final static String START_TIME = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());

    /**
     * Хранилище параметров жанров.
     */
    public static ArrayList<GenreEntry> alGenres = new ArrayList();

    /**
     * Фрейм.
     */
    public static UI ui;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initUi();
        vSetProgramName();
        vSetDefaultRepoPath();
        vSetSavePath(MUSIC_DIR);
        vSetProgress(-1, -1);
    }

    private static void initUi() {
        ui = new UI();

//        ui.setLookAndFeelSystem();
//        ui.setLookAndFeelNimbus();

        ui.setVisible(true);
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
     */
    public static void vSetProgress(int done, int toDo) {
        if (done < 0) {
            ui.jLabelProgress.setText(UI_PROGRESS + ":");
            ui.jProgressBarProgress.setValue( 0 );
        } else if (done <= toDo) {
            ui.jLabelProgress.setText(UI_PROGRESS + String.format(" (%d/%d):", done, toDo));
            ui.jProgressBarProgress.setValue((int)Math.round(done * 100.0 / toDo));
        } else {
            try {
                throw new InvalidAttributeValueException(String.format("done (==%d) > toDo (==%d)!", done, toDo));
            } catch (InvalidAttributeValueException ex) {
                Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    static void vShowNamesOfGenres() {
//        for(int i = 0; i < alGenres.size(); i++) {
//            System.out.println(i + " - " + alGenres.get(i).name);
//        }
//    }
}
