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

import mudcartographer.map.MudMap;

public class Exit {
    private MudMap.Direction direction;
    public Integer destination;
    public String lookDescription;
    public String keywords;
    public String doorName;
    public String databaseKey;
    public String lockDifficulty;
    public String vKey;

    public Exit(MudMap.Direction direction){
        this.direction = direction;
    }

    public MudMap.Direction getDirection(){
        return this.direction;
    }
}
