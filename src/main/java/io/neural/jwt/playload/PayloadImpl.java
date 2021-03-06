package io.neural.jwt.playload;

import static io.neural.jwt.claim.JsonNodeClaim.extractClaim;
import io.neural.jwt.claim.Claim;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The PayloadImpl class implements the Payload interface.
 */
class PayloadImpl implements Payload {
    private final String issuer;
    private final String subject;
    private final List<String> audience;
    private final Date expiresAt;
    private final Date notBefore;
    private final Date issuedAt;
    private final String jwtId;
    private final Map<String, JsonNode> tree;

    PayloadImpl(String issuer, String subject, List<String> audience, Date expiresAt, Date notBefore, Date issuedAt, String jwtId, Map<String, JsonNode> tree) {
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
        this.expiresAt = expiresAt;
        this.notBefore = notBefore;
        this.issuedAt = issuedAt;
        this.jwtId = jwtId;
        this.tree = Collections.unmodifiableMap(tree == null ? new HashMap<String, JsonNode>() : tree);
    }

    Map<String, JsonNode> getTree() {
        return tree;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public List<String> getAudience() {
        return audience;
    }

    @Override
    public Date getExpiresAt() {
        return expiresAt;
    }

    @Override
    public Date getNotBefore() {
        return notBefore;
    }

    @Override
    public Date getIssuedAt() {
        return issuedAt;
    }

    @Override
    public String getId() {
        return jwtId;
    }

    @Override
    public Claim getClaim(String name) {
        return extractClaim(name, tree);
    }

    @Override
    public Map<String, Claim> getClaims() {
        Map<String, Claim> claims = new HashMap<>();
        for (String name : tree.keySet()) {
            claims.put(name, extractClaim(name, tree));
        }
        return Collections.unmodifiableMap(claims);
    }
}
