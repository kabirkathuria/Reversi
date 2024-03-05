package cs3500.reversi.provider.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs3500.reversi.provider.model.ReversiReadOnlyModel;

/**
 *  To represent the view of Hexagon Reversi. The is only made up of one panel and it registers
 *  itself as a listener for view features.
 */
public final class HexagonReversiView extends JFrame implements ReversiView {
  private final ReversiPanel reversiPanel;

  /**
   * Constructor used to create an instance of a HexagonReversiView.
   * @param model Read only model.
   */
  public HexagonReversiView(ReversiReadOnlyModel model) {
    this.setTitle("Reversi");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.reversiPanel = new ReversiPanel(model);
    this.add(reversiPanel);
    this.pack();

    this.setVisible(true);

  }

  @Override
  public void addFeatureListener(ViewFeatures features) {
    this.reversiPanel.addFeaturesListener(features);
  }

  @Override
  public void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message, "",
            JOptionPane.INFORMATION_MESSAGE);

  }

  @Override
  public void enableInput() {
    reversiPanel.enableInput();
  }

  @Override
  public void disableInput() {
    reversiPanel.disableInput();
  }

  @Override
  public void repaint() {
    reversiPanel.repaint();
  }


}
