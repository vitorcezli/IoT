/*
 * Copyright (C) 2017 vitorcezar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * this class shows a photo to the user
 * @author vitorcezar
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PainelFoto extends JPanel {

    /**
     * the variable which saves the photo
     */
    private BufferedImage image;

    /**
     * initializes the class with the photo
     * @param filePath the path of the photo
     */
    public PainelFoto( String filePath ) {
       try {                
          image = ImageIO.read( new File( filePath ) );
       } catch( IOException ex ) {
            
       }
    }

    /**
     * shows the photo
     * @param g graphics 
     */
    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        g.drawImage( image, 0, 0, this );            
    }
}
