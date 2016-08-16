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

import java.util.List;
import nl.meine.ibb.stripes.Block;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Meine Toonen
 */
public class TextVectorizerTest {
  
    private TextVectorizer instance = new TextVectorizer();
            
    @Test
    public void wordTest(){
        String text = "aap";
        List<Block> blocks = instance.process(text, 32, 12, 0.1);
        Assert.assertNotEquals("Cannot be empty", 0, blocks.size());
    }
    
}
