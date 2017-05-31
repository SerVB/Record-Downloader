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

import java.util.ArrayList;

/**
 * Структура Жанр.
 */
class Genre {

    /**
     * Оригинальное имя.
     */
    public String originalName;

    /**
     * Отображаемое имя.
     */
    public String name;

    /**
     * Дата изменения.
     */
    public String date;

    /**
     * Прямая ссылка на жанр.
     */
    public String fullPath;

    /**
     * Хранилище треков жанра.
     */
    public ArrayList<Track> alTracks;

    /**
     * Хранилище HTML-кода страницы.
     */
    public String code;

    /**
     * Контсруктор.
     * @param originalName Оригинальное имя.
     * @param date Дата изменения.
     * @param fullPath Прямая ссылка на жанр.
     */
    public Genre(String originalName, String date, String fullPath) {
        this.originalName = originalName;
        this.date = date;
        this.fullPath = fullPath;

        this.name = sGetName(this.originalName);

        this.alTracks = new ArrayList();
    }

    private String sGetName(String origName) {
        return origName;
    }
}