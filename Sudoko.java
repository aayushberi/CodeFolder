import java.awt.BasicStroke;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.LayerUI;

public class Sudoko implements ActionListener {
	ArrayList<JTextField>textFieldList = new ArrayList<JTextField>();
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  public static void createAndShowGUI() {
    JFrame f = new JFrame("Suduko");
    Sudoko ss = new Sudoko();
    JPanel panel = ss.createPanel();
    LayerUI<JComponent> layerUI = new SudukoSolverLayer();
    JLayer<JComponent> jlayer = new JLayer<JComponent>(panel, layerUI);
    
    f.add (jlayer);
    f.setExtendedState(JFrame.MAXIMIZED_BOTH);
    f.setSize(1500, 1500);
    f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    f.setLocationRelativeTo (null);
    f.setVisible (true);
  }

  private JPanel createPanel() {
	  JPanel gui = new JPanel();
	  gui.setLayout(null);
		
	  JPanel panelForControls = new JPanel();
	  panelForControls.setLayout(null);
	  panelForControls.setLocation(0,0);
	  panelForControls.setSize(1500, 1500);
      gui.add(panelForControls);

      
      
      for(int outeriterator = 0;outeriterator<9;outeriterator++) {
    	  for(int inneriterator=0;inneriterator<9;inneriterator++) {
    		  JTextField jText = new JTextField();
    		  jText.setSize(50,25);
    		  jText.setLocation(80+(110*outeriterator),75+(70*inneriterator)); 
    		  jText.setHorizontalAlignment(JTextField.CENTER);
    		  textFieldList.add(jText);
    	  }	 
      }

      for(int iterator = 0;iterator < textFieldList.size();iterator++) {
		  panelForControls.add(textFieldList.get(iterator));
	  }
      
      JLabel jHeading = new JLabel("Sudoko Puzzle Solver");
      jHeading.setLocation(500, 0);
      jHeading.setSize(150,50);
      panelForControls.add(jHeading);
      
      JButton jbSolve = new JButton("Solve");
      jbSolve.setSize(150, 25);
      jbSolve.setLocation(1150, 75);
      panelForControls.add(jbSolve);
      
      jbSolve.setActionCommand("solve");
      jbSolve.addActionListener((ActionListener)this);
      
      JButton jbReset = new JButton("Reset");
      jbReset.setSize(150, 25);
      jbReset.setLocation(1150, 145);
      
      jbReset.setActionCommand("reset");
      jbReset.addActionListener((ActionListener)this);
      
      panelForControls.add(jbReset);
	  
	  return gui;
  }
  
  public void disableArrayList(ArrayList<JTextField>List) {
	  for(int iterator = 0;iterator < List.size();iterator++) {
		  List.get(iterator).setEnabled(false);
	  }
  }

  public boolean scanArrayList(ArrayList<JTextField>list) {
	  for(int iterator = 0;iterator < textFieldList.size();iterator++){ //Scans to check if input is valid
		  try {
			  if(!(textFieldList.get(iterator).getText().equals(""))) {
				  if (!((Integer.parseInt(textFieldList.get(iterator).getText())) > 0
						  && (Integer.parseInt(textFieldList.get(iterator).getText())) <=9)) {
					  JOptionPane.showMessageDialog(new Frame(), "Invalid input provided");
					  return false;	
				  }	
			  }					
		  }				
		  catch (Exception exception) {
			  JOptionPane.showMessageDialog(new Frame(), "Invalid input provided");
			  return false;
		  }				
	  }
	  return true;
  }
  
  public int[][] convertToArray(ArrayList<JTextField>list) {
	  
	  int rowIterator = 0;
	  int colIterator = 0;
	  int [][]squares = new int[9][9];
	  for(int iterator = 0;iterator<list.size();iterator++){
		  
		  if(textFieldList.get(iterator).getText().equals("")) {
			  squares[rowIterator][colIterator] = 0;
		  }
		  else {
			  squares[rowIterator][colIterator] = Integer.parseInt(textFieldList.get(iterator).getText());
		  }
		  rowIterator++;
		  		  
		  if(rowIterator == 9) {
			  rowIterator = 0;
			  colIterator++;
		  }		  
		  
		  if(colIterator == 9) {
			break;		
		  }			  
	  }
		  
	  return squares;	  
  }
  
  public boolean checkSquares(int val, int rowIterator, int colIterator,int [][]squares) {

	  int squareRowOffset;
	  int squareColOffset;
	  
	  for(int iterator = 0;iterator<9;iterator++) {
		  if(squares[rowIterator][iterator] == val) {
			  return false;
		  }
	  } 
	  
	  for(int iterator = 0;iterator<9;iterator++) {
		  if(squares[iterator][colIterator] == val) {
			  return false;
		  }
	  } 
	  
	  squareColOffset = (colIterator/3)*3;
	  squareRowOffset = (rowIterator/3)*3;
	  
	  for(int outerIterator = 0;outerIterator<3;outerIterator++) {
		  for(int innerIterator = 0;innerIterator<3;innerIterator++) {
			  if(squares[squareRowOffset+outerIterator][squareColOffset+innerIterator] == val) {
				  return false;
			  }
		  }
	  }
	  
	return true;	  
  }
  
  public boolean solveSquare(int rowIterator, int colIterator, int[][]squares) {
	  
	  if(colIterator == 9) {
		  colIterator = 0; //Reset to enter next row
		  rowIterator++;
		  if(rowIterator == 9) { //To check end
			  return true;
		  }
	  }
		  
	  if(squares[rowIterator][colIterator] != 0) { //Skip filled squares
		  return solveSquare(rowIterator, colIterator+1, squares);
	  }
	  
	  for(int checkVal = 1;checkVal <= 9;checkVal++) {
		  if(checkSquares(checkVal,rowIterator,colIterator,squares)){
			  squares[rowIterator][colIterator] = checkVal;
			  if(solveSquare(rowIterator, colIterator+1, squares)) {
				  return true;
			  }
		  }		  	  
	  }
	  squares[rowIterator][colIterator] = 0; //backtracking
	  return false;
  }
 
  public void setArrayList(int[][]squares,ArrayList<JTextField>list) {
	  int iterator = 0;
	  for(int colIterator=0;colIterator<9;colIterator++) {
		  for(int rowIterator=0;rowIterator<9;rowIterator++) {
			  list.get(iterator).setText(Integer.toString(squares[rowIterator][colIterator]));
			  iterator++;
		  }
	  }
  }
  
@Override
	public void actionPerformed(ActionEvent e) {		
		int [][]squares;
	
		if(e.getActionCommand() == "reset") {
			for(int iterator = 0;iterator < textFieldList.size();iterator++){
				textFieldList.get(iterator).setEnabled(true);
				textFieldList.get(iterator).setText("");				
			}	
		}
		else if(e.getActionCommand() == "solve") {
					
			disableArrayList(textFieldList);
			
			if(scanArrayList(textFieldList)) { //
				if((squares = convertToArray(textFieldList)) != null) {
					solveSquare(0,0,squares); //Start solving from 1st row and column
					setArrayList(squares,textFieldList);
				}	
			}			
		}		
	}
}


@SuppressWarnings("serial")
class SudokoSolverLayer extends LayerUI<JComponent> {
  
  int outer_x,outer_y;
  int row_x1,row_x2,row_y1,row_y2;
  int col_x1,col_x2,col_y1,col_y2;
  
  @Override
  public void paint(Graphics g, JComponent c) {
    super.paint(g, c);

    outer_x = 50;
	outer_y = 50;
	
	col_x1 = 160;
	col_x2 = 160;
	col_y1 = 50;
	col_y2 = 680;
	
	row_x1 = 50;
	row_x2 = 1040;
	row_y1 = 120;
	row_y2 = 120;
		
		
	
	Graphics2D g2 = (Graphics2D) g.create();	
	g2.setStroke(new BasicStroke(3));
	g2.drawRect(outer_x, outer_y, 990, 630);
  
	for(int iterator=1;iterator<=8;iterator++) {
		if(iterator % 3 != 0) {
			g.drawLine(col_x1, col_y1, col_x2, col_y2);
			g.drawLine(row_x1, row_y1, row_x2, row_y2);
		}
		else{
			g2.drawLine(col_x1, col_y1, col_x2, col_y2);
			g2.drawLine(row_x1, row_y1, row_x2, row_y2);
		}
		col_x1 += 110;
		col_x2 += 110;
		row_y1 += 70;
		row_y2 += 70;
	}		
  }
}