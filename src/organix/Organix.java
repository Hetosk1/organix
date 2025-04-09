
package organix;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Organix {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {

        Home mainPage = new Home();
        
        mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPage.setTitle("Organix - Organ Donation Platform");
        
        mainPage.setLocationRelativeTo(null);
        
        mainPage.setVisible(true);
    }
}