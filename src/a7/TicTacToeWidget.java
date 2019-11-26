package a7;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeWidget extends JPanel implements ActionListener, SpotListener {

	/* Enum to identify player. */
	
	private enum Player {WHITE, BLACK};
	
	private JSpotBoard _board;		/* SpotBoard playing area. */
	private JLabel _message;		/* Label for messages. */
	private boolean _game_won;		/* Indicates if games was been won already.*/
	private boolean _draw;
	private Player _next_to_play;	/* Identifies who has next turn. */
	
	public TicTacToeWidget() {
		
		/* Create SpotBoard and message label. */
		
		_board = new JSpotBoard();
		_message = new JLabel();
		
		/* Set layout and place SpotBoard at center. */
		
		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);

		/* Create subpanel for message area and reset button. */
		
		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());

		/* Reset button. Add ourselves as the action listener. */
		
		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_message_panel.add(reset_button, BorderLayout.EAST);
		reset_message_panel.add(_message, BorderLayout.CENTER);

		/* Add subpanel in south area of layout. */
		
		add(reset_message_panel, BorderLayout.SOUTH);

		/* Add ourselves as a spot listener for all of the
		 * spots on the spot board.
		 */
		
		_board.addSpotListener(this);
		
		_board.getSpotAt(3, 3).setSpotColor(Color.WHITE);

		/* Reset game. */
		resetGame();
	}

	/* resetGame
	 * 
	 * Resets the game by clearing all the spots on the board,
	 * picking a new secret spot, resetting game status fields, 
	 * and displaying start message.
	 * 
	 */

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
		_next_to_play = Player.WHITE;
		
		/* Display game start message. */
		
		_message.setText("Welcome to the Tic Tac Toe. White to play");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Handles reset game button. Simply reset the game. */
				resetGame();
	}

	/* Implementation of SpotListener below. Implements game
	 * logic as responses to enter/exit/click on spots.
	 */
	
	@Override
	public void spotClicked(Spot s) {
		
		/* If game already won, do nothing. */
		if (_game_won) {
			return;
		}
		
		/* If spot is already filled, do nothing. */
		if (!s.isEmpty()) {
			return;
		}

		/* Set up player and next player name strings,
		 * and player color as local variables to
		 * be used later.
		 */
		
		String player_name = null;
		String next_player_name = null;
		Color player_color = null;
		
		if (_next_to_play == Player.WHITE) {
			player_color = Color.WHITE;
			player_name = "White";
			next_player_name = "Black";
			_next_to_play = Player.BLACK;
		} else {
			player_color = Color.BLACK;
			player_name = "Black";
			next_player_name = "White";
			_next_to_play = Player.WHITE;			
		}
		
				
		
		/* Set color of spot clicked and toggle. */
		
		s.setSpotColor(player_color);
		s.toggleSpot();
		
	
		/* Check if the game is over. */
		
		_game_won = gameWon(s);
		_draw = draw();
		

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

	private boolean gameWon(Spot s) {
		int x = s.getSpotX();
		int y = s.getSpotY();
		Color color = s.getSpotColor();
		
		Spot a = _board.getSpotAt(0, 0);
		Spot b = _board.getSpotAt(1, 0);
		Spot c = _board.getSpotAt(2, 0);
		Spot d = _board.getSpotAt(0, 1);
		Spot e = _board.getSpotAt(1, 1);
		Spot f = _board.getSpotAt(2, 1);
		Spot g = _board.getSpotAt(0, 2);
		Spot h = _board.getSpotAt(1, 2);
		Spot i = _board.getSpotAt(2, 2);
		
		if (x == 0 && y == 0) {
			if (!(b.isEmpty() || c.isEmpty())) {
				return color.equals(b.getSpotColor()) && color.equals(c.getSpotColor());
			}
			if (!(d.isEmpty() || g.isEmpty())) {
				return color.equals(d.getSpotColor()) && color.equals(g.getSpotColor());
			}
			if (!(e.isEmpty() || i.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(i.getSpotColor());
			}
		}
		
		if (x == 1 && y == 0) {
			if (!(a.isEmpty() || c.isEmpty())) {
				return color.equals(a.getSpotColor()) && color.equals(c.getSpotColor());
			}
			if (!(e.isEmpty() || h.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(h.getSpotColor());
			}
		}
		
		if (x == 2 && y == 0) {
			if (!(a.isEmpty() || b.isEmpty())) {
				return color.equals(a.getSpotColor()) && color.equals(b.getSpotColor());
			}
			if (!(f.isEmpty() || i.isEmpty())) {
				return color.equals(f.getSpotColor()) && color.equals(i.getSpotColor());
			}
			if (!(e.isEmpty() || g.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(g.getSpotColor());
			}
		}
		
		if (x == 0 && y == 1) {
			if (!(a.isEmpty() || g.isEmpty())) {
				return color.equals(a.getSpotColor()) && color.equals(g.getSpotColor());
			}
			if (!(e.isEmpty() || f.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(f.getSpotColor());
			}
		}
		
		if (x == 1 && y == 1) {
			if (!(a.isEmpty() || i.isEmpty())) {
				return color.equals(a.getSpotColor()) && color.equals(i.getSpotColor());
			}
			if (!(c.isEmpty() || g.isEmpty())) {
				return color.equals(c.getSpotColor()) && color.equals(g.getSpotColor());
			}
			if (!(b.isEmpty() || h.isEmpty())) {
				return color.equals(b.getSpotColor()) && color.equals(h.getSpotColor());
			}
			if (!(d.isEmpty() || f.isEmpty())) {
				return color.equals(d.getSpotColor()) && color.equals(f.getSpotColor());
			}
		}
		
		if (x == 2 && y == 1) {
			if (!(c.isEmpty() || i.isEmpty())) {
				return color.equals(c.getSpotColor()) && color.equals(i.getSpotColor());
			}
			if (!(e.isEmpty() || d.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(d.getSpotColor());
			}
		}
		
		if (x == 0 && y == 2) {
			if (!(d.isEmpty() || a.isEmpty())) {
				return color.equals(d.getSpotColor()) && color.equals(a.getSpotColor());
			}
			if (!(e.isEmpty() || c.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(c.getSpotColor());
			}
			if (!(h.isEmpty() || i.isEmpty())) {
				return color.equals(h.getSpotColor()) && color.equals(i.getSpotColor());
			}
		}
		
		if (x == 1 && y == 2) {
			if (!(g.isEmpty() || i.isEmpty())) {
				return color.equals(g.getSpotColor()) && color.equals(i.getSpotColor());
			}
			if (!(e.isEmpty() || b.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(b.getSpotColor());
			}
		}
		
		if (x == 2 && y == 2) {
			if (!(e.isEmpty() || a.isEmpty())) {
				return color.equals(e.getSpotColor()) && color.equals(a.getSpotColor());
			}
			if (!(h.isEmpty() || g.isEmpty())) {
				return color.equals(h.getSpotColor()) && color.equals(g.getSpotColor());
			}
			if (!(f.isEmpty() || c.isEmpty()) ) {
				return color.equals(f.getSpotColor()) && color.equals(c.getSpotColor());
			}
		}
	
		return false;
		
	}
	
	private boolean draw() {
		for (Spot s: _board) {
			if (s.isEmpty()) {
				return false;
			}
		}
		return true;
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