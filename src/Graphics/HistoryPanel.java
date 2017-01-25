package Graphics;

import GameLogic.History.RecordMove;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by giogio on 1/12/17.
 */
public class HistoryPanel extends JPanel{
    JScrollPane pane;
    JTextPane historyArea;
    JButton closeHistory;
    public HistoryPanel(GameGui gameGui){
        setLayout(new MigLayout("", "center"));
        setSize(new Dimension(150,400));
        setPreferredSize(new Dimension(150,400));
        historyArea = new JTextPane();
        pane = new JScrollPane(historyArea);
        pane.setSize(new Dimension(150,380));
        pane.setPreferredSize(new Dimension(150,380));
        this.setBackground(Color.lightGray);

        closeHistory = new JButton("Close History");
        this.add(pane,"span");
        this.add(closeHistory,"span");
        closeHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameGui.closeHistoryPanel();
            }
        });
    }
    public void updateHistory(ArrayList<RecordMove> records){
        historyArea.setText("");
        for(RecordMove rec: records){
            int x = rec.getX();
            int y = rec.getY();
            StyledDocument document = (StyledDocument) historyArea.getDocument();
            String s;
            StyleContext sc = new StyleContext();
            Style style = sc.addStyle("strikethru", null);
            StyleConstants.setStrikeThrough (style,true);
            s = rec.getColor() + ": (" + x + "," + y + ")\n";

            try {
                if(rec.isStatus())
                    document.insertString (document.getLength(), s, null);
                else
                    document.insertString (document.getLength(), s, style);
            }catch (BadLocationException e){
                System.out.println("Error: "+e);
            }

        }
    }
}
