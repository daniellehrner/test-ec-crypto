import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

public class App {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final SecureRandom secureRandom = new SecureRandom ();
    private static final X9ECParameters curve = SECNamedCurves.getByName ("secp256r1");
    private static final ECDomainParameters domain = new ECDomainParameters(curve.getCurve (), curve.getG (), curve.getN (), curve.getH ());

    public static void main(String[] args) throws NoSuchAlgorithmException {
        ECKeyPairGenerator generator = new ECKeyPairGenerator ();
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters (domain, secureRandom);
        generator.init (keygenParams);
        AsymmetricCipherKeyPair keypair = generator.generateKeyPair ();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate ();

        BigInteger privateKey = privParams.getD();

        System.out.println("Private key: " + privateKey.toString(16));

        String message = "This is an example of a signed message.";
        MessageDigest digest = MessageDigest.getInstance("KECCAK-256");
        byte[] hashedMessage = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        ECDSASigner signer = new ECDSASigner (new HMacDSAKCalculator(new SHA256Digest()));
        signer.init (true, new ECPrivateKeyParameters (privateKey, domain));
        BigInteger[] signature = signer.generateSignature (hashedMessage);

        System.out.println("Signature[0]: " + signature[0].toString(16));
        System.out.println("Signature[1]: " + signature[1].toString(16));
    }
}
