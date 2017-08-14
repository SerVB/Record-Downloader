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

/**
 * Структура "Трек".
 */
public final class TrackEntry {

    /**
     * Оригинальное имя.
     */
    private final String originalName;

    /**
     * Дата изменения.
     */
    private final String date;

    /**
     * Размер в байтах.
     */
    private final long size;

    /**
     * Прямая ссылка на трек.
     */
    private final String fullPath;

    /**
     * Конструирует трек.
     *
     * @param originalName  Отображаемое имя.
     * @param date          Дата изменения.
     * @param size          Размер в байтах.
     * @param fullPath      Прямая ссылка на трек.
     */
    public TrackEntry(final String originalName, final String date, final long size, final String fullPath) {
        this.originalName = originalName;
        this.date = date;
        this.size = size;
        this.fullPath = fullPath;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.originalName != null ? this.originalName.hashCode() : 0);
        hash = 83 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 83 * hash + (int) (this.size ^ (this.size >>> 32));
        hash = 83 * hash + (this.fullPath != null ? this.fullPath.hashCode() : 0);
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
        final TrackEntry other = (TrackEntry) obj;
        if (this.size != other.size) {
            return false;
        }
        if ((this.originalName == null) ? (other.originalName != null) : !this.originalName.equals(other.originalName)) {
            return false;
        }
        if ((this.date == null) ? (other.date != null) : !this.date.equals(other.date)) {
            return false;
        }
        if ((this.fullPath == null) ? (other.fullPath != null) : !this.fullPath.equals(other.fullPath)) {
            return false;
        }
        return true;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getDate() {
        return date;
    }

    public long getSize() {
        return size;
    }

    public String getFullPath() {
        return fullPath;
    }

    @Override
    public String toString() {
        return "Track{" + originalName + '}';
    }

}