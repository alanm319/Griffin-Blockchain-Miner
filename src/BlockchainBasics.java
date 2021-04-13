/**
 * This class manages the main thread, kicks off the Blockchain management classes, and controls the UI interaction.
 */
public class BlockchainBasics {

    public static String sRemoteMinerIP = null;
    public static int iRemoteMinerPort = 0;


    /**
     * This method manages the UI interaction and spawns off the Miner and P2PServer threads.
     * @param args
     */
    public static void main(String[] args){

        BlockchainUtil u = new BlockchainUtil();


        // Kick off miner.
        String sUsername = u.promptUser("Username for this miner? ");
        Miner oMiner = new Miner();
        oMiner.sUsername = sUsername;
        Thread oMinerThread = new Thread(oMiner);
        oMinerThread.start();


        // Sleep here so that miner thread can get up and running completely.
        u.sleep(500);


        // Start server after getting port from user.
        String sPort = u.promptUser("What port do you want to start server on? ");
        int iPort = Integer.parseInt(sPort);
        P2PServer oServer = new P2PServer(iPort);
        Thread oServerThread = new Thread(oServer);
        oServerThread.start();


        // Sleep here so that server thread can get up and running completely.
        u.sleep(500);


        String sSend = u.promptUser("Send transactions to another miner (y,n)? ");

        if(sSend.equals("y")){
            sRemoteMinerIP = u.promptUser("Miner's IP address? " );
            String sTempPort = u.promptUser("Miner's Port? ");
            iRemoteMinerPort = Integer.parseInt(sTempPort);
        }

        // Looping Menu: create & send TX OR create only.
        while(true){

            String sTransaction = u.promptUser("Enter Transaction: ");

            if(sSend.equals("y")) {
                String sReply = P2PUtil.connectForOneMessage(sRemoteMinerIP, iRemoteMinerPort, sTransaction);
                u.p("[main] Reply from server: " + sReply);
            }

            Miner.lstTransactionPool.add(sTransaction);
        }
    }
}
