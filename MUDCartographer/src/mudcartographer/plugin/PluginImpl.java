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
package mudcartographer.plugin;

import alexmud.plugin.AlexMUDMapPlugin;
import defaultplugin.plugin.DefaultPlugin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public enum PluginImpl {
    ALEXMUD("AlexMUD"){
        public Plugin getPlugin(){
            return new AlexMUDMapPlugin();
        }
        public KeyStroke getKeyStroke(){
            return KeyStroke.getKeyStroke(getKeyEvent(), ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);
        }
        public int getKeyEvent() {
            return KeyEvent.VK_A;
        }
    },
    DEFAULT("MUDCartographer"){
        public Plugin getPlugin(){
            return new DefaultPlugin();
        }
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(getKeyEvent(), ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);
        }
        public int getKeyEvent() {
            return KeyEvent.VK_M;
        }
    };

    private String description;

    private PluginImpl(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract Plugin getPlugin();
    public abstract KeyStroke getKeyStroke();
    public abstract int getKeyEvent();
}
