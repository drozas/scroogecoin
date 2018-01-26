import java.util.ArrayList;

//See https://github.com/drozas/cryptocurrency-course-materials for tests
//See https://www.coursera.org/learn/cryptocurrency/programming/KOo3V/scrooge-coin/discussions for previous discussions

public class TxHandler {
	UTXOPool my_pool;
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
    	my_pool = new UTXOPool(utxoPool);
    	
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
   	
    	UTXOPool duplicated_utxo_pool = new UTXOPool();
    	
    	
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//Extra condition: check is not null!
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

    	if (tx == null) {
    		return false;
    	}
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
 	
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
        // First condition - all output claimed by tx are in current utxopool. 
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (Transaction.Input input: tx.getInputs()) {
        	//Creo utxo temporal con el hash de la transaccion anterior y su output
        	UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            // Si no esta en el pool, lanzo error. Esto seria equivalente a si no esta en la cadena de bloques?
            if (!my_pool.contains(utxo)) {
                return false;
            }

        }
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

        
        
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////    	
    	//Second condition the signatures on each input of {@code tx} are valid
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

        int i = 0;
        for (Transaction.Input input: tx.getInputs()) {
        	//Creo utxo temporal con el hash de la transaccion anterior y su output
        	UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
        	if (!Crypto.verifySignature(my_pool.getTxOutput(utxo).address, tx.getRawDataToSign(i), input.signature)){
        		return false;
        	}
        	i++;
        }
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//Fourth condition:  all of {@code tx}s output values are non-negative
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
        for (Transaction.Output o : tx.getOutputs()) {
			if (o.value<0){
				return false;
			}
		}
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

    	
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//Fifth condition:  (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output values; and false otherwise.
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

    	double total_value_outputs = 0;
		double total_value_inputs = 0;

    	//Calculate total value outputs in this transaction
		for (Transaction.Output o : tx.getOutputs()) {
    		total_value_outputs += o.value;
		}
    	
    	//Calculate total value inputs going through previous transactions outputs
		for (Transaction.Input input: tx.getInputs()) {
			//Creo utxo temporal con el hash de la transaccion anterior y su output
        	UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
        	total_value_inputs += my_pool.getTxOutput(utxo).value;
        	
        }
    	
		//Carry out comparison for (5)
    	if (total_value_inputs<total_value_outputs){
    		return false;
    	}
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
    	
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//Third condition UTXO is claimed multiple times by {@code tx}
    	//We do this at the end because we need to add it only if it passes all of the previous conditions
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
    	//Check if utxo in our duplicated pool checker
		for (Transaction.Input input: tx.getInputs()) {
			//Creo utxo temporal con el hash de la transaccion anterior y su output
        	UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
        	if (duplicated_utxo_pool.contains(utxo)){
        		return false;
        	}else{
        		duplicated_utxo_pool.addUTXO(utxo, my_pool.getTxOutput(utxo));
        	}
        }

    	/////////////////////////////////////////////////////////////////////////////////////////////////////////

    	return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {

    	//Inspect whole array of proposed transaction
    	for (Transaction tx: possibleTxs) {
    		//If the transaction is valid, anado all of the utxo of all of the inputs al pool
    		for (Transaction.Input input: tx.getInputs()) {
    			//Creo utxo temporal con el hash de la transaccion anterior y su output
            	UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            	//my_pool.addUTXO(uxto, my_pool.getTxOutput(utxo)); // THIS DOES NOT MAKE SENSE... ASK SEM point to possible Txs instead?
            }

    	}
    	
    	return possibleTxs;
    }

}
