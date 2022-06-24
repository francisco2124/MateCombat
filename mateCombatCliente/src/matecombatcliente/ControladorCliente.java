/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matecombatcliente;

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

import matecombatcliente.vistaMateCombat;
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
public class ControladorCliente implements  MouseListener, ActionListener{
    
    
    private final vistaMateCombat ventana; //Recibir el principal y mantener la encapsulacion
    boolean turno = true;
    int vidaJugadorNegro = 0;
    int vidaJugadorBlanco = 0;
    int niveldeAtaque = 0;
    //JLabel label;
    //String respuesta = "soy servidor";
    HiloSocketCliente servidor;
    
    
    

    
    
    public ControladorCliente(vistaMateCombat ventana) {
        
       
        this.ventana = ventana;
        
        oyentes();
        
        //this.ventana.setUndecorated(true);
        this.ventana.setAlwaysOnTop(true);
        this.ventana.setVisible(true);//Hacer vislible la venta
        this.ventana.setSize(new Dimension(650, 610));
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
            
            
            String ip = ventana.tfIp.getText();
            if (validate(ip)== true){
                //HiloServer1 servidor = new HiloServer1(respuesta);

                HiloSocketCliente servidor =  new HiloSocketCliente(ip,ventana.jlEsperando,ventana.jlSigno, 
                                ventana.lbTurno,ventana.jlVidaBlanco,ventana.pbPeleadorBlanco,ventana.btnHacerOperacion,ventana.imgRes2,ventana.jlAtack);
                servidor.iniciar();
                ventana.jlEsperando.setText("Jugador 2_CONECTADO");
                ventana.btnHacerOperacion.setEnabled(true);
                ventana.btnIniciar.setEnabled(false);
            }else{
                ventana.lbTurno.setText("Ip Incorrecta");
            }
            
            
            
    
        }else if(e.getSource()==ventana.btnHacerOperacion){
            
            if(ventana.jlVidaBlanco.getText() == "0%"){
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
            double res;
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
                
                
                System.out.println(res);
                double resUser = Double.parseDouble(ventana.tfRespuesta.getText());
                System.out.println("El resutado es: "+resUser);

                if (res == resUser){

                    ventana.imgRes2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/bien.png")));
                    ventana.lbTurno.setText("Correcta");

                    vidaJugadorNegro = vidaJugadorNegro + niveldeAtaque;
                    ventana.pbPeleadorNegro.setValue(0+vidaJugadorNegro);
                    ventana.jlVidaNegro.setText(String.valueOf(100-vidaJugadorNegro)+"%");

                }else{
                    ventana.imgRes2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/mal.png")));
                    ventana.lbTurno.setText("Incorrecta");

                }

                ventana.jlResultado.setText(String.valueOf(res));
                ventana.jlResultado.setForeground(new java.awt.Color(0, 0, 255));
                ventana.btnEnviarResultado.setEnabled(false);
                ventana.btnHacerOperacion.setEnabled(true);


                if(vidaJugadorNegro >= 100){
                    ventana.lbTurno.setForeground(new java.awt.Color(204,0,0));
                    ventana.lbTurno.setText("Has Ganado Esta Batalla!");

                    ventana.imgRes2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/primero.png")));
                }
            
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
    
    public static boolean validate(final String ip) {
    String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    return ip.matches(PATTERN);
    }
    
    /*public static boolean validatefloat(final String num) {
        String PATTERN = "[+-]?([0-9]*[.])?[0-9]+";
    return num.matches(PATTERN);
    }*/
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

