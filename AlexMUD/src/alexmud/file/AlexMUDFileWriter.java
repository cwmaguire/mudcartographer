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

import mudcartographer.map.MudMap;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AlexMUDFileWriter {
    public static void write(File file, MudMap map) {

        try {
            FileOutputStream fileStream = new FileOutputStream(file);

            try {
                BufferedOutputStream bufferedStream = new BufferedOutputStream(fileStream);

                try {
                    bufferedStream.write(AlexMUDFileWriter.getString(map).toCharArray());
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

    public static String getString(MudMap map){
        return "Wow, it saved!";
    }
}
