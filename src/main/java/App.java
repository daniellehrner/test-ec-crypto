import java.math.BigInteger;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;

public class App {

  public static void main(String[] args) {
    final BigInteger privateKey =
        new BigInteger("977bec283a9964329f4f2822164fd468d8ee291c1c6f3631f76083f15d35507", 16);

    final Secp256K1ECKeyPair ecKeyPair = Secp256K1ECKeyPair.create(privateKey);

    System.out.println("Private key: " + ecKeyPair.getPrivateKey().toString(16));
    System.out.println("Public key: " + ecKeyPair.getPublicKey().toString(16));

    final RawTransaction etherTransaction =
        RawTransaction.createEtherTransaction(
            BigInteger.valueOf(1),
            BigInteger.valueOf(1),
            BigInteger.valueOf(1),
            "0x43e9be8ca773e10317d2effd790f9b1121bc5ea0",
            BigInteger.valueOf(1));

    final byte[] encodedEtherTransaction = TransactionEncoder.encode(etherTransaction);
    System.out.println("Encoded transaction: " + HexUtil.encodeHexString(encodedEtherTransaction));

    final Sign.SignatureData signatureData =
        Secp256R1Sign.signMessage(encodedEtherTransaction, ecKeyPair);

    final byte[] signedTransaction = EncodingUtil.encode(etherTransaction, signatureData);
    System.out.println("Signed transaction: " + HexUtil.encodeHexString(signedTransaction));

    final BigInteger recoveredPublicKey =
        Secp256R1Sign.recoverFromSignature(
            signatureData.getV()[0],
            new ECDSASignature(
                new BigInteger(signatureData.getR()), new BigInteger(signatureData.getS())),
            encodedEtherTransaction);

    if (recoveredPublicKey == null) {
      System.out.println("Public key recovery not possible");
      return;
    }

    System.out.println("Recovered public key: " + recoveredPublicKey.toString(16));
  }
}
