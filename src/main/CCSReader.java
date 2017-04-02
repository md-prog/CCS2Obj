package main;

import com.binaryfilereader.BinaryReader;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import utils.FileDrop;
import utils.FileDrop.Listener;

public class CCSReader extends JFrame
{
  public static int count = 0;
  private BinaryReader br;
  private StringBuffer log;
  private TextArea output;
  private JCheckBox cb;
  public CCSF ccs;
  
  public CCSReader()
  {
    setSize(330, 550);
    setTitle("CCS File Reader (MDL to OBJ converter)");
    setVisible(true);
    setDefaultCloseOperation(3);
    setLayout(new FlowLayout(0));
    output = new TextArea("", 25, 40, 1);
    add(output);
    add(this.cb = new JCheckBox("Ignore MDL"));
    add(new JLabel(" Drag 'n Drop a .CCS/.tmp file onto this window."));
    add(new JLabel(" All obj files will be saved to the \"output\" folder"));
    log = new StringBuffer(16777216);

    new FileDrop((java.awt.Component)this, new Listener() {
      public void filesDropped(File[] files) {
        for (File f : files) {
          if (f.isDirectory()) { traverse(f);
          } else {
            new CCSF(f, cb.isSelected());
            log.append("File " + f.getName() + " processed\n");
          }
        }
        
        log.append("Done!\n");
        output.setText(log.toString());
        output.setCaretPosition(output.getText().length());
      }
    });
    validate();
  }
  
  public static void main(String[] args) { new CCSReader(); }
  
  public void traverse(File f)
  {
    output.setText(log.toString());
    output.setCaretPosition(output.getText().length());
    if (f.isDirectory()) {
      log.append("Entering directory " + f.getName() + "\n");
      
      File[] children = f.listFiles();
      for (File child : children) {
        traverse(child);
      }
      return;
    }
    onFile(f);
  }
  
  public void onFile(File f) { new CCSF(f, cb.isSelected());
    log.append("File " + f.getName() + " processed\n");
  }
}
