/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author vitorcezar
 */
public class AplicacaoTinyOS {
    
    public static void main( String[] args ) {
        InterfaceGrafica interfaceGrafica = new InterfaceGrafica();
        interfaceGrafica.mostreInterfaceGrafica();
    }
}