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

package alexmud.file;

import alexmud.map.AMRoom;
import alexmud.map.Exit;
import alexmud.map.RoomKeywordDescription;
import mudcartographer.file.MudFileWriter;
import mudcartographer.map.MudMap;
import mudcartographer.map.Room;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AlexMUDFileWriter extends MudFileWriter {
    private static final String SINGLE_LF = "\n\n";
    private static final String DOUBLE_LF = "\n\n";
    private static final String SOFT_TAB = "    ";
    private static final String TILDE = "~";
    private static final String DOUBLE_QUOTE = "\"";

    public void write(File file, MudMap map) {

        try {
            FileOutputStream fileStream = new FileOutputStream(file);

            try {
                BufferedOutputStream bufferedStream = new BufferedOutputStream(fileStream);

                try {
                    bufferedStream.write(createMapFile(map).getBytes());
                    bufferedStream.flush();
                } finally {
                    bufferedStream.close();
                }
            } finally {
                fileStream.close();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String createMapFile(MudMap map){
        List<Room> rooms = map.getRooms();
        StringBuilder mapString = new StringBuilder();

        for(Room room : rooms){
            mapString.append(createRoomString((AMRoom) room));
        }

        return mapString.toString();
    }

    public String createRoomString(AMRoom room){
        StringBuilder roomString = new StringBuilder();
        List<RoomKeywordDescription> keywordDescs = room.getKeywordDescriptions();
        List<String> flagNames = new ArrayList<String>(room.flags.keySet());

        roomString.append("#Room").append(SOFT_TAB).append(room.getID()).append(DOUBLE_LF);
        roomString.append("#Name~").append(SINGLE_LF).append(room.getName() == null ? "Room " + room.getID() : room.getName()).append(TILDE).append(DOUBLE_LF);
        roomString.append("#Description~").append(room.getDescription()).append(TILDE).append(DOUBLE_LF);

        if(keywordDescs != null && !keywordDescs.isEmpty()){
            roomString.append("#XtraDescs:").append(SINGLE_LF);

            for(RoomKeywordDescription keywordDesc : room.getKeywordDescriptions()){
                roomString.append(SOFT_TAB).append("#Keywords~:").append(keywordDesc.keywords).append(TILDE).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#Description~:").append(SINGLE_LF).append(keywordDesc.description).append(TILDE).append(SINGLE_LF);
            }

            roomString.append("#EndExtraDescs").append(DOUBLE_LF);
        }

        roomString.append("#Flags").append(SOFT_TAB).append(DOUBLE_QUOTE);

        for(String flagName : flagNames){
            if(room.isFlagSet(flagName)){
                roomString.append(flagName).append(" ");
            }
        }
        roomString.append("\"").append(SINGLE_LF);

        roomString.append("#Terrain").append(SOFT_TAB).append(DOUBLE_QUOTE).append(room.getTerrain()).append(DOUBLE_QUOTE).append(SINGLE_LF);

        for(Exit exit : room.exits){
            if(exit != null){
                roomString.append("#Exit:").append(SOFT_TAB).append(DOUBLE_QUOTE).append(exit.getDirection()).append(DOUBLE_QUOTE).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#Destination: ").append(SOFT_TAB).append(exit.destination).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#LookDesc~:").append(SINGLE_LF);
                roomString.append(SOFT_TAB).append(exit.lookDescription).append(TILDE).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#Keywords~:").append(SOFT_TAB).append(exit.keywords).append(TILDE).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#DoorName~:").append(SOFT_TAB).append(exit.doorName).append(TILDE).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#ExitFlags:").append(SOFT_TAB).append(DOUBLE_QUOTE).append("DOOR").append(DOUBLE_QUOTE).append(TILDE).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#KeyDBnum:").append(SOFT_TAB).append(exit.databaseKey).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#LockDifficulty:").append(SOFT_TAB).append(exit.lockDifficulty).append(SINGLE_LF);
                roomString.append(SOFT_TAB).append("#KeyVnum:").append(SOFT_TAB).append(exit.vKey).append(SINGLE_LF);
                roomString.append("#Exit:").append(SINGLE_LF);
            }
        }

        roomString.append("#EndRoom").append(DOUBLE_LF);

        return roomString.toString();
    }
}

/*

#Room nnnn



#Name~
example name~

#Description~
example desc~

[used to match descriptions against “look” keywords]

#XtraDescs:
            #Keywords~:     itemlore-dir-desc black streak~
            #Description~:
            example desc~
#EndExtraDescs

[double-quoted, space-delimited list]
#Flags              "INDOORS E-ENCOUNTER P-ENCOUNTER"
#Terrain             "Inside"
#Exit:                "north"
            [required to go anywhere]
            (opt) #Destination:         1234 (room number)
            [may be used alone to supply a description for a direction without the option to go in that direction]

            #LookDesc~:
            example desc~

            (opt) #Keywords~:         door metal~
            (opt) #DoorName~:       metal door~
            (opt) #ExitFlags:           "DOOR"
            (opt) #KeyDBnum:         91
            (opt) #LockDifficulty:     100
            (opt) #KeyVnum:           0

#EndExit

#EndRoom

 */

/*

Exit Flags

DOOR
CLOSED
LOCKED
SECRET
SWITCH-ACTIVATED
TRAPPED
NO-MOB
ILLUSION
PLURAL
UNOBVIOUS
AUTOCLOSE
ONE-WAY
BEND
WILDERNESS
DISPLAY-ON-ENTRY
MOVE-DESC-ONLY
FOLIAGE
CUT-FOLIAGE
CLIMB
ROOM-DESC
GENERATED
CONTINUATION

 */

/*

Room Flags

DARK
SAFE
FALL-TRAP
INDOORS
NO-SUMMON
S-ENCOUNTER
E-ENCOUNTER
P-ENCOUNTER
HAS-LIGHT
PRIVATE
SUN-LIGHT
NO-MAGIC
RESTRICTED
SOUNDPROOF
NO-SLEEP
DANGEROUS
PRISON
WILDERNESS
WATER
SALTWATER
POISONED
BUSY
NO-PURGE
ARENA
TELEPORT
SAVES

 */

/*
Terrain Types

Inside
City Street
Field
Forest
Hills
Mountains
Water-Wadable
Water-Unswimable
Air
Water-Rough
Desert
Ice
Underwater
Dungeon
River-North
River-Northeast
River-East
River-Southeast
River-South
River-Southwest
River-West
River-Northwest
Road
Trail
Tunnel
Swamp
Plains
Sewer
Garden
City Square
Beach
Passage
Forest Path
Hilly Path
Mountain Path
Cavern
Jungle
Jungle Path
Tree
*/