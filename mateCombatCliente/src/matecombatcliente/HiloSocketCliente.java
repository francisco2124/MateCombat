package matecombatcliente;

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


/**
 *
 * @author Ivanovich
 */
public class HiloSocketCliente extends Thread {

    String HOST = "localhost";
    static int PUERTO = 5000;
    Socket sc;
    DataOutputStream salida;
    DataInputStream entrada;
    String mensajeRecibido;
    
    int vidaJugadorBlanco = 100;
    int turno = 1;
    
    JLabel label;
    JLabel vidaBlanco;
    JLabel labelip;
    JLabel resultadoIC;
    JProgressBar jugador1;
    JProgressBar jugador2;
    JButton btnOperacion;
    JLabel imgAtck;
    JLabel labelImagen;
    JLabel operador;
    
    private Thread T;
    

    public HiloSocketCliente(String HOST,JLabel label, JLabel operador, JLabel labelip, JLabel vidaBlanco, JProgressBar jugador1,JButton btnOperacion, JLabel labelImagen, JLabel imgAtck) {
        this.label = label;
        this.vidaBlanco = vidaBlanco;
        this.resultadoIC = resultadoIC;
        this.labelip = labelip;
        this.jugador1 = jugador1;
        this.btnOperacion = btnOperacion;
        this.HOST = HOST;
        this.imgAtck = imgAtck;
        this. labelImagen = labelImagen;
        this.operador = operador;
    }

    @Override
    public void run() {
        try {
            sc = new Socket(HOST, PUERTO);
            salida = new DataOutputStream(sc.getOutputStream());
            entrada = new DataInputStream(sc.getInputStream());
            labelip.setText("Conexión Exitosa");
            label.setText("Conectado con jugador 1");
            System.out.println("Por iniciar turnos");
            label.setText("Es tu turno");
            
            iniciarWhile();
            

        } catch (Exception e) {
        }
    }
public void iniciarWhile() throws SocketException, UnknownHostException, IOException, InterruptedException {
        Scanner Teclado = new Scanner(System.in);
        DatagramSocket clienteSocket = new DatagramSocket();
        //DatagramSocket serversokect = new DatagramSocket(9090);
        //InetAddress[] IpAddress = InetAddress.getAllByName("localhost"); //server
        InetAddress IpAdress = InetAddress.getByName(HOST);
        DatagramSocket serversokect = new DatagramSocket(135);
        byte DatosEnviar[];
        boolean stop = false; 
        String CadenaEnviar = "0";
        int setTurno = 0;
        
        while (true) {
            //System.out.println("Entre al while");
            if(turno == 1){
                btnOperacion.setEnabled(true);
            }else{
                btnOperacion.setEnabled(false);
            }
            
            if (turno == 1) {
                
                label.setText("Es tu turno");
                
                DatosEnviar = new byte[1024];
                System.out.println("Enviando respuesta: ");
                //CadenaEnviar = Teclado.nextLine(); //preparar para enviar
                CadenaEnviar = labelip.getText();
                System.out.println("La cadena tiene: "+ CadenaEnviar);
                if (CadenaEnviar=="Correcta") {
                    
                    if(operador.getText() == "+" || operador.getText() == "-"){
                        CadenaEnviar = "C";
                    }else if(operador.getText() == "*"){
                        CadenaEnviar = "M";
                        
                    }else if(operador.getText() == "/")
                        CadenaEnviar = "D";
                    
                    
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));

                   DatosEnviar = CadenaEnviar.getBytes(); //Convertir a bytes los datos
                   DatagramPacket PaqueteSalida = new DatagramPacket(DatosEnviar, DatosEnviar.length, IpAdress, 9090);
                   clienteSocket.send(PaqueteSalida);
                   labelip.setText("Esperando"); 
                   turno = 0;
                   
                }else if(CadenaEnviar=="Incorrecta"){
                   DatosEnviar = CadenaEnviar.getBytes(); //Convertir a bytes los datos
                   DatagramPacket PaqueteSalida = new DatagramPacket(DatosEnviar, DatosEnviar.length, IpAdress, 9090);
                   clienteSocket.send(PaqueteSalida);
                   labelip.setText("Esperando"); 
                   turno = 0;
                }else if(CadenaEnviar == "Has Ganado Esta Batalla!"){
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/gato.png")));
                    Thread.sleep(4000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                    
                   DatosEnviar = CadenaEnviar.getBytes(); //Convertir a bytes los datos
                   DatagramPacket PaqueteSalida = new DatagramPacket(DatosEnviar, DatosEnviar.length, IpAdress, 9090);
                   clienteSocket.send(PaqueteSalida);
                   turno = 2;
                }else{
                    Thread.sleep(2000);
                    System.out.println("Esperando respuesta");
                }
                
                
                
                
                
            } else if (turno == 0) {
                
                label.setText("Turno del otro jugador");
                System.out.println("Escuchando...");
                byte DatosRecibir[];
                DatosRecibir = new byte[1024];
                System.out.println("\nEsperando....");
                DatagramPacket paqueteRecibido = new DatagramPacket(DatosRecibir, DatosRecibir.length);
                serversokect.receive(paqueteRecibido);

                String DatosRecibidos = new String(paqueteRecibido.getData());
                System.out.println("Recibiendo: "+DatosRecibidos);
                
                
                if(DatosRecibidos.charAt(0) == 'C'){
                    
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego2.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    System.out.println("Es correcto");
                    vidaJugadorBlanco = vidaJugadorBlanco -10;
                    jugador1.setValue(vidaJugadorBlanco);
                    vidaBlanco.setText(String.valueOf(vidaJugadorBlanco)+"%");
                    labelip.setText("Tu oponente Acerto");
                }else if(DatosRecibidos.charAt(0) == 'M'){
                    
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego2.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    System.out.println("Es correcto");
                    vidaJugadorBlanco = vidaJugadorBlanco -15;
                    jugador1.setValue(vidaJugadorBlanco);
                    vidaBlanco.setText(String.valueOf(vidaJugadorBlanco)+"%");
                    labelip.setText("Tu oponente Acerto");
                    
                }else if(DatosRecibidos.charAt(0) == 'D'){
                    
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego2.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    System.out.println("Es correcto");
                    vidaJugadorBlanco = vidaJugadorBlanco -20;
                    jugador1.setValue(vidaJugadorBlanco);
                    vidaBlanco.setText(String.valueOf(vidaJugadorBlanco)+"%");
                    labelip.setText("Tu oponente Acerto");
                    
                }else if(DatosRecibidos.charAt(0) == 'I'){
                    System.out.println("Es incorrecta");
                    labelip.setText("Tu oponente Fallo");
                }else if(DatosRecibidos.charAt(0) == 'H'){
                    
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/gato2.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                    
                    System.out.println("Has perdido");
                    labelip.setForeground(new java.awt.Color(204,0,0));
                    vidaBlanco.setText("0%");
                    jugador1.setValue(0);
                    labelip.setText("Has Perdido La Batalla");
                    labelImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/segundo.png")));
                    setTurno = 1;
                    
                }
                if (setTurno == 0){
                    turno = 1;
                }else if(setTurno == 1){
                    turno = 2;
                }
                
                
            }else if (turno == 2){
                btnOperacion.setEnabled(false);
                label.setText("Fin del Juego");
                System.out.println("Juego Terminado");
                Thread.sleep(10000);
            }
        }
    }
public void iniciar(){
        
          
            if (T == null) { // Si no ha iniciado, si no existe 
                T = new Thread(this);
                T.start();           
        }
    }

/*public void setTurno(boolean valor){
    
    if(valor == true){
        turno = 1;
    }
}*/

}


