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
import java.util.HashMap;
import java.util.List;
import nl.meine.ibb.stripes.Block;

/**
 *
 * @author Meine Toonen
 */
public abstract class Vectorizer {

    protected byte[][] palette;

    public void init() {
        palette = new byte[8][4];
        for (int colorcnt = 0; colorcnt < 8; colorcnt++) {
            palette[colorcnt][0] = (byte) (-128 + colorcnt * 32); // R
            palette[colorcnt][1] = (byte) (-128 + colorcnt * 32); // G
            palette[colorcnt][2] = (byte) (-128 + colorcnt * 32); // B
            palette[colorcnt][3] = (byte) 255;             // A
        }
    }

    public abstract List<Block> process(File input, int width, int height, double resolution);

    public abstract String fileToSvg(File input) throws Exception;

    public abstract List<Block> svgToBlockList(String svg, int width, int height, double resolution);

    protected HashMap<String, Float> getDefaultOptions() {
        HashMap<String, Float> options = new HashMap<>();

        // Tracing
        options.put("ltres", 1f);
        options.put("qtres", 1f);
        options.put("pathomit", 8f);

        // Color quantization
        options.put("colorsampling", 1f); // 1f means true ; 0f means false: starting with generated palette
        options.put("numberofcolors", 16f);
        options.put("mincolorratio", 0.02f);
        options.put("colorquantcycles", 3f);

        // SVG rendering
        options.put("scale", 1f);
        options.put("simplifytolerance", 0f);
        options.put("roundcoords", 1f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3 places, like 7.356 ; etc.
        options.put("lcpr", 0f);
        options.put("qcpr", 0f);
        options.put("desc", 0f); // 1f means true ; 0f means false: SVG descriptions deactivated
        options.put("viewbox", 0f); // 1f means true ; 0f means false: fixed width and height

        // Selective Gauss Blur
        options.put("blurradius", 0f); // 0f means deactivated; 1f .. 5f : blur with this radius
        options.put("blurdelta", 20f); // smaller than this RGB difference will be blurred
        return options;
    }
}
