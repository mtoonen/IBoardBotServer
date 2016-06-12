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
package nl.meine.ibb.stripes;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import nl.meine.ibb.processor.ImageVectorizer;
import nl.meine.ibb.processor.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Meine Toonen
 */
@UrlBinding("/action/server")
public class ServerActionBean implements ActionBean {

    private static final Log log = LogFactory.getLog(ServerActionBean.class);
    private Processor p;
    
    public ServerActionBean() {
    }

    private ActionBeanContext context;
    
    @Validate
    private String STATUS;

    @Validate
    private String ID_IWBB;

    @Validate
    private int NUM;

    private String svg;
    
 
    // <editor-fold desc="Getters and Setters" defaultstate="collapsed">
    public void setNUM(int NUM) {
        this.NUM = NUM;
    }
   public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getID_IWBB() {
        return ID_IWBB;
    }

    public void setID_IWBB(String ID_IWBB) {
        this.ID_IWBB = ID_IWBB;
    }

    public int getNUM() {
        return NUM;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }
    
    // </editor-fold>

    public Resolution view() throws Exception{
          p = new Processor(100, 100, 0.1);
         
        File input = new File(this.getClass().getResource("2lines.png").getFile());
        ImageVectorizer iv = new ImageVectorizer();
        svg = iv.fileToSvg(input);
        return new ForwardResolution("/WEB-INF/svgtest.jsp");
    }
    
    @DefaultHandler
    public Resolution poll() {
        log.debug("Polled. AppId " + ID_IWBB + " with status " + STATUS);
         p = new Processor(36, 12, 0.1);
         
        File input = new File(this.getClass().getResource("2lines.png").getFile());
        List<Block> bs = p.processImage(input);
        final Block b = bs.get(0);
       /* final Block b = new Block();
        b.addPosition(2000, 400);
        b.down();
        b.addPosition(1500, 800);
        b.addPosition(3000, 200);
        b.addPosition(3500, 1000);
        b.up();
        b.finish();*/
       /* final Block b = new Block(1);
        b.addPosition(1000,100);
        b.down();
        b.addPosition(10500, 150);
        b.up();
        b.finish();*/

        StreamingResolution res = new StreamingResolution("application/octet-stream") {
            @Override
            public void stream(HttpServletResponse response) throws Exception {
                OutputStream out = response.getOutputStream();
                b.write(out);
                out.close();
            }
        };
        res.setAttachment(true);
        res.setFilename(ID_IWBB + ".g092");
        return res;
    }
}
