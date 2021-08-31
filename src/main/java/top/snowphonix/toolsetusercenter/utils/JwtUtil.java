package top.snowphonix.toolsetusercenter.utils;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.snowphonix.toolsetusercenter.config.JwtConfig;
import top.snowphonix.toolsetusercenter.model.JwtPayload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Component
public class JwtUtil {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil(JwtConfig jwtConfig) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        byte[] publicFileBytes = readPemFile(jwtConfig.getPublicKeyPath());
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicFileBytes);

        byte[] privateFileBytes = readPemFile(jwtConfig.getPrivateKeyPath());
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateFileBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(publicSpec);
        this.privateKey = keyFactory.generatePrivate(privateSpec);
    }

    private byte[] readPemFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        StringBuilder sb = new StringBuilder();
        lines.stream().filter(s -> s.charAt(0) != '-').forEach(sb::append);
        return Base64.getDecoder().decode(sb.toString());
    }

    /***
     * 生成JWT。
     * 如果issuedAt为null，则为其生成当前时间。
     * 如果notBefore或auth为null，则不会出现在最终的JWT中。
     * 其余属性不应当为null。
     *
     * @param payload 需要JWT保存的信息
     * @return 生成的JWT字符串
     * @throws JoseException 生成JWT失败
     */
    public String generateJwt(JwtPayload payload) throws JoseException {

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setClaim("uid", payload.getUid());
        claims.setExpirationTime(
                TimeUtil.localDateTimeToNumericDate(payload.getExpire())); // time when the token will expire (10 minutes from now)
        //        claims.setGeneratedJwtId(); // a unique identifier for the token
        LocalDateTime issuedAt = payload.getIssuedAt();
        if (issuedAt == null) {
            issuedAt = LocalDateTime.now();
        }
        claims.setIssuedAt(TimeUtil.localDateTimeToNumericDate(issuedAt));  // when the token was issued/created (now)
        LocalDateTime notBefore = payload.getNotBefore();
        if (notBefore != null) {
            claims.setNotBefore(TimeUtil.localDateTimeToNumericDate(notBefore));
        }
        String auth = payload.getAuth();
        if (auth != null) {
            claims.setClaim("auth", auth);
        }

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        String actualPayload = claims.toJson();
        jws.setPayload(actualPayload);

        // The JWT is signed using the private key
        jws.setKey(this.privateKey);

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        String jwt = jws.getCompactSerialization();

        // Now you can do something with the JWT. Like send it to some other party
        // over the clouds and through the interwebs.
        logger.debug("payload: " + actualPayload);
        logger.debug("JWT: " + jwt);

        return jwt;
    }

    /***
     * 验证JWT，如果验证失败，返回null，否则返回其Payload中的信息。
     * 返回的Payload信息中，notBefore和auth可能会null，其余都不为null。
     *
     * @param jwt JWT字符串
     * @return 失败返回null，成功返回其中Payload中的信息
     */
    public JwtPayload validateToken(String jwt) {

        // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
        // be used to validate and process the JWT.
        // The specific validation requirements for a JWT are context dependent, however,
        // it typically advisable to require a (reasonable) expiration time, a trusted issuer, and
        // and audience that identifies your system as the intended recipient.
        // If the JWT is encrypted too, you need only provide a decryption key or
        // decryption key resolver to the builder.
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setEvaluationTime(TimeUtil.localDateTimeToNumericDate(LocalDateTime.now()))
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireIssuedAt() // the JWT must have an issued time
                .setVerificationKey(this.publicKey) // verify the signature with the public key
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT,
                        AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                .build(); // create the JwtConsumer instance

        try
        {
            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            Long uid = jwtClaims.getClaimValue("uid", Long.class);
            if (uid == null) {
                logger.error("Valid JWT without uid payload");
                return null;
            }
            JwtPayload payload = JwtPayload.builder()
                    .uid(Math.toIntExact(uid))
                    .expire(TimeUtil.numericDateToLocalDateTime(jwtClaims.getExpirationTime()))
                    .issuedAt(TimeUtil.numericDateToLocalDateTime(jwtClaims.getIssuedAt()))
                    .build();
            String auth = jwtClaims.getClaimValueAsString("auth");
            if (auth != null) {
                payload.setAuth(auth);
            }
            NumericDate notBefore = jwtClaims.getNotBefore();
            if (notBefore != null) {
                payload.setNotBefore(TimeUtil.numericDateToLocalDateTime(notBefore));
            }
            logger.debug("JWT validation succeeded! " + jwtClaims);
            return payload;
        }
        catch (MalformedClaimException e) {
            logger.error("Valid JWT with wrong type", e);
            return null;
        }
        catch (InvalidJwtException e) {
            // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
            // Hopefully with meaningful explanations(s) about what went wrong.
            logger.info("Invalid JWT! ", e);

            // Programmatic access to (some) specific reasons for JWT invalidity is also possible
            // should you want different error handling behavior for certain conditions.

            // Whether or not the JWT has expired being one common reason for invalidity
            if (e.hasExpired())
            {
                try {
                    NumericDate exp = e.getJwtContext().getJwtClaims().getExpirationTime();
                    logger.debug("JWT expired at " + exp);
//                    throw new TokenExpiredException(TimeUtil.numericDateToLocalDateTime(exp));
                }
                catch (MalformedClaimException me) {
                    logger.debug("JWT expired and has wrong expire type");
                }
            }
            return null;
        }
    }
}
