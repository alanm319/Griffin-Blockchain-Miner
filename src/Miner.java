import java.util.ArrayList;


/**
 * This class runs on separate thread and manages the transaction queue and Block mining.
 */
public class Miner implements Runnable{


    public static volatile P2PMessageQueue oIncomingMessageQueue = new P2PMessageQueue();

    public static volatile boolean bAbortPoW = false;
    public static volatile ArrayList<String> lstTransactionPool = new ArrayList<>();
    int iBlockTxSize = 4;
    public String sUsername;



    /**
     * PoW is where miner keeps trying incrementing nonce until hash begins with as many 0s as the difficulty specifies.
     * @param oBlock
     * @return
     */
    public boolean doProofOfWork(Block oBlock) {

        int numberOfZeros = oBlock.getDifficulty();
		String StringWithZeros = "";                    //needs to be an empty String first
		int iNonce = 0;


		while(StringWithZeros.length() < numberOfZeros){
		    StringWithZeros = StringWithZeros + "0"; //add zeros to String
        }

		while(true){

		    if(bAbortPoW == true){                                                          //block has been mined by someone else
		        bAbortPoW = false;
                System.out.println("[miner] Aborted mining block. Another confirmed block received.");
                return false;
            }

		    else{                                                                           //attempt for a valid nonce
		        oBlock.setNonce(Integer.toString(iNonce));
		        oBlock.setHash(oBlock.computeHash());

		        if(oBlock.getHash().startsWith(StringWithZeros)){                           //if has the correct number of zeros, nonce is valid
                    return true;
                }
		        else{                                                                       //increase nonce by one to try again
		            iNonce++;
                }

            }
        }

    }



    /**
     * This thread monitors incoming messages, monitors the transaction queue, and mines Block if enough transactions collected.
     * Called as part of Runnable interface and shouldn't be called directly by code.
     */
    public void run() {

        BlockchainUtil u = new BlockchainUtil();

        u.p("Miner thread started.");


        // *****************************
        // *** Eternal Mining Loop *****
        // Because miner always checking for next block to immediately work on.
        while(true){

            u.sleep(500);

            while(oIncomingMessageQueue.hasNodes()){
                P2PMessage oMessage = oIncomingMessageQueue.dequeue();
                lstTransactionPool.add(oMessage.getMessage());
            }

            // Check if transaction pool full and lock if it is.
            if (lstTransactionPool.size() >= iBlockTxSize) {

                Block oBlock = new Block();
                oBlock.setMinerUsername(sUsername);
                oBlock.computeHash();
                String sMerkleRoot = oBlock.computeMerkleRoot(lstTransactionPool);
                oBlock.setMerkleRoot(sMerkleRoot);
                boolean bMined = doProofOfWork(oBlock);

                if(bMined){

                    // Notify connected node.
                    if(BlockchainBasics.sRemoteMinerIP != null){
                        P2PUtil.connectForOneMessage(BlockchainBasics.sRemoteMinerIP, BlockchainBasics.iRemoteMinerPort,
                                "mined");
                    }

                    u.p("");
                    u.p("***********");
                    u.p("BLOCK MINED");
                    u.p("nonce: " + oBlock.getNonce());
                    u.p("hash: " + oBlock.getHash());
                    u.p("");
                    u.p("Transactions:");
                    for(int x=0; x < lstTransactionPool.size(); x++){
                        u.p("Tx " + x + ": " + lstTransactionPool.get(x));
                    }
                    u.p("***********");
                }
                else{
                    u.p("[miner] Mining block failed.");
                }

                // Clear tx pool.
                lstTransactionPool.clear();
            }
        }
    }
}
