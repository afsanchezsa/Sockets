/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Usuario9
 */
public class TableroCliente extends JPanel implements ActionListener, KeyListener{
    private Timer timer; 
    private ArrayList<Circulo> circulo;
    private Carro personajePrincipal;
    private Socket cliente ;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private int puntaje = 0;
   
    public TableroCliente(){
        try {
            this.setFocusable(true);
            this.addKeyListener(this);
            this.personajePrincipal = new Carro(100,200);
          
            this.circulo = new ArrayList<Circulo>();
            this.circulo.add(new Circulo(20,20));
            this.circulo.add(new Circulo(100,80));
            this.circulo.add(new Circulo(80,120));
            
            cliente=new Socket("localhost",8000);
            System.out.println("Me conecte a un servidor");
         
            this.salida = new DataOutputStream(cliente.getOutputStream());
            this.timer = new Timer(50, this);
            this.timer.start();
        } catch (IOException ex) {
            Logger.getLogger(TableroCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         for(Circulo c: this.circulo)
            c.dibujar(g,this);
         
         this.personajePrincipal.dibujar(g,this);
         g.drawString("Puntaje " + puntaje, 40, 40);
         
         
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        validarColisiones();
         for(Circulo c: this.circulo)
            c.mover();
            repaint();
        
    }
     
    
    public void validarColisiones(){
        Rectangle recPersonaje= this.personajePrincipal.obtenerRectangulo();
        ArrayList<Circulo> copia = (ArrayList<Circulo>) this.circulo.clone();
        for(Circulo c : circulo){
           Rectangle RecCir = c.obtenerRectangulo();
           if(recPersonaje.intersects(RecCir)){
               copia.remove(c);
               this.puntaje++;
           }
           this.circulo=copia;   
           
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
     
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo=-1;
        try {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            codigo=1;
        }

        if (key == KeyEvent.VK_LEFT) {
            codigo=1;
        }

        if (key == KeyEvent.VK_RIGHT) {
           codigo=2;
        }

        if (key == KeyEvent.VK_UP) {
           codigo=3;
        }

        if (key == KeyEvent.VK_DOWN) {
           codigo=4;
        }
        
         this.personajePrincipal.keyPressed(e);
         this.salida.writeInt(codigo);
         this.salida.flush();
        } catch (IOException ex) {
            Logger.getLogger(TableroCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }
}
