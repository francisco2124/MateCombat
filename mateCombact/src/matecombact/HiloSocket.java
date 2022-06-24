package matecombact;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
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
public class HiloSocket extends Thread {

    static int PUERTO = 5000;
    ServerSocket sc;
    Socket so;
    DataOutputStream salida;
    DataInputStream entrada;
    int respuesta;
    int vidaJugadorNegro = 0;
    JLabel vidaNegro;
    int turno;
    JLabel label;
    JLabel labelImagen;
    JLabel labelip;
    JProgressBar jugador1;
    JProgressBar jugador2;
    JButton btnOperacion;
    JLabel imgAtck;
    JLabel operador;
    
    
    
    private Thread T;

    public HiloSocket(JLabel label, JLabel labelip,JLabel operador,JLabel vidaNegro, JProgressBar jugador1,JButton btnOperacion,JLabel labelImagen,JLabel imgAtck) {
        this.label = label;
        this.labelip = labelip;
        this.jugador1 = jugador1;
        this.vidaNegro = vidaNegro;
        this.btnOperacion = btnOperacion;
        this.labelImagen =labelImagen;
        this.imgAtck =imgAtck;
        this.operador = operador;
    }

    @Override
    public void run() {
        try {
            sc = new ServerSocket(PUERTO);
            so = new Socket();
            System.out.println("Esperando...");
            label.setText("Esperando al otro jugador");
            try {
                InetAddress ip = InetAddress.getLocalHost();
                labelip.setText("IP:" + ip.getHostAddress().toString());
            } catch (Exception e) {
            }
            so = sc.accept();
            entrada = new DataInputStream(so.getInputStream());
            salida = new DataOutputStream(so.getOutputStream());
            label.setText("Se conecto el otro jugador");
            
            System.out.println("Preparando mensaje");
            
            iniciarWhile();
            

        } catch (Exception e) {
        }
    }
    
    public void iniciar(){
        
          
            if (T == null) { // Si no ha iniciado, si no existe 
                T = new Thread(this);
                T.start();           
        }
    }
    
    public void iniciarWhile() throws SocketException, UnknownHostException, IOException, InterruptedException {
        Scanner Teclado = new Scanner(System.in);
        DatagramSocket serversokect = new DatagramSocket(9090);
        byte DatosRecibir[];
        byte DatosEnviar[];
        DatagramSocket clienteSocket = new DatagramSocket();
        InetAddress IpAdress = InetAddress.getByName("");
        String CadenaEnviar = "Soy Servidor";
        turno =0;
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
                    
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego2.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                   DatosEnviar = CadenaEnviar.getBytes(); //Convertir a bytes los datos
                    System.out.println("Recupere esta ip: "+IpAdress);
                   DatagramPacket PaqueteSalida = new DatagramPacket(DatosEnviar, DatosEnviar.length, IpAdress, 135);
                   clienteSocket.send(PaqueteSalida);
                   labelip.setText("Esperando"); 
                   turno = 0;
                }else if(CadenaEnviar=="Incorrecta"){
                    
                   DatosEnviar = CadenaEnviar.getBytes(); //Convertir a bytes los datos
                   DatagramPacket PaqueteSalida = new DatagramPacket(DatosEnviar, DatosEnviar.length, IpAdress, 135);
                   clienteSocket.send(PaqueteSalida);
                   labelip.setText("Esperando"); 
                   turno = 0;
                    
                }else if(CadenaEnviar == "Has Ganado Esta Batalla!"){
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/gato2.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                    
                   DatosEnviar = CadenaEnviar.getBytes(); //Convertir a bytes los datos
                   DatagramPacket PaqueteSalida = new DatagramPacket(DatosEnviar, DatosEnviar.length, IpAdress, 135);
                   clienteSocket.send(PaqueteSalida);
                   turno = 2;
                
                }else{
                    Thread.sleep(2000);
                    System.out.println("Esperando respuesta");
                }
                
                
            } else if (turno == 0) {
                
                label.setText("Turno del otro jugador");
                System.out.println("Escuchando...");
                DatosRecibir = new byte[1024];
                System.out.println("\nEsperando....");
                DatagramPacket paqueteRecibido = new DatagramPacket(DatosRecibir, DatosRecibir.length);
                serversokect.receive(paqueteRecibido);
                IpAdress = paqueteRecibido.getAddress();

                String DatosRecibidos = new String(paqueteRecibido.getData());
                System.out.println("Recibiendo: "+DatosRecibidos);
                char[] aCaracteres = DatosRecibidos.toCharArray();
                
                
                if(DatosRecibidos.charAt(0) == 'C'){
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                    System.out.println("Es Correcta");
                    vidaJugadorNegro = vidaJugadorNegro +10;
                    jugador1.setValue(0 + vidaJugadorNegro);
                    vidaNegro.setText(String.valueOf(100 - vidaJugadorNegro)+"%");
                    labelip.setText("Tu oponente Acerto");
                    turno =1;
                }else if(DatosRecibidos.charAt(0) == 'M'){
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                    System.out.println("Es Correcta");
                    vidaJugadorNegro = vidaJugadorNegro +15;
                    jugador1.setValue(0 + vidaJugadorNegro);
                    vidaNegro.setText(String.valueOf(100 - vidaJugadorNegro)+"%");
                    labelip.setText("Tu oponente Acerto");
                    turno =1;
                }else if(DatosRecibidos.charAt(0) == 'D'){
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fuego.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                    System.out.println("Es Correcta");
                    vidaJugadorNegro = vidaJugadorNegro +20;
                    jugador1.setValue(vidaJugadorNegro);
                    vidaNegro.setText(String.valueOf(100 - vidaJugadorNegro)+"%");
                    labelip.setText("Tu oponente Acerto");
                    turno =1;
                    
                }else if(DatosRecibidos.charAt(0) == 'I'){
                    System.out.println("Es incorrecta");
                    labelip.setText("Tu oponente Fallo");
                    turno =1;
                }else if(DatosRecibidos.charAt(0) == 'H'){
                    
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/gato.png")));
                    Thread.sleep(2000);
                    imgAtck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guantes.png")));
                    
                    System.out.println("Has perdido");
                    labelip.setForeground(new java.awt.Color(204,0,0));
                    vidaNegro.setText("0%");
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
                
                
                //labelip.setText(DatosRecibidos);
                
                
            }else if (turno == 2){
                btnOperacion.setEnabled(false);
                label.setText("Fin del Juego");
                System.out.println("Juego Terminado");
                Thread.sleep(10000);
            }
        }
    }
    
    public void setTurno(boolean valor){
    
    if(valor == true){
        turno = 1;
    }
}

}
