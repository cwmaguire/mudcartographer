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

package alexmud.map;

import java.util.List;

public class RoomKeywordDescription {
    public String keywords;
    public String description;

    public RoomKeywordDescription(String[] keywordDescription){
        keywords = keywordDescription[0];
        description = keywordDescription[1];
    }

    public static String[][] get2DArrayOfKeywordDescriptions(List<RoomKeywordDescription> keywordDescriptions){
        String[][] keywordDescriptionArray = new String[keywordDescriptions.size()][2];
        for(int i = 0; i < keywordDescriptions.size(); i++){
            keywordDescriptionArray[i] = keywordDescriptions.get(i).toArray();
        }
        return keywordDescriptionArray;
    }

    public String[] toArray(){
        String[] keywordDescription = new String[2];
        keywordDescription[0] = keywords;
        keywordDescription[1] = description;
        return keywordDescription;
    }
}
