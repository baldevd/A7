package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OthelloWidget extends JPanel implements ActionListener, SpotListener {
	
	private enum Player {BLACK, WHITE};
	
	private JSpotBoard _board;		/* SpotBoard playing area. */
	private JLabel _message;		/* Label for messages. */
	private boolean _game_won;		/* Indicates if games was been won already.*/
	private boolean _draw;
	private Player _next_to_play;	/* Identifies who has next turn. */
	
	public OthelloWidget() {
		
		_board = new JSpotBoard(8,8);
		
		_message = new JLabel();
		 
		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);
		
		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());

		
		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_message_panel.add(reset_button, BorderLayout.EAST);
		reset_message_panel.add(_message, BorderLayout.CENTER);
		
		add(reset_message_panel, BorderLayout.SOUTH);
		
		_board.addSpotListener(this);

		resetGame();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/* Handles reset game button. Simply reset the game. */
				resetGame();
	}	
	
	private void resetGame() {
		/* Clear all spots on board. Uses the fact that SpotBoard
		 * implements Iterable<Spot> to do this in a for-each loop.
		 */

		for (Spot s : _board) {
			s.clearSpot();
		}

		/* Reset the background of the old secret spot.
		 * Check _secret_spot for null first because call to 
		 * resetGame from constructor won't have a secret spot 
		 * chosen yet.
		 */
	
		
		/* Reset game won and next to play fields */
		_game_won = false;
		_draw = false;
		_next_to_play = Player.BLACK;
		
		/* Display game start message. */
		
		_message.setText("Welcome to the Othello. Black to play");
	}


	@Override
	public void spotClicked(Spot s) {
		
		
		/* If game already won, do nothing. */
		if (_game_won) {
			return;
		}

		
		/* Set up player and next player name strings,
		 * and player color as local variables to
		 * be used later.
		 */
		
		String player_name = null;
		String next_player_name = null;
		Color player_color = null;
		
		if (_next_to_play == Player.BLACK) {
			player_color = Color.BLACK;
			player_name = "Black";
			next_player_name = "White";
			_next_to_play = Player.WHITE;
		} else {
			player_color = Color.WHITE;
			player_name = "White";
			next_player_name = "Black";
			_next_to_play = Player.BLACK;			
		}
		
				
		
		/* Set color of spot clicked and toggle. */
		
	
		/* Check if the game is over. */
		
		_game_won = false;
		_draw = false;
		

		/* Update the message depending on what happened.
		 * If spot is empty, then we must have just cleared the spot. Update message accordingly.
		 * If spot is not empty and the game is won, we must have
		 * just won. Calculate score and display as part of game won message.
		 * If spot is not empty and the game is not won, update message to
		 * report spot coordinates and indicate whose turn is next.
		 */
		
		if (_game_won) {
			_message.setText(player_name + " wins!");
		} else if (_draw) {
			_message.setText("Draw!");
		} else {
			_message.setText(next_player_name + " to play.");
		}
		
	}
	

	@Override
	public void spotEntered(Spot s) {
		/* Highlight spot if game still going on. */
		
		if (_game_won) {
			return;
		}
		if (!s.isEmpty()) {
			return;
		}
		s.highlightSpot();
	}

	@Override
	public void spotExited(Spot s) {
		
		/* Unhighlight spot. */
		s.unhighlightSpot();
	}
	
}

	