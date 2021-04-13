import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;

/**
 * This class has utility methods used by the Blockchain logic.
 */
public class BlockchainUtil {

    /**
     * This method produces an SHA-256 hash of the string input.
     * @param sOriginal
     * @return
     */
    public static synchronized String generateHash(String sOriginal){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] btEncodedhash = digest.digest(
                    sOriginal.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < btEncodedhash.length; i++) {
                sb.append(Integer.toString((btEncodedhash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
        catch(Exception ex){

            System.out.println("Error generating hash: " + ex.getMessage());
            return null;
        }
    }


    /**
     * This method can either use Scanner or JOptionPane approaches to get user input.
     * @param sQuestion
     * @return
     */
    public String promptUser(String sQuestion){

        // Using input dialog:
        //return JOptionPane.showInputDialog(sQuestion);

        // Using Scanner:
        System.out.print(sQuestion);
        Scanner oCommandInput = new Scanner(System.in);

        return oCommandInput.nextLine();
    }


    /**
     * This method allows user to avoid extra typing and efficient code, especially if this class variable is one letter
     * such as u, thus:
     * u.p("hello");
     * @param sMessage
     */
    public void p(String sMessage){
        System.out.println(sMessage);
    }


    /**
     * This is used by any thread that needs to sleep to allow updating of static variables or to alleviate
     * CPU usage on continuous looping.
     * @param lMillis
     */
    public void sleep(long lMillis){

        try{
            Thread.sleep(lMillis);
        }
        catch(Exception ex){
            // do nothing.
        }
    }
}
