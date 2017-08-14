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
package servb.record.core;

import java.util.HashMap;

/**
 * Перечисление "Жанр".
 *
 * @author SerVB
 */
public enum Genre {

    UNKNOWN(null, "Unknown Genre"),
    BREAKS("brks", "Record Breaks"),
    CHILLOUT("chil", "Record Chill-Out"),
    EDM("club", "Record EDM"),
    DANCECORE("dc", "Record Dancecore"),
    DEEP("deep", "Record Deep"),
    DUBSTEP("dub", "Record Dubstep"),
    FUTURE_HOUSE("fut", "Future House"),
    GOA("goa", "GOA/PSY"),
    GOLD("gold", "Gold"),
    ГОП("gop", "Гоп FM"),
    МЕДЛЯК("mdl", "Медляк FM"),
    MINIMAL("mini", "Minimal/Tech"),
    MEGAMIX("mix", "Record Magamix"),
    НАФТАЛИН("naft", "Нафталин FM"),
    PIRATE_STATION("ps", "Pirate Station"),
    OLD_SCHOOL("pump", "Old School"),
    RAVE("rave", "Rave FM"),
    ROCK("rock", "Record Rock"),
    RADIO_RECORD("rr", "Radio Record"),
    RUSSIAN_MIX("rus", "Russian Mix"),
    СУПЕРДИСКОТЕКА_90("sd90", "Супердискотека 90-х"),
    TECHNO("techno", "TECHNO"), // ?
    HARDSTYLE("teo", "Record Hardstyle"),
    TRANCEMISSION("tm", "Trancemission Radio"),
    TRAP("trap", "Record Trap"),
    TROPICAL("trop", "Tropical"),
    VIP("vip", "Vip House"),
    BLACK("yo", "Black");

    private final String shortName;
    private final String humanName;

    private Genre(final String shortName, final String humanName) {
        this.shortName = shortName;
        this.humanName = humanName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getHumanName() {
        return humanName;
    }

    public static Genre getByShortName(final String shortName) {
        if (mapByShortName.isEmpty()) {
            for (final Genre genre : Genre.values()) {
                mapByShortName.put(genre.shortName, genre);
            }
        }
        return mapByShortName.containsKey(shortName) ? mapByShortName.get(shortName) : UNKNOWN;
    }

    public static final HashMap<String, Genre> mapByShortName = new HashMap<String, Genre>();

}
