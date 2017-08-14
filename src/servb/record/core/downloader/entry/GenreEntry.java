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
package servb.record.core.downloader.entry;

import java.util.ArrayList;
import servb.record.core.Genre;

/**
 * Структура "Жанр".
 */
public final class GenreEntry {

    /**
     * Жанр.
     */
    private final Genre genre;

    /**
     * Дата изменения.
     */
    private final String date;

    /**
     * Прямая ссылка на жанр.
     */
    private final String fullPath;

    /**
     * Хранилище треков жанра.
     */
    private final ArrayList<TrackEntry> tracks;

    /**
     * HTML-код страницы.
     */
    public String code;

    public final String originalName;

    /**
     * Конструирует жанр.
     *
     * @param originalName  Оригинальное имя.
     * @param date          Дата изменения.
     * @param fullPath      Прямая ссылка на жанр.
     */
    public GenreEntry(final String originalName, final String date, final String fullPath) {
        this.originalName = originalName;
        this.genre = Genre.getByShortName(this.originalName);
        this.date = date;
        this.fullPath = fullPath;

        this.tracks = new ArrayList<TrackEntry>();
    }

    public String getName() {
        return genre == Genre.UNKNOWN ? genre.getHumanName() + "(" + originalName + ")" : genre.getHumanName();
    }

    public String getDate() {
        return date;
    }

    public String getFullPath() {
        return fullPath;
    }

    public ArrayList<TrackEntry> getTracks() {
        return tracks;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.genre != null ? this.genre.hashCode() : 0);
        hash = 83 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 83 * hash + (this.fullPath != null ? this.fullPath.hashCode() : 0);
        hash = 83 * hash + (this.tracks != null ? this.tracks.hashCode() : 0);
        hash = 83 * hash + (this.code != null ? this.code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GenreEntry other = (GenreEntry) obj;
        if (this.genre != other.genre) {
            return false;
        }
        if ((this.date == null) ? (other.date != null) : !this.date.equals(other.date)) {
            return false;
        }
        if ((this.fullPath == null) ? (other.fullPath != null) : !this.fullPath.equals(other.fullPath)) {
            return false;
        }
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        if (this.tracks != other.tracks && (this.tracks == null || !this.tracks.equals(other.tracks))) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return "Genre{" + genre + '}';
    }

}