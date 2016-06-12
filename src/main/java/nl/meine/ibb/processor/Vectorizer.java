/*
 * Copyright (C) 2016 Meine Toonen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.meine.ibb.processor;

import java.io.File;
import java.util.List;
import nl.meine.ibb.stripes.Block;

/**
 *
 * @author meine
 */
public abstract class Vectorizer {
    
    public abstract void init();
    
    public abstract List<Block> process(File input, int xOffset, int yOffset);
    
    public abstract String fileToSvg(File input)throws Exception;
    
    public abstract List<Block> svgToBlockList(String svg, int xOffset, int yOffset);
    
}
