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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.Tag;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitorcezar
 */
public class InterfaceGrafica extends JFrame {

    /**
     * botão para ativar o timer
     */
    private JButton ativarButton;
    
    /**
     * botão que realiza uma tentativa de ler as etiquetas
     */
    private JButton lerEtiquetasButton;
    
    /**
     * botão para pausar o timer
     */
    private JButton pausarButton;
    
    /**
     * painel onde ficarão os botões
     */
    private JPanel botoesPanel;
    
    /**
     * combo box para identificar o modo da conexão
     */
    private JComboBox< String > modoConexaoComboBox;
    
    /**
     * combo box para identificar o modo de ordenação
     */
    private JComboBox< String > ordenacaoComboBox;
    
    /**
     * label do modo de conexão
     */
    private JLabel modoConexaoLabel;
    
    /**
     * label da tentativa de leitura
     */
    private JLabel tentativaLeituraLabel;
    
    /**
     * label de ordenação
     */
    private JLabel ordenacaoLabel;
    
    /**
     * label do tempo decorrido
     */
    private JLabel tempoDecorridoLabel;
    
    /**
     * painel onde estarão as configurações do programa
     */
    private JPanel configuracaoPanel;
    
    /**
     * painel de scroll para a tabela
     */
    private JScrollPane informacoesScrollPane;
    
    /**
     * tabela onde os dados serão exibidos
     */
    private JTable informacoesTable;
    
    /**
     * área de texto onde será exibido o tempo decorrido em segundos
     */
    private JTextField tempoDecorridoTextField;
    
    /**
     * área de texto onde será exibida a quantidade de vezes em que se tentou
     * ler as etiquetas
     */
    private JTextField tentativaLeituraTextField;
    
    /**
     * lista que armazenará as informações das etiquetas para serem exibidas
     * na tabela
     */
    private ArrayList< InformacaoRFID > informacoesRFID;
    
    /**
     * tempo decorrido em milissegundos
     */
    private int tempo;
    
    /**
     * temporizador do programa
     */
    private Timer timer;
    private Timer timer2;
    
    /**
     * quantidade de tentativas de leituras das etiquetas
     */
    private int quantidadeTentativa;
    
    /**
     * label do ip
     */
    private JLabel ipLabel;
    
    /**
     * campo de texto onde será exibido o ip do leitor
     */
    private JTextField ipTextField;
    
    /**
     * combo box que irá permitir selecionar o leitor
     */
    private JComboBox< String > leitorComboBox;
    
    /**
     * label do leitor
     */
    private JLabel leitorLabel;
    
    /* Reader in IP 150.164.10.41*/
    private RFIDReader reader1;
    
    /* Reader in IP 150.164.10.42*/
    private RFIDReader reader2;
    
    /**
     * Cria uma interface gráfica
     */
    public InterfaceGrafica() {
        super( "Interface RFID" );
        try {
            reader1 = new RFIDReader("150.164.10.41");
            reader2 = new RFIDReader("150.164.10.42");
        } catch (AlienReaderException ex) {
            System.out.println("Error: " + ex.toString());;
        }
        informacoesRFID = new ArrayList<>();
        inicializaComponentes();
        inicializaTimer();
        adicionaListeners();  
        tempo = 0;
        quantidadeTentativa = 0;
    }
    
    /**
     * mostra a interface gráfica para o usuário. Uma nova thread é criada
     * nessa função, ou seja, a interface será executada paralelamente ao
     * código
     */
    public void mostreInterfaceGrafica() {
        this.setVisible( true );
    }
    
    /**
     * incrementa a quantidade de vezes em que a etiqueta foi lida e modifica
     * o tempo da última leitura
     * @param id identificador da etiqueta
     */
    public void ultimaLeituraInformacao( String id ) {
        for( int i = 0; i < informacoesRFID.size(); i++ ) {
            if( informacoesRFID.get( i ).retornaId().equals( id ) ) {
                informacoesRFID.get( i ).defineUltimaLeitura( tempo );
                informacoesTable.setValueAt( 
                    informacoesRFID.get( i ).retornaTempoUltimaLeitura(), i, 2 
                );
                informacoesTable.setValueAt( 
                    informacoesRFID.get( i ).retornaQuantidadeLeitura(), i, 3 
                );
            }
        }
    }
    
    /**
     * adiciona informação de uma nova etiqueta na tabela
     * @param id identificação da nova etiqueta
     * @param ip endereço IP da etiqueta
     */
    public void adicionaNovaInformacao( String id, String ip ) {
        /* verifica se a identificação já existe na tabela, e caso exista não
        adiciona a informação da etiqueta */
        for( InformacaoRFID informacao : informacoesRFID ) {
            if( informacao.retornaId().equals( id ) ) {
                return;
            }
        }
        
        // adiciona a informação e atualiza a lista
        informacoesRFID.add( new InformacaoRFID( id, ip, tempo ) );
        ordenaLista();
        atualizaTabela();
    }
    
    /**
     * muda o valor da quantidade de tentativas de leitura
     */
    public void acrescentaQuantidadeTentativaLeitura() {
        quantidadeTentativa++;
        tentativaLeituraTextField.setText( 
            String.valueOf( quantidadeTentativa ) 
        );
    }
    
    /**
     * indica uma tentativa de leitura
     */
    private void tentaLeituraEtiquetas() {
        acrescentaQuantidadeTentativaLeitura();

        if(leitorComboBox.getSelectedIndex() == 1){
            try {
                reader1.openReader();
                Tag[] taglist = reader1.getTags();
                for(Tag tag : taglist){
                    String tagid = tag.getTagID();
                    boolean to_continue = false;
                    for( InformacaoRFID inf : informacoesRFID){
                        if(inf.retornaId().equals(tagid)) {
                            ultimaLeituraInformacao( tagid );
                            to_continue = true;
                        }
                    }
                    if( to_continue ) { continue; }
                    adicionaNovaInformacao( tagid, "" );
                }
                reader1.closeReader();
            } catch (AlienReaderException ex) {
                Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        else {
           try {
                reader2.openReader();
                Tag[] taglist = reader2.getTags();
                for(Tag tag : taglist){
                    String tagid = tag.getTagID();
                    boolean to_continue = false;
                    for( InformacaoRFID inf : informacoesRFID){
                        if(inf.retornaId().equals(tagid)) {
                            ultimaLeituraInformacao( tagid );
                            to_continue = true;
                        }
                    }
                    if( to_continue ) { continue; }
                    adicionaNovaInformacao( tagid, "" );
                }
                reader2.closeReader();
            } catch (AlienReaderException ex) {
                Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
            }    
        }
        
        atualizaTabela();
        
    }
    
    /**
     * reinicia a leitura das etiquetas zerando todos os dados
     */
    private void reiniciaTudo() {
        informacoesRFID = new ArrayList< InformacaoRFID >();
        quantidadeTentativa = 0;
        tempo = 0;
        tempoDecorridoTextField.setText( "0" );
        tentativaLeituraTextField.setText( "0" );
        atualizaTabela();
    }
    
    /**
     * ordena a lista de acordo com a opção escolhida no JComboBox
     */
    private void ordenaLista() {
        int tipoOrdenacao = ordenacaoComboBox.getSelectedIndex();
        Comparator< InformacaoRFID > comparadorOrdenacao =
            comparadorOrdenacao = new Comparator< InformacaoRFID >() {
                @Override
                public int compare( InformacaoRFID i1, InformacaoRFID i2 ) {
                    return i1.retornaId().compareTo( i2.retornaId() );
                }
            };
        
        switch( tipoOrdenacao ) {
            case 0:
                // já foi configurado
                break;
            case 1:
                comparadorOrdenacao = new Comparator< InformacaoRFID >() {
                    @Override
                    public int compare( InformacaoRFID i1, InformacaoRFID i2 ) {
                        if( i1.retornaQuantidadeLeitura() > 
                            i2.retornaQuantidadeLeitura() ) {
                            return -1;
                        } else if( i1.retornaQuantidadeLeitura() <
                            i2.retornaQuantidadeLeitura() ) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                };
                break;
        }
        Collections.sort( informacoesRFID, comparadorOrdenacao );
    }
    
    /**
     * remove todos os itens da tabela e os adiciona na ordem da lista
     */
    private void atualizaTabela() {
        DefaultTableModel modelo = 
            ( DefaultTableModel ) informacoesTable.getModel();
        
        // remove os itens da tabela
        int quantidadeLinhas = modelo.getRowCount();
        for( int i = quantidadeLinhas - 1; i >= 0; i-- ) {
            modelo.removeRow( i );
        }
        
        // adiciona os itens na ordem da lista
        for( InformacaoRFID informacao : informacoesRFID ) {
            modelo.addRow( new Object[]{ 
                informacao.retornaId(),
                stringDivisao( 
                    informacao.retornaQuantidadeLeitura(),
                    tempo 
                ),
                stringDivisao( 
                    informacao.retornaQuantidadeLeitura(), 
                    quantidadeTentativa 
                ),
                informacao.retornaQuantidadeLeitura() 
            } );
        }
    }
    
    /**
     * retorna uma string com no máximo dois dígitos decimais
     * @param dividendo número que será dividido
     * @param divisor número que divide
     * @return uma string com no máximo dois dígitos decimais
     */
    private String stringDivisao( double dividendo, double divisor ) {
        if( divisor == 0 ) {
            return "Infinity";
        }
        String divisao = String.valueOf( dividendo / divisor );
        int menor = Math.min( divisao.indexOf( '.' ) + 3, divisao.length() );
        return divisao.substring( 0, menor );
    }
    
    /**
     * adiciona listeners nos elementos da interface gráfica
     */
    private void adicionaListeners() {
        
        // listeners dos botões
        ativarButton.addActionListener( ( e ) -> {
            if(leitorComboBox.getSelectedIndex() == 1){
                try {
                    reader1.setupToAutomousModeON();
                } catch (Exception ex) {
                    Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
                }   
            } else {
               try {
                    reader2.setupToAutomousModeON();
                } catch (Exception ex) {
                    Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
                }    
            }

            timer.start();
            timer2.start();
        } );
        pausarButton.addActionListener( ( e ) -> {

            if(leitorComboBox.getSelectedIndex() == 1){
                try {
                    reader1.setupToAutomousModeOFF();
                } catch (Exception ex) {
                    Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
                }   
            } else {
               try {
                    reader2.setupToAutomousModeOFF();
                } catch (Exception ex) {
                    Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
                }    
            }
            
            timer.stop();
            timer2.stop();
        } );
        lerEtiquetasButton.addActionListener( ( e ) -> {
            tentaLeituraEtiquetas();
        } );
        
        // listener das combo boxes
        ordenacaoComboBox.addItemListener((ItemEvent e) -> {
            ordenaLista();
            atualizaTabela();
        });
        modoConexaoComboBox.addItemListener((ItemEvent e) -> {
            timer.stop();
            timer2.stop();
            if( modoConexaoComboBox.getSelectedIndex() == 0 ) {
                ativarModoAtivo();
            } else {
                ativarModoAutonomo();
            }
        });
        leitorComboBox.addItemListener((ItemEvent e) -> {
            reiniciaTudo();
            timer.stop();
            timer2.stop();
            if( leitorComboBox.getSelectedIndex() == 0 ) {
                ipTextField.setText("150.164.10.41");
            } else {
                ipTextField.setText("150.164.10.42");
            }
        });
    }
    
    /**
     * inicializa um temporizador que irá incrementar a variável tempo a cada
     * 1 ms e atualizar o valor no campo de texto
     */
    private void inicializaTimer() {
        ActionListener listener = (ActionEvent e) -> {
            tempoDecorridoTextField.setText( 
                    String.valueOf( tempo )
            );
            atualizaTabela();
            tempo++;
        };
        timer = new Timer( 1000, listener );
        
        ActionListener listener2 = (ActionEvent e) -> {
            acrescentaQuantidadeTentativaLeitura();

            if(leitorComboBox.getSelectedIndex() == 1){
                Tag[] taglist = reader1.message.getTagList();
                for(Tag tag : taglist){
                    String tagid = tag.getTagID();
                    boolean to_continue = false;
                    for( InformacaoRFID inf : informacoesRFID){
                        if(inf.retornaId().equals(tagid)) {
                            ultimaLeituraInformacao( tagid );
                            to_continue = true;
                        }
                    }
                    if( to_continue ) { continue; }
                    adicionaNovaInformacao( tagid, "" );
                }   
            }
            else {
                Tag[] taglist = reader2.message.getTagList();
                for(Tag tag : taglist){
                    String tagid = tag.getTagID();
                    boolean to_continue = false;
                    for( InformacaoRFID inf : informacoesRFID){
                        if(inf.retornaId().equals(tagid)) {
                            ultimaLeituraInformacao( tagid );
                            to_continue = true;
                        }
                    }
                    if( to_continue ) { continue; }
                    adicionaNovaInformacao( tagid, "" );
                }    
            }

            atualizaTabela();
        };
        timer2 = new Timer( 1000, listener2 );
    }
    
    /**
     * modo ativo, em que o usuário deve mandar o programa ler as etiquetas
     */
    private void ativarModoAtivo() {
        ativarButton.setEnabled( true );
        pausarButton.setEnabled( true );
        lerEtiquetasButton.setEnabled( false );
    }
    
    /**
     * modo autônomo, em que o programa lê as etiquetas automaticamente
     */
    private void ativarModoAutonomo() {
        ativarButton.setEnabled( false );
        pausarButton.setEnabled( false );
        lerEtiquetasButton.setEnabled( true );
    }

    /**
     * inicialização dos componentes. Código gerado pelo NetBeans
     */                       
    private void inicializaComponentes() {

        informacoesScrollPane = new JScrollPane();
        informacoesTable = new JTable();
        configuracaoPanel = new JPanel();
        modoConexaoComboBox = new JComboBox<>();
        tempoDecorridoLabel = new JLabel();
        tempoDecorridoTextField = new JTextField( "0" );
        ordenacaoLabel = new JLabel();
        ordenacaoComboBox = new JComboBox<>();
        modoConexaoLabel = new JLabel();
        botoesPanel = new JPanel();
        ativarButton = new JButton();
        pausarButton = new JButton();
        tentativaLeituraLabel = new javax.swing.JLabel();
        tentativaLeituraTextField = new javax.swing.JTextField( "0" );
        lerEtiquetasButton = new javax.swing.JButton();
        leitorLabel = new javax.swing.JLabel("Leitor:");
        leitorComboBox = new javax.swing.JComboBox<>();
        leitorComboBox.setBackground(new java.awt.Color(255, 255, 255));
        leitorComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leitor 1", "Leitor 2" }));
        leitorComboBox.setToolTipText("");
        leitorComboBox.setFocusable(false);
        ipLabel = new javax.swing.JLabel( "IP do leitor:");
        
        /* adicione como parâmetro do construtor do textField a string com o ip
        do primeiro leitor. ADD CODE HERE */
        ipTextField = new javax.swing.JTextField( "150.164.10.41" );
        ipTextField.setEditable(false);
        ipTextField.setBackground(new java.awt.Color(255, 255, 255));
        lerEtiquetasButton.setText("Ler etiquetas");
        lerEtiquetasButton.setFocusable(false);        
        tentativaLeituraLabel.setText("Tentativa leitura:");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        informacoesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Read Rate", "Success Rate", "Quantidade leitura" }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        informacoesTable.getTableHeader().setReorderingAllowed(false);
        informacoesScrollPane.setViewportView(informacoesTable);

        modoConexaoComboBox.setBackground(new java.awt.Color(255, 255, 255));
        modoConexaoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Autônomo", "Ativo" }));
        modoConexaoComboBox.setToolTipText("");
        modoConexaoComboBox.setFocusable(false);

        tempoDecorridoLabel.setText("Tempo decorrido (s):");

        tempoDecorridoTextField.setEditable(false);
        tentativaLeituraTextField.setEditable(false);
        tempoDecorridoTextField.setBackground(new java.awt.Color(255, 255, 255));
        tentativaLeituraTextField.setBackground(new java.awt.Color(255, 255, 255));

        ordenacaoLabel.setText("Ordenar por:");

        ordenacaoComboBox.setBackground(new java.awt.Color(255, 255, 255));
        ordenacaoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Identificação", "Quantidade de leitura" }));
        ordenacaoComboBox.setToolTipText("");
        ordenacaoComboBox.setFocusable(false);

        modoConexaoLabel.setText("Modo:");

        javax.swing.GroupLayout configuracaoPanelLayout = new javax.swing.GroupLayout(configuracaoPanel);
        configuracaoPanel.setLayout(configuracaoPanelLayout);
        configuracaoPanelLayout.setHorizontalGroup(
            configuracaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configuracaoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configuracaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configuracaoPanelLayout.createSequentialGroup()
                        .addComponent(modoConexaoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modoConexaoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(configuracaoPanelLayout.createSequentialGroup()
                        .addComponent(ordenacaoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ordenacaoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(configuracaoPanelLayout.createSequentialGroup()
                        .addComponent(leitorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(leitorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(configuracaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configuracaoPanelLayout.createSequentialGroup()
                        .addComponent(tempoDecorridoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tempoDecorridoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(configuracaoPanelLayout.createSequentialGroup()
                        .addComponent(tentativaLeituraLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tentativaLeituraTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(configuracaoPanelLayout.createSequentialGroup()
                        .addComponent(ipLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(61, 61, 61))
        );
        configuracaoPanelLayout.setVerticalGroup(
            configuracaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configuracaoPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(configuracaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leitorLabel)
                    .addComponent(leitorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ipLabel)
                    .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(configuracaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modoConexaoLabel)
                    .addComponent(modoConexaoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tempoDecorridoLabel)
                    .addComponent(tempoDecorridoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(configuracaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ordenacaoLabel)
                    .addComponent(ordenacaoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tentativaLeituraLabel)
                    .addComponent(tentativaLeituraTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        ativarButton.setText("Ativar");
        ativarButton.setFocusable(false);

        pausarButton.setText("Pausar");
        pausarButton.setFocusable(false);

        javax.swing.GroupLayout botoesPanelLayout = new javax.swing.GroupLayout(botoesPanel);
        botoesPanel.setLayout(botoesPanelLayout);
        botoesPanelLayout.setHorizontalGroup(
            botoesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botoesPanelLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(ativarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(pausarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lerEtiquetasButton, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
        );
        botoesPanelLayout.setVerticalGroup(
            botoesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botoesPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(botoesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ativarButton)
                    .addComponent(pausarButton)
                    .addComponent(lerEtiquetasButton))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(informacoesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addComponent(configuracaoPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(botoesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(configuracaoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informacoesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botoesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        ativarModoAtivo();
    }                 
}
