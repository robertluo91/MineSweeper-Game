package minesweeper.server;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import minesweeper.BoardSquare.Board;
import minesweeper.BoardSquare.Square.boomException;

/**
 * please refer to the main thread (MinesweeperServer.java) for thread safety statement
 */


public class MinesweeperServerThread extends Thread {
    private final Socket socket;
    private final Board board;
    private final boolean debug;
    private final MinesweeperServer server;
    
    public MinesweeperServerThread(MinesweeperServer server, Socket socket, Board board, boolean debug) {
    	// constructor
        this.socket = socket;
        this.board = board;
        this.server = server;
        this.debug = debug;
        
        }
    
    public void run(){  
        try {
            handleConnection(socket);           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Handle a single client connection and return when client disconnects.
     * @param socket socket where the client is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
        BufferedReader readin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
        	server.increaseNumPlayers();
            out.println("Welcome to Minesweeper.  "+server.getNumPlayers()+ " people are playing including you.  Type 'help' for help.");
            for (String line =readin.readLine(); line!=null; line=readin.readLine()) {
                String output = handleRequest(line);
                if(output != null) {
                    out.println(output);
                }
                if (output==boomException.message && debug==false){
					// once a bomb is dug and DEBUG flag is false, break out of the input buffer
                    throw new closureException();
                }
            }
        } 
        catch(closureException b){} /*the precondition for disconnect is satisfied*/
        finally {        
            out.close();
            readin.close();
            server.decreaseNumPlayers();
        }
    }
    
    /**
     * Handler for client input. Make requested mutations on game state if applicable, then return 
     * appropriate message to the user.
     * 
     * @param input a string from a client
     * @return appropriate message
     * @throws closureException 
     */
    private String handleRequest(String input) throws closureException{
        String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|" +
                "(deflag \\d+ \\d+)|(help)|(bye)|(x)";
        if(!input.matches(regex)) {        
            return null;//invalid input type
        }
        
        String[] tokens = input.split(" ");
        System.out.println(input);
        if (tokens[0].equals("look")) {
            return board.toString();
        } else if (tokens[0].equals("help")) {
        	String message =   
        			"MESSAGE     :== ( LOOK | DIG | FLAG | DEFLAG | HELP_REQ | BYE ) NEWLINE"+
                    "LOOK        :== 'look'"+
                    "DIG         :== 'dig' SPACE X SPACE Y"+
                    "FLAG        :== 'flag' SPACE X SPACE Y"+
                    "DEFLAG      :== 'deflag' SPACE X SPACE Y"+
                    "HELP_REQ    :== 'help'"+
                    "BYE         :== 'bye'";        		
        			       
            return message;
        } else if (tokens[0].equals("bye")) {
            throw new closureException();
        } else {
            int y = Integer.parseInt(tokens[1]);
            int x = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                return board.dig(x, y);
            } else if (tokens[0].equals("flag")) {
                return board.flag(x, y);
            } else if (tokens[0].equals("deflag")) {
                return board.deflag(x, y);
            }
        }
        //throw exception if the program gets to this step
        throw new UnsupportedOperationException();

    }
    
    @SuppressWarnings("serial")  
    private static class closureException extends Exception{};

}