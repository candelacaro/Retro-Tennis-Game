package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

/**
 * Classe ball: Representa la pilota del joc.
 * S'encarrega de gestionar el seu moviment, la seva representació gràfica 
 * i les col·lisions amb les parets, la raqueta i els obstacles.
 * 
 * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Ball {

	//Declaració i inicialització d'atributs privats
	//Posició horitzontal de la pilota (eix de les absisses)
    private int x = 10;
    //Posició vertical de la pilota (eix de les ordenades)
    private int y = 10;
    //Direcció i velocitat base en l'eix X
    private int xVel = 1;
    //Direcció i velocitat base en l'eix Y
    private int yVel = 1;

    //Declaració i inicialització de final per la mida de la pilota
    private static final int DIAMETER = 30;

    //Instància d'objecte de la classe Game per interactuar amb el joc
    private Game game;

    //Declaració i inicialització d'atribut privat, aquest té control sobre la velocitat
    private double speed = 1.0;

    /**
     * Constructor, que inicialitza la pilota connectant-la amb la instància del joc actual.
     * @param game, instància de la classe Game que conté la pilota
     */
    public Ball(Game game) {
    		//Assignem el paràmetre a l'atribut de la classe
        this.game = game;
    }

    /**
     * Mètode que incrementa la velocitat de la pilota en un 10%.
     */
    public void increaseSpeed() {
    		//Multipliquem la velocitat actual per 1.10 (10%)
        speed *= 1.10;
    }

    /**
     * Mètode que, calcula el moviment de la pilota i gestiona les col·lisions
     * @param obstacles, Llista d'obstacles que hi ha a la pantalla
     */
    public void move(List<Obstacle> obstacles) {

    		/*Declaració i inicialització de variable int, aquesta calcula quina 
    		 * seria la següent posició abans d'aplicar-la*/
    		//En el eix de les absisses (eix X)
        int nextX = x + (int)(xVel * speed);
        //En el eix de les ordenades (eix Y)
        int nextY = y + (int)(yVel * speed);

        //Estructura condicional que gestiona les parets laterals del eix X.
        if (nextX < 0) {
        		
        		/**Si toca l'esquerra, forcem velocitat positiva (dreta)
        		 * Fem servir el mètode Math.abs, aquest torna el valor absolut 
        		 * d'un nombre, el que significa que converteix qualsevol nombre 
        		 * negatiu en el seu equivalent positiu, mentre que els nombres positius 
        		 * romanen igual.
        		 */
            xVel = Math.abs(xVel);
            //Posem la pilota al límit de la pantalla
            x = 0;
        } 
        
        /**
         * Si la posició predictiva de la part esquerra de la pilota és més gran que l'amplada de 
         * la pantalla menys el que mesura la pilota, significa que la vora dreta de la pilota està 
         * tocant o superant el límit de la finestra, per tant s'executa el següent:
         */
        else if (nextX > game.getWidth() - DIAMETER) {
        		//Si toca la dreta, forcem velocitat negativa (esquerra)
            xVel = -Math.abs(xVel); 
            //La col·loquem al límit dret
            x = game.getWidth() - DIAMETER; 
        } else {
        		//Si no toca paret, es mou normal
            x = nextX; 
        }

        //Estructura condicional que gestiona el sostre i el terra del eix Y
        if (nextY < 0) {
        		//Si toca el sostre, forcem velocitat positiva (avall)
            yVel = Math.abs(yVel); 
            //La posem al límit
            y = 0;
        } 
        /**
         * Si la posició predictiva de la part esquerra de la pilota és més gran que l'alçada de 
         * la pantalla menys el que mesura la pilota, significa que la vora del terra de la pilota està 
         * tocant o superant el límit de la finestra, per tant s'executa el següent:
         */
        else if (nextY > game.getHeight() - DIAMETER) {
        		//Cridem el mètode de la classe game
            game.gameOver();
        } else {
        		//Si no, es mou normal
            y = nextY;
        }

        /*Estructura condicional que gestiona la col·lisió de la raqueta,
         * amb el mètode 'intersects' és un mètode de Java que mira si dos rectangles es toquen.
         */
        if (game.racquet.getBounds().intersects(getBounds())) {
        		//S'executa el so que hem possat
            game.sonido.playGolpe(); 
            //Rebot cap a dalt (negatiu en l'eix Y)
            yVel = -Math.abs(yVel);
        }

        /**
         * Gestió de col·lisió amb osbtacles.
         * Recorrem tots els obstacles de la llista mitjançant una estructura iterativa FOR-EACH
         */
        for (Obstacle o : obstacles) {
        		/*Estructura condicional que gestiona la col·lisió de la raqueta,
             * amb el mètode 'intersects' és un mètode de Java que mira si dos rectangles es toquen.
             */
            if (o.getBounds().intersects(getBounds())) {
            		//Instància d'objectes rectangle per al càlcul de col·lisió
                Rectangle ballRect = getBounds();
                //Instància d'objectes rectangle per al càlcul de col·lisió amb l'obstacle
                Rectangle obsRect = o.getBounds();

                //Càlcul de la penetració de l'objecte
                int penetracioDreta = ballRect.x + ballRect.width - obsRect.x;
                int penetracioEsquerra = obsRect.x + obsRect.width - ballRect.x;
                int penetracioTerra = ballRect.y + ballRect.height - obsRect.y;
                int penetracioSostre = obsRect.y + obsRect.height - ballRect.y;

                /**
                 * Declaració i inicialització de variable que serveix per determinar 
                 * l'eix de col·lisió mitjançant el valor mínim de penetració
                 */
                int penetracioMinima = Math.min(Math.min(penetracioDreta, penetracioEsquerra), Math.min(penetracioSostre, penetracioTerra));

                //Estructura condicional on validem les penetracions de la pilota amb l'obstacle
                //Si penetracioMinima és igual a penetracioDreta s'executa
                if (penetracioMinima == penetracioDreta) {
                		//Forcem rebot a l'esquerra
                    xVel = -Math.abs(xVel); 
                    //Treiem la pilota de dins de l'obstacle
                    x = obsRect.x - ballRect.width; 
                } 
                //Si penetracioMinima és igual a penetracioEsquerra s'executa
                else if (penetracioMinima == penetracioEsquerra) {
                		//Forcem rebot a la dreta
                    xVel = Math.abs(xVel); 
                    //Correcció de la posició de la pilota cap a la dreta
                    x = obsRect.x + obsRect.width;
                } 
                //Si penetracioMinima és igual a penetracioTerra s'executa
                else if (penetracioMinima == penetracioTerra) {
                		//Forcem rebot cap a dalt
                    yVel = -Math.abs(yVel); 
                    //Treiem la pilota de dins de l'obstacle
                    y = obsRect.y - ballRect.height;
                } 
                //Si penetracioMinima és igual a penetracioSostre s'executa
                else if (penetracioMinima == penetracioSostre) {
                		//Forcem rebot cap a baix
                    yVel = Math.abs(yVel); 
                    //Correcció de la posició de la pilota 
                    y = obsRect.y + obsRect.height;
                }
                
                //Mentre que la pilota es copeja, es reprodueix el so playGolpe() 
                game.sonido.playGolpe(); 
            }
        }

        //Fem casting a int, de la velocitat de la pilota i l'increment a l'eix X
        x += (int)(xVel * speed);
        //Fem casting a int, de la velocitat de la pilota i l'increment a l'eix Y
        y += (int)(yVel * speed);
    }

    /**
     * Mètode que s'encarrega de dibuixar la pilota
     * @param g, gràfic que s'ha de mostrar
     */
    public void paint(Graphics2D g) {
    		//Amb mètode setColor, inserim un color per la bola
        g.setColor(Color.YELLOW);
        //Amb el mètode fillOval la mida de la bola 
        g.fillOval(x, y, DIAMETER, DIAMETER);
    }

    /**
     * Mètode Rectangle que controla els límits de la finestra
     * @return, retorna la finestra amb les seves mesures
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER, DIAMETER);
    }
}