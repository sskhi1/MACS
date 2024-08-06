import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris{
    private final Brain brain;
    protected JCheckBox brainMode;
    protected JCheckBox animateFallingMode;
    protected JSlider adversarySlider;
    protected JLabel okLabel;
    private Brain.Move move;
    private int count;

    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        this.brain = new DefaultBrain();
        this.count = 0;
    }

    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain Active");
        panel.add(brainMode);
        JPanel little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversarySlider = new JSlider(0, 100, 0); // min, max, current
        adversarySlider.setPreferredSize(new Dimension(100,15));
        little.add(adversarySlider);
        panel.add(little);

        okLabel = new JLabel("ok");
        panel.add(okLabel);

        animateFallingMode = new JCheckBox("Animate Falling");
        animateFallingMode.setSelected(true);
        panel.add(animateFallingMode);

        return panel;
    }

    @Override
    public void tick(int verb) {
        if(brainMode.isSelected() && verb == DOWN){
            if(count != super.count){
                count = super.count;
                board.undo();
                move = brain.bestMove(board, currentPiece, board.getHeight(), move);
            }
            if(move != null){
                if(!move.piece.equals(currentPiece)) {
                    super.tick(ROTATE);
                }
                if(move.x > currentX){
                    super.tick(RIGHT);
                }else if(move.x < currentX){
                    super.tick(LEFT);
                }else if(!animateFallingMode.isSelected() && move.y < currentY){
                    super.tick(DROP);
                }
            }
        }
        super.tick(verb);
    }

    @Override
    public Piece pickNextPiece() {
        if(random.nextInt(99) + 1 >= adversarySlider.getValue()){
            okLabel.setText("ok");
            return super.pickNextPiece();
        }else{
            okLabel.setText("*ok*");
            Piece res = super.pickNextPiece();
            double currentBest = 0;
            for(Piece piece : pieces){
                Brain.Move adversaryMove = brain.bestMove(board, piece, board.getHeight(), move);
                if(adversaryMove != null){
                    if(adversaryMove.score >= currentBest){
                        res = piece;
                        currentBest = adversaryMove.score;
                    }
                }
            }
            return res;
        }
    }
}
