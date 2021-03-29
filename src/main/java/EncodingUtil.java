import java.util.List;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpType;

public class EncodingUtil {
  public static byte[] encode(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
    List<RlpType> values = TransactionEncoder.asRlpValues(rawTransaction, signatureData);
    RlpList rlpList = new RlpList(values);
    return RlpEncoder.encode(rlpList);
  }
}
