import java.math.BigInteger;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.web3j.crypto.ECDSASignature;

public class Secp256K1ECKeyPair {
  private final BigInteger privateKey;
  private final BigInteger publicKey;

  public Secp256K1ECKeyPair(BigInteger privateKey, BigInteger publicKey) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  public BigInteger getPrivateKey() {
    return privateKey;
  }

  public BigInteger getPublicKey() {
    return publicKey;
  }

  /**
   * Sign a hash with the private key of this key pair.
   *
   * @param transactionHash the hash to sign
   * @return An {@link ECDSASignature} of the hash
   */
  public ECDSASignature sign(byte[] transactionHash) {
    ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));

    ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKey, Secp256R1Sign.CURVE);
    signer.init(true, privKey);
    BigInteger[] components = signer.generateSignature(transactionHash);

    return new ECDSASignature(components[0], components[1]).toCanonicalised();
  }

  public static Secp256K1ECKeyPair create(BigInteger privateKey) {
    return new Secp256K1ECKeyPair(privateKey, Secp256R1Sign.publicKeyFromPrivate(privateKey));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Secp256K1ECKeyPair ecKeyPair = (Secp256K1ECKeyPair) o;

    if (privateKey != null
        ? !privateKey.equals(ecKeyPair.privateKey)
        : ecKeyPair.privateKey != null) {
      return false;
    }

    return publicKey != null ? publicKey.equals(ecKeyPair.publicKey) : ecKeyPair.publicKey == null;
  }

  @Override
  public int hashCode() {
    int result = privateKey != null ? privateKey.hashCode() : 0;
    result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
    return result;
  }
}
