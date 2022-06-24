/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matecombact;

/**
 *
 * @author Personal
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import matecombact.vistaMateCombat;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.Timer;


import java.net.*;
import java.util.Scanner;
import javax.swing.JLabel;

/**
 *
 * @author Personal
 */
public class Controlador1 implements  MouseListener, ActionListener{
    
    
    private final vistaMateCombat ventana; //Recibir el principal y mantener la encapsulacion
    boolean turno = true;
    int vidaJugadorBlanco = 100;
    HiloSocket servidor;
    int niveldeAtaque = 0;
    //JLabel label;
    //String respuesta = "soy servidor";
    
    
    

    
    
    public Controlador1(vistaMateCombat ventana) {
        
       
        this.ventana = ventana;
        
        oyentes();
        
        //this.ventana.setUndecorated(true);
        this.ventana.setAlwaysOnTop(true);
        this.ventana.setVisible(true);//Hacer vislible la venta
        this.ventana.setSize(new Dimension(669, 610));
        this.ventana.setResizable(false);
        
        
        
    }

    private void oyentes() {
        //Oyente para mouse     
       ventana.btnIniciar.addMouseListener(this);
       ventana.btnEnviarResultado.addMouseListener(this);
       ventana.btnHacerOperacion.addMouseListener(this);
      
        
        
        
    }
    private static boolean isNumeric(String cadena){
	try {
		Integer.parseInt(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
}
    

    @Override
    public void mouseClicked(MouseEvent e) {
        
        if(e.getSource()==ventana.btnIniciar){
            
            
            
            //HiloServer1 servidor = new HiloServer1(respuesta);
            HiloSocket servidor =  new HiloSocket(ventana.jlEsperando, ventana.lbTurno, ventana.jlSigno,
                            ventana.jlVidaNegro, ventana.pbPeleadorNegro,ventana.btnHacerOperacion,ventana.imgRes2,ventana.jlAtack);
            servidor.iniciar();
            ventana.jlEsperando.setText("Jugador 2_CONECTADO");
            ventana.btnHacerOperacion.setEnabled(true);
            ventana.btnIniciar.setEnabled(false);
    
        }else if(e.getSource()==ventana.btnHacerOperacion){
            if(ventana.jlVidaNegro.getText() == "0%"){
                ventana.lbTurno.setText("Has Perdido Esta Batalla!");
                ventana.imgRes2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/segundo.png")));
            }
            ventana.jlNumero1.setText(String.valueOf((int)(Math. random()*99+1)));
            ventana.jlNumero2.setText(String.valueOf((int)(Math. random()*99+1)));
            ventana.jlNumero1.setForeground(new java.awt.Color(0, 0, 255));
            ventana.jlNumero2.setForeground(new java.awt.Color(0, 0, 255));
            
            int operador = ((int)(Math. random()*4+1));
            System.out.println("El sino es: "+String.valueOf(operador));
            
            switch (operador) {
                case 1:
                    ventana.jlSigno.setText("+");
                    break;
                case 2:
                    ventana.jlSigno.setText("-");
                    break;
                case 3:
                    ventana.jlSigno.setText("*");
                    break;
                case 4:
                    ventana.jlSigno.setText("/");
                    break;
                default:
                    throw new AssertionError();
            }
            
            
            ventana.btnEnviarResultado.setEnabled(true);
            ventana.btnHacerOperacion.setEnabled(false);
            ventana.jlResultado.setText("???");
            
        
        }else if(e.getSource()==ventana.btnEnviarResultado){
            
            
            String respuestaPrueva = ventana.tfRespuesta.getText();
            System.out.println(respuestaPrueva);
            double res = 0;
            if(validatefloat( respuestaPrueva) ==true){
            
               double rsultadoOperacion;
                switch (ventana.jlSigno.getText()) {
                case "+":
                     rsultadoOperacion = Double.parseDouble(ventana.jlNumero1.getText()) + Double.parseDouble(ventana.jlNumero2.getText());
                     res = Math.round(rsultadoOperacion * 100.0) / 100.0 ;
                     niveldeAtaque = 10;
                    break;
                case "-":
                     rsultadoOperacion = Double.parseDouble(ventana.jlNumero1.getText()) - Double.parseDouble(ventana.jlNumero2.getText());
                     res = Math.round(rsultadoOperacion * 100.0) / 100.0 ;
                     niveldeAtaque = 10;
                    break;
                case "*":
                     rsultadoOperacion = Double.parseDouble(ventana.jlNumero1.getText()) * Double.parseDouble(ventana.jlNumero2.getText());
                     res = Math.round(rsultadoOperacion * 100.0) / 100.0 ;
                     niveldeAtaque = 15;
                    break;
                case "/":
                    //double roundOff = Math.round(a * 100.0) / 100.0;
                     rsultadoOperacion = Double.parseDouble(ventana.jlNumero1.getText()) / Double.parseDouble(ventana.jlNumero2.getText());
                     res = Math.round(rsultadoOperacion * 100.0) / 100.0 ;
                     niveldeAtaque = 20;
                    break;
                default:
                    throw new AssertionError();
                }
            
            

                double resUser = Double.parseDouble(ventana.tfRespuesta.getText());
                System.out.println(resUser);
                ventana.jlEsperando.setText("Es tu turno");

                if (res == resUser){

                    ventana.imgRes2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/bien.png")));
                    ventana.lbTurno.setText("Correcta");
                    vidaJugadorBlanco = vidaJugadorBlanco - niveldeAtaque;
                    ventana.pbPeleadorBlanco.setValue(vidaJugadorBlanco);
                    ventana.jlVidaBlanco.setText(String.valueOf(vidaJugadorBlanco)+"%");



                }else{
                    ventana.imgRes2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/mal.png")));
                    ventana.lbTurno.setText("Incorrecta");

                }

                ventana.jlResultado.setText(String.valueOf(res));
                ventana.jlResultado.setForeground(new java.awt.Color(0, 0, 255));
                ventana.btnEnviarResultado.setEnabled(false);
                ventana.btnHacerOperacion.setEnabled(true);

                if(vidaJugadorBlanco<=0){
                    ventana.lbTurno.setForeground(new java.awt.Color(204,0,0));
                    ventana.lbTurno.setText("Has Ganado Esta Batalla!");

                    ventana.imgRes2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/primero.png")));
                }
                //ventana.tfRespuesta.setText("-");

            }else{
              ventana.lbTurno.setText("Valor erroneo");
            }
        }
                  
            
            
            
        
        
    }
    
  
    
    

    @Override
    public void mousePressed(MouseEvent e) {
       
       
        
    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public boolean validatefloat(String cad){
        try
        {
          Float.parseFloat(cad);
          return true;
        }
        catch(NumberFormatException nfe)
        {
          return false;
        }
        }
    
   }

