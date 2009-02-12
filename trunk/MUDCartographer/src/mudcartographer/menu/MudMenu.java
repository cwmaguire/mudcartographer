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
package mudcartographer.menu;

import mudcartographer.MudController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class MudMenu extends JMenu {

    protected MudController controller;
    protected abstract String getMenuText();
    protected abstract List<Component> getMenusAndItems();

    public MudMenu(MudController controller){
        this.controller = controller;
    }

    public void createAndAddMenusAndItems(){
        List<Component> menuComponents = getMenusAndItems();
        List<Component> pluginMenuComponents;

        if(menuComponents != null && !menuComponents.isEmpty()){
            for(Component component : menuComponents){
                add(component);
            }
            add(new JSeparator());
        }

        if(controller.plugin != null){
            pluginMenuComponents = controller.plugin.getMenuComponents(getMenuText());
            if(pluginMenuComponents != null && !pluginMenuComponents.isEmpty()){
                for(Component component : pluginMenuComponents){
                    add(component);
                }
            }
        }

    }
}
