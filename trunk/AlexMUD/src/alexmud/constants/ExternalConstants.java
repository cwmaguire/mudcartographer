/*
Copyright 2008 Chris Maguire (cwmaguire@gmail.com)

MUD Cartographer is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

MUD Cartographer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with MUD Cartographer.  If not, see <http://www.gnu.org/licenses/>.
 */

package alexmud.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalConstants {

    private static final Map<String, List<String>> CONSTANTS = new HashMap<String, List<String>>();
    public static final String DIRECTIONS_KEY = "directions";
    public static final String ROOM_FLAGS_KEY = "room flags";
    public static final String EXIT_FLAGS_KEY = "exit flags";
    public static final String TERRAIN_TYPES_KEY = "terrain types";

    public static List<String> getConstants(String key){
        return CONSTANTS.get(key);
    }

    // ToDo: CM - change this to load from an ini file or something
    public static void load(){
        loadDirections();
        loadRoomFlags();
        loadExitFlags();
        loadTerrainTypes();
    }

    private static void loadDirections(){
        CONSTANTS.put(DIRECTIONS_KEY, Arrays.asList("NORTH", "SOUTH", "EAST","WEST","UP","DOWN","NORTHEAST","SOUTHEAST","SOUTHWEST","NORTHWEST"));
    }

    private static void loadRoomFlags(){
        CONSTANTS.put(ROOM_FLAGS_KEY, Arrays.asList("DARK", "SAFE", "FALL-TRAP", "INDOORS", "NO-SUMMON", "S-ENCOUNTER", "E-ENCOUNTER", "P-ENCOUNTER",
                                                    "HAS-LIGHT", "PRIVATE", "SUN-LIGHT", "NO-MAGIC", "RESTRICTED", "SOUNDPROOF", "NO-SLEEP",
                                                    "DANGEROUS", "PRISON", "WILDERNESS", "WATER", "SALTWATER", "POISONED", "BUSY", "NO-PURGE",
                                                    "ARENA", "TELEPORT", "SAVES"));
    }

    private static void loadExitFlags(){
        CONSTANTS.put(EXIT_FLAGS_KEY, Arrays.asList("DOOR", "CLOSED", "LOCKED", "SECRET", "SWITCH-ACTIVATED", "TRAPPED", "NO-MOB", "ILLUSION",
                                                   "PLURAL", "UNOBVIOUS", "AUTOCLOSE", "ONE-WAY", "BEND", "WILDERNESS", "DISPLAY-ON-ENTRY",
                                                   "MOVE-DESC-ONLY", "FOLIAGE", "CUT-FOLIAGE", "CLIMB", "ROOM-DESC", "GENERATED", "CONTINUATION"));
        
    }

    private static void loadTerrainTypes(){
        CONSTANTS.put(TERRAIN_TYPES_KEY, Arrays.asList(
                "Inside", "City Street", "Field", "Forest", "Hills", "Mountains", "Water-Wadable", "Water-Unswimable", "Air", "Water-Rough",
                "Desert", "Ice", "Underwater", "Dungeon", "River-North", "River-Northeast", "River-East", "River-Southeast", "River-South",
                "River-Southwest", "River-West", "River-Northwest", "Road", "Trail", "Tunnel", "Swamp", "Plains", "Sewer", "Garden", "City Square",
                "Beach", "Passage", "Forest Path", "Hilly Path", "Mountain Path", "Cavern", "Jungle", "Jungle Path", "Tree"));
    }
}
