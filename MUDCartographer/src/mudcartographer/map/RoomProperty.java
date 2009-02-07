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

package mudcartographer.map;

public enum RoomProperty {
        SYMBOL,
        ID,
        RECTANGLE,
        POINT,
        TEXT_COLOR,
        BACKGROUND_COLOR,
        DESCRIPTION,
        PAINT,
        NAME,
        TERRAIN,
        FLAGS;

        public int getFlagBits(){
            return (int) Math.pow(2, (this.ordinal() + 1));
        }

        public static int getAll(){
            return Integer.MAX_VALUE; // in binary this is many 1's
        }
    }
