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
import net.tinyos.message.Message;
import net.tinyos.message.MessageListener;

/**
 *
 * @author vitorcezar
 */
public class ComunicacaoNos implements MessageListener {

    /**
     * indica quando uma mensagem é recebida
     */
    private boolean mensagemFoiRecebida;
    
    /**
     * armazena a mensagem recebida
     */
    private byte[] mensagemRecebida;
    
    /**
     * construtor
     */
    public ComunicacaoNos() {
        mensagemFoiRecebida = false;
    }
    
    /**
     * função executada quando uma mensagem for recebida
     * @param i
     * @param message 
     */
    @Override
    public void messageReceived( int i, Message message ) {
        mensagemFoiRecebida = true;
        mensagemRecebida = message.dataGet();
    }
    
    /**
     * indica quando uma mensagem for recebida
     * @return se a mensagem foi recebida
     */
    public boolean mensagemRecebida() {
        if( mensagemFoiRecebida ) {
            mensagemFoiRecebida = false;
            return true;
        }
        return false;
    }
    
    /**
     * retorna a mensagem recebida
     * @return a mensagem recebida
     */
    public byte[] pegaMensagemRecebida() {
        return mensagemRecebida;
    }
}
