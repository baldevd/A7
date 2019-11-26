package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectFourWidget extends JPanel implements ActionListener, SpotListener {
	
	private enum Player {RED, BLACK};
	
	private JSpotBoard _board;		/* SpotBoard playing area. */
	private JLabel _message;		/* Label for messages. */
	private boolean _game_won;		/* Indicates if games was been won already.*/
	private boolean _draw;
	private Player _next_to_play;	/* Identifies who has next turn. */
	
	public ConnectFourWidget() {
		
		_board = new JSpotBoard("ConnectFour");
		
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
		_next_to_play = Player.RED;
		
		/* Display game start message. */
		
		_message.setText("Welcome to the Connect Four. Red to play");
	}


	@Override
	public void spotClicked(Spot s) {
		Spot spotToToggle;
		int columnClicked = s.getSpotX();
		boolean gameWon = false;
		
		/* If game already won, do nothing. */
		if (_game_won) {
			return;
		}

		/* If column is already filled, do nothing. */
		if (fullColumn(s)) {
			return;
		}
		
		/* Set up player and next player name strings,
		 * and player color as local variables to
		 * be used later.
		 */
		
		String player_name = null;
		String next_player_name = null;
		Color player_color = null;
		
		if (_next_to_play == Player.RED) {
			player_color = Color.RED;
			player_name = "Red";
			next_player_name = "Black";
			_next_to_play = Player.BLACK;
		} else {
			player_color = Color.BLACK;
			player_name = "Black";
			next_player_name = "Red";
			_next_to_play = Player.RED;			
		}
		
				
		
		/* Set color of spot clicked and toggle. */
		spotToToggle = _board.getSpotAt(columnClicked, bottommostColumn(s));
		spotToToggle.setSpotColor(player_color);
		spotToToggle.toggleSpot();
	
		/* Check if the game is over. */
		
		for (Spot spot: _board) {
			if (gameWon(spot)) {
				_game_won = true;
			}
		}
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
		boolean[] winPossibilities = {true, true, true, true, true, true, true, true};
		Color checkColor;
	
		if (y+4 <= 5) {
			for (int i=0; i<4; i++) {
			if (!_board.getSpotAt(x, y+i).isEmpty()) {
				checkColor = _board.getSpotAt(x, y+i).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[0] = false;
				}
			} else {
				winPossibilities[0] = false;
			}
			 
		}
		} else {
			winPossibilities[0] = false;
		}
		
		if (x+4 <= 6) {
			for (int i=0; i<4; i++) {
			if (!_board.getSpotAt(x+i, y).isEmpty()) {
				checkColor = _board.getSpotAt(x+i, y).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[1] = false;
				}
			} else {
				winPossibilities[1] = false;
			}
			
		}
		} else {
			winPossibilities[1] = false;
		}
		
		if (x+4 <= 6 && y+4 <= 5) {
			for (int i=0; i<4; i++) {
			if (!_board.getSpotAt(x+i, y+i).isEmpty()) {
				checkColor = _board.getSpotAt(x+i, y+i).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[2] = false;
				}
			} else {
				winPossibilities[2] = false;
			}
			
		}
		} else {
			winPossibilities[2] = false;
		}
		
		if (x+4 <= 6 && y-4 >= 0) {
			for (int i=0; i<4; i++) {
			if (!_board.getSpotAt(x+i, y-i).isEmpty()) {
				checkColor = _board.getSpotAt(x+i, y-i).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[3] = false;
				}
			} else {
				winPossibilities[3] = false;
			}
			
		}
		} else {
			winPossibilities[3] = false;
		}
		
		if (x-4 >= 0 && y-4 >= 0) {
			for (int i=0; i<4; i++) {
			if (!_board.getSpotAt(x-i, y-i).isEmpty()) {
				checkColor = _board.getSpotAt(x-i, y-i).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[4] = false;
				}
			} else {
				winPossibilities[4] = false;
			}
			
		}
		} else {
			winPossibilities[4] = false;
		}
		
		if (x-4 >= 0 && y+4 <= 5) {
			for (int i=0; i<4; i++) {
			if (!_board.getSpotAt(x-i, y+i).isEmpty()) {
				checkColor = _board.getSpotAt(x-i, y+i).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[5] = false;
				}
			} else {
				winPossibilities[5] = false;
			}
			
		} 
		} else {
			winPossibilities[5] = false;
		}
		
		if (y-4 >= 0) {
			for (int i=0; i<4; i++) {
			if (! _board.getSpotAt(x, y-i).isEmpty()) {
				checkColor = _board.getSpotAt(x, y-i).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[6] = false;
				}
			} else {
				winPossibilities[6] = false;
			}
			
		}
		} else {
			winPossibilities[6] = false;
		}
		
		if (x-4 >= 0) {
			for (int i=0; i<4; i++) {
			if (!_board.getSpotAt(x-i, y).isEmpty()) {
				checkColor = _board.getSpotAt(x-i, y).getSpotColor();
				if (!(color.equals(checkColor))) {
					winPossibilities[7] = false;
				}
			} else {
				winPossibilities[7] = false;
			}
			
		}
		} else {
			winPossibilities[7] = false;
		}
		
		for (boolean b: winPossibilities) {
			if (b) {
				return true;
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
	
	private boolean fullColumn(Spot s) {
		int columnClicked = s.getSpotX();
		int rowClicked = s.getSpotY();
		boolean fullColumn = true;
		
		for (int i=0; i<6; i++) {
			if (_board.getSpotAt(columnClicked, i).isEmpty()) {
				fullColumn = false;
			}
		}
		return fullColumn;
		
	}
	
	private int bottommostColumn(Spot s) {
		int bottommostColumn = 0;
		if (fullColumn(s)) {
			return bottommostColumn;
		}
		int columnClicked = s.getSpotX();
		int rowClicked = s.getSpotY();
		
		
		for (int i=0; i<6; i++) {
			if (_board.getSpotAt(columnClicked, i).isEmpty()) {
				bottommostColumn = i;
			} 
		}
		
		return bottommostColumn;
		
	}

	@Override
	public void spotEntered(Spot s) {
		int columnClicked = s.getSpotX();
		int rowClicked = s.getSpotY();
		
		/* Highlight spot if game still going on. */
		if (_game_won) {
			return;
		}
		if (fullColumn(s)) {
			return;
		}
		
		for (int i=0; i<6; i++) {
			if (_board.getSpotAt(columnClicked, i).isEmpty()) {
				_board.getSpotAt(columnClicked, i).highlightSpot();
			} 
		}
		
		
		
	}

	@Override
	public void spotExited(Spot s) {
		int columnClicked = s.getSpotX();
		int rowClicked = s.getSpotY();
		
		/* Unhighlight spot. */
		for (int i=0; i<6; i++) {
			if (_board.getSpotAt(columnClicked, i).isEmpty()) {
				_board.getSpotAt(columnClicked, i).unhighlightSpot();
			} else {
				_board.getSpotAt(columnClicked, i).unhighlightSpot();
			}
		}
		
	}
	
}

	