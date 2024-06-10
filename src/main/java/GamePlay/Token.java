package GamePlay;

import java.util.UUID;

public class Token {
    private String tokenId;

    public Token() {
        this.tokenId = UUID.randomUUID().toString();
    }

    public String getTokenId() {
        return tokenId;
    }
}

