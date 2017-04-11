/*
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

import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class holds the reader data and its connection. 
 * @author Rafagan Soares
 */
public class RFIDReader {
    
    private final AlienClass1Reader reader;

    public RFIDReader(String ipAddress) throws AlienReaderException{

        /* inicializando os parâmetros do primeiro reader no endereço 41. */
        reader = new AlienClass1Reader();

        /*Parâmentros para a conexão com o leitor via rede. */
        reader.setConnection(ipAddress, 23);
        reader.setUsername("alien");
        reader.setPassword("password");
        
    }

    /**
     * Open the connection with the given IP Address. 
     * @throws AlienReaderException 
     */
    public void OpenReader() throws AlienReaderException {
        /* Open a connection to the reader */
        reader.open();
    }

    /**
     * Get the reader connected IP.  
     * @return reader IP 
     */
    public String getIPAddress(){
        try {
            return reader.getIPAddress();
        } catch (AlienReaderException ex) {
            Logger.getLogger(RFIDReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Gets the tags of the reader have read. 
     * @return List of Tags that the reader have at the request moment. 
     * @throws AlienReaderException 
     */
    public Tag[] getTags() throws AlienReaderException {
        /* Ask the reader to read tags and print them. */
        Tag tagList[] = reader.getTagList();
        if (tagList == null) {
          System.out.println("No Tags Found");
        }
        return tagList;
    }
    
    /*
    public void inputDataInformacaoRFID (ArrayList< InformacaoRFID > infTags) throws AlienReaderException{
        Tag tagList[] = reader.getTagList();
        for( Tag tag : tagList){
            InformacaoRFID temp = new InformacaoRFID(tag.getTagID(), tag.ip, 0)
        }
    }
    */
    /**
     * Close connection with reader. 
     */
    public void CloseReader(){
        // Close the connection
        reader.close();
    }
}
