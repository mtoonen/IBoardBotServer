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
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
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

    /* int[][] commands = {
        {4009, 4001},//   [FA9FA1]    # Nuevo dibujo
        {4009, 0001},//   [FA9001]    # Block number (this number is calculated by the server)
        {4001, 4001},//   [FA1FA1]    # Start drawing (new draw)
        {4003, 0000},//   [FA3000]     # pen lift
        {0000, 0000},//   [000000]      # Move to  X = 0, Y = 0
        {1000, 1000},//   [3E83E8]     # Move to X = 100mm, Y = 100mm
        {4004, 0000},//   [FA4000]     # pen down (draw)
        {1500, 1000},//   [5DC3E8]    # Move to  X = 150mm, Y = 100mm
        {4003, 0000},//   [FA3000]     # Pen lift
        {0000, 0000},//   [000000]      # Move to 0,0
        {4002, 0000}//   [FA2000]     # Stop drawing (Finish).
    };*/
  
 
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
    // </editor-fold>

    @DefaultHandler
    public Resolution poll() {
        log.debug("Polled. AppId " + ID_IWBB + " with status " + STATUS);
         p = new Processor(100, 100);
         
        File input = new File(this.getClass().getResource("2lines.png").getFile());
        List<Block> bs = p.processImage(input);
        final Block b = bs.get(0);
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
