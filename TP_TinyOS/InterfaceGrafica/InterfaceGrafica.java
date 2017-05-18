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
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author vitorcezar
 */
public class InterfaceGrafica extends JFrame {
                       
    private JButton ativarButton;
    private JPanel graficoPanel;
    private JPanel painelModoLeituraInformacoes;
    private JPanel painelBotoes;
    private JButton lerDadosButton;
    private JComboBox< String > modoLeituraComboBox;
    private JLabel modoLeituraLabel;
    private JButton mostrarDadosButton;
    private JButton pausarButton;
    private final static double HEIGHT_REDIMENSION = 0.8;
    
    /**
     * construtor da interface gráfica
     */
    public InterfaceGrafica() {
        super( "Interface TinyOS" );
        inicializaComponentes();
        ativarModoAutonomo();
        adicionaListeners();
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
     * modo ativo, em que o usuário deve mandar o programa ler as etiquetas
     */
    private void ativarModoAutonomo() {
        ativarButton.setEnabled( true );
        pausarButton.setEnabled( true );
        lerDadosButton.setEnabled( false );
    }
    
    /**
     * modo autônomo, em que o programa lê as etiquetas automaticamente
     */
    private void ativarModoAtivo() {
        ativarButton.setEnabled( false );
        pausarButton.setEnabled( false );
        lerDadosButton.setEnabled( true );
    }
    
    /**
     * adiciona os listeners dos componentes
     */
    private void adicionaListeners() {
        modoLeituraComboBox.addItemListener( ( e ) -> {
            if( modoLeituraComboBox.getSelectedIndex() == 0 ) {
                ativarModoAutonomo();
            } else {
                ativarModoAtivo();
            }
        } );
        ativarButton.addActionListener( ( e ) -> {
            adicionaGrafico();
        } );
        mostrarDadosButton.addActionListener( ( e ) -> {
            new TabelaInformacoes().mostrarInformacoesTabela();
        } );
    }
    
    /**
     * ler a topologia da memória secundária e desenha o gráfico
     */
    private void adicionaGrafico() {
        DirectedSparseGraph g = new DirectedSparseGraph();
        g.addVertex("Vertex1");
        g.addVertex("Vertex2");
        g.addVertex("Vertex3");
        g.addVertex("Vertex4");
        g.addVertex("Vertex5");
        g.addEdge("Edge1", "Vertex1", "Vertex2");
        g.addEdge("Edge2", "Vertex1", "Vertex3");
        g.addEdge("Edge3", "Vertex3", "Vertex1");
        g.addEdge("Edge4", "Vertex2", "Vertex4");
        g.addEdge("Edge5", "Vertex1", "Vertex5" );
        g.addEdge("Edge6", "Vertex2", "Vertex5" );
        g.addEdge("Edge7", "Vertex3", "Vertex5" );
        g.addEdge("Edge8", "Vertex4", "Vertex5" );
        
        Transformer<String, String> transformer = ( String arg ) -> arg;
        
        Dimension dimension = graficoPanel.getSize();
        VisualizationViewer visualizationViewer = new VisualizationViewer( 
            new CircleLayout( g ), new Dimension( 
                ( int ) dimension.getWidth(), 
                ( int ) ( dimension.getHeight() * HEIGHT_REDIMENSION )
            )
        );
        visualizationViewer.setBackground( Color.WHITE );
        visualizationViewer.getRenderContext().setVertexLabelTransformer( 
            transformer 
        );
        graficoPanel.setLayout( new BorderLayout() );
        graficoPanel.add( visualizationViewer );
        graficoPanel.revalidate();
    }
    
    /**
     * função gerada pelo NetBeans para inicializar os componentes
     */                        
    private void inicializaComponentes() {

        painelModoLeituraInformacoes = new javax.swing.JPanel();
        modoLeituraLabel = new javax.swing.JLabel();
        modoLeituraComboBox = new javax.swing.JComboBox<>();
        mostrarDadosButton = new javax.swing.JButton();
        graficoPanel = new javax.swing.JPanel();
        graficoPanel.setBackground( Color.WHITE );
        painelBotoes = new javax.swing.JPanel();
        ativarButton = new javax.swing.JButton();
        pausarButton = new javax.swing.JButton();
        lerDadosButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        modoLeituraLabel.setText("Modo leitura:");

        modoLeituraComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Autônomo", "Ativo" }));
        modoLeituraComboBox.setFocusable(false);

        mostrarDadosButton.setText("Mostrar dados");
        mostrarDadosButton.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(painelModoLeituraInformacoes);
        painelModoLeituraInformacoes.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modoLeituraLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modoLeituraComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mostrarDadosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modoLeituraLabel)
                    .addComponent(modoLeituraComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mostrarDadosButton))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout graficoPanelLayout = new javax.swing.GroupLayout(graficoPanel);
        graficoPanel.setLayout(graficoPanelLayout);
        graficoPanelLayout.setHorizontalGroup(
            graficoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        graficoPanelLayout.setVerticalGroup(
            graficoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 349, Short.MAX_VALUE)
        );

        ativarButton.setText("Ativar");
        ativarButton.setFocusable(false);

        pausarButton.setText("Pausar");
        pausarButton.setFocusable(false);

        lerDadosButton.setText("Ler dados");
        lerDadosButton.setFocusable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(painelBotoes);
        painelBotoes.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(ativarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pausarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                .addComponent(lerDadosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ativarButton)
                    .addComponent(pausarButton)
                    .addComponent(lerDadosButton))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelModoLeituraInformacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(painelBotoes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graficoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelModoLeituraInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graficoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(painelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>                        
          
}
