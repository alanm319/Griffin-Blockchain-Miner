import java.util.ArrayList;


/**
 * This holds the values for a Block in the Blockchain, and it has methods to compute the Merkle Root and generate a hash.
 */
public class Block {


    private String sMerkleRoot;
    private int iDifficulty = 5; // Mining seconds in testing 5: 6,10,15,17,20,32 | testing 6: 12,289,218
    private String sNonce;
    private String sMinerUsername;
    private String sHash;



    /**
     * This computes the Merkle Root. It either accepts 2 or 4 items, or if made to be dynamic, then accepts any
     * multiple of 2 (2,4,8.16.32,etc.) items.
     * @param lstItems
     * @return
     */
    public synchronized String computeMerkleRoot(ArrayList<String> lstItems) {      //90% approach

        MerkleNode oNode1 = new MerkleNode();
        MerkleNode oNode2 = new MerkleNode();
        MerkleNode oNode3 = new MerkleNode();
        MerkleNode oNode4 = new MerkleNode();
        MerkleNode oNode5 = new MerkleNode();
        MerkleNode oNode6 = new MerkleNode();
        MerkleNode oNode7 = new MerkleNode();

        if(lstItems.size() == 4){                                           //4 leaves in merkleNode
            oNode1.sHash = BlockchainUtil.generateHash(lstItems.get(0));
            oNode2.sHash = BlockchainUtil.generateHash(lstItems.get(1));
            oNode3.sHash = BlockchainUtil.generateHash(lstItems.get(2));
            oNode4.sHash = BlockchainUtil.generateHash(lstItems.get(3));

            populateMerkleNode(oNode5, oNode1, oNode2);
            populateMerkleNode(oNode6, oNode3, oNode4);
            populateMerkleNode(oNode7, oNode5, oNode6);

            return oNode7.sHash;
        }
        else{                                                               //2 leaves in merkleNode

            oNode1.sHash = BlockchainUtil.generateHash(lstItems.get(0));
            oNode2.sHash = BlockchainUtil.generateHash(lstItems.get(1));

            populateMerkleNode(oNode7, oNode1, oNode2);

            return oNode7.sHash;
        }


    }



    /**
     * This method populates a Merkle node's left, right, and hash variables.
     * @param oNode
     * @param oLeftNode
     * @param oRightNode
     */
    private void populateMerkleNode(MerkleNode oNode, MerkleNode oLeftNode, MerkleNode oRightNode){

        oNode.oLeft = oLeftNode;
        oNode.oRight = oRightNode;
        oNode.sHash = BlockchainUtil.generateHash(oNode.oLeft.sHash + oNode.oRight.sHash);

    }


    // Hash this block, and hash will also be next block's previous hash.

    /**
     * This generates the hash for this block by combining the properties and hashing them.
     * @return
     */
    public String computeHash() {

        return new BlockchainUtil().generateHash(sMerkleRoot + iDifficulty + sMinerUsername + sNonce);
    }



    public int getDifficulty() {
        return iDifficulty;
    }


    public String getNonce() {
        return sNonce;
    }
    public void setNonce(String nonce) {
        this.sNonce = nonce;
    }

    public void setMinerUsername(String sMinerUsername) {
        this.sMinerUsername = sMinerUsername;
    }

    public String getHash() { return sHash; }
    public void setHash(String h) {
        this.sHash = h;
    }

    public synchronized void setMerkleRoot(String merkleRoot) { this.sMerkleRoot = merkleRoot; }




    /**
     * Run this to test your merkle tree logic.
     * @param args
     */
    public static void main(String[] args){

        ArrayList<String> lstItems = new ArrayList<>();
        Block oBlock = new Block();
        String sMerkleRoot;

        // These merkle root hashes based on "t1","t2" for two items, and then "t3","t4" added for four items.
        String sExpectedMerkleRoot_2Items = "3269f5f93615478d3d2b4a32023126ff1bf47ebc54c2c96651d2ac72e1c5e235";
        String sExpectedMerkleRoot_4Items = "e08f7b0331197112ff8aa7acdb4ecab1cfb9497cbfb84fb6d54f1cfdb0579d69";

        lstItems.add("t1");
        lstItems.add("t2");


        // *** BEGIN TEST 2 ITEMS ***

        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_2Items)){

            System.out.println("Merkle root method for 2 items worked!");
        }

        else{
            System.out.println("Merkle root method for 2 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_2Items);
            System.out.println("Received: " + sMerkleRoot);

        }


        // *** BEGIN TEST 4 ITEMS ***

        lstItems.add("t3");
        lstItems.add("t4");
        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_4Items)){

            System.out.println("Merkle root method for 4 items worked!");
        }

        else{
            System.out.println("Merkle root method for 4 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_4Items);
            System.out.println("Received: " + sMerkleRoot);

        }
    }
}
