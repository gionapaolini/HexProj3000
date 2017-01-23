package BotAlgorithms.Testicus;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Nibbla on 22.01.2017.
 */
public class Genome {

    private String name;

    public void jkj() throws IOException {
        /*
        JFileChooser fileChooser = new JFileChooser("./Saved");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("HEX GAME FILE", "hex", "Hex Dump");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            FileReader fr;
            fr = new FileReader(file);
            String line;
            BufferedReader reader = new BufferedReader(fr);
            Genome gene = new Genome();
            while (true){
                line = reader.readLine();
                if(line==null) {
                    break;
                }
                String[] currentLine = line.split(" ");
                if(currentLine[0].equals("Name")){
                  //  name  = Integer.parseInt(currentLine[1]);
                }else if(currentLine[0].equals("time")) {

                }else {
                    boolean status;
                    StatusCell player;
                    byte row, column;

                    if(currentLine[1].equals("Blue")){
                        player = StatusCell.Blue;
                    }else {
                        player = StatusCell.Red;
                    }
                    column = (byte)Integer.parseInt(currentLine[2]);
                    row = (byte)Integer.parseInt(currentLine[3]);
                    if(currentLine[4].equals("false")){
                        status = false;
                    }else {
                        status = true;
                    }
                    history.addRecord(new RecordMove(player,column,row,status));


                }



            }

        }

*/
    }

}
